package cu.phibrain.cardinal.app.ui.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.cardinal.app.R;
import cu.phibrain.cardinal.app.helpers.LatLongUtils;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectOperations;
import cu.phibrain.plugins.cardinal.io.database.objects.ItemComparators;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.LibraryConstants;
import eu.geopaparazzi.map.GPGeoPoint;
import eu.geopaparazzi.map.MapsSupportService;

public class MapObjectJoinedActivity extends AppCompatActivity {
    private String[] mapobjectsCodes;
    private Map<String, MapObject> mapObjectMap = new HashMap<>();
    private Comparator<MapObject> mapObjectComparator = new ItemComparators.MapObjectComparator(false);
    private ListView listView;
    private EditText filterText;
    private AppContainer appContainer;
    private MapObject currentMOA;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_object_joined);

        appContainer = ((CardinalApplication) getApplication()).getContainer();
        currentMOA = appContainer.getCurrentMapObject();


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(String.format(getResources().getString(R.string.title_activity_map_object_joined), currentMOA.getCode()));

        if (null != toolbar) {
            toolbar.setNavigationOnClickListener(v -> NavUtils.navigateUpFromSameTask(this));

        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        listView = findViewById(R.id.joinedList);


        refreshList();

        filterText = findViewById(R.id.join_search_box);
        filterText.addTextChangedListener(filterTextWatcher);
    }

    @Override
    protected void onResume() {
        super.onResume();

        String filterStr = filterText.getText().toString();
        if (filterStr.length() > 0) {
            filterList(filterStr);
        } else {
            refreshList();
        }
    }

    protected void onDestroy() {
        super.onDestroy();
        filterText.removeTextChangedListener(filterTextWatcher);
    }

    private void refreshList() {
        if (GPLog.LOG_HEAVY)
            GPLog.addLogEntry(this, "refreshing map objects list"); //$NON-NLS-1$


        List<MapObject> mapObjectList = MapObjectOperations.getInstance().getJoinedList(currentMOA.getId());

        Collections.sort(mapObjectList, mapObjectComparator);
        mapobjectsCodes = new String[mapObjectList.size()];
        mapObjectMap.clear();
        int index = 0;
        for (MapObject mapObject : mapObjectList) {
            String code = mapObject.getCode();
            mapObjectMap.put(code, mapObject);
            mapobjectsCodes[index] = code;
            index++;
        }


        redoAdapter();
    }

    private void filterList(String filterText) {
        if (GPLog.LOG_HEAVY)
            GPLog.addLogEntry(this, "filter a map objects list"); //$NON-NLS-1$

        List<MapObject> mapObjectList = MapObjectOperations.getInstance().getJoinedList(currentMOA.getId());

        Collections.sort(mapObjectList, mapObjectComparator);

        mapObjectMap.clear();
        filterText = ".*" + filterText.toLowerCase() + ".*"; //$NON-NLS-1$ //$NON-NLS-2$
        List<String> codeList = new ArrayList<>();
        for (MapObject mapObject : mapObjectList) {
            String code = mapObject.getCode();
            String codeLower = code.toLowerCase();
            if (codeLower.matches(filterText)) {
                codeList.add(code);
                mapObjectMap.put(code, mapObject);
            }
        }

        mapobjectsCodes = codeList.toArray(new String[codeList.size()]);

        redoAdapter();
    }

    private void redoAdapter() {

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, eu.geopaparazzi.core.R.layout.activity_bookmarkslist_row, mapobjectsCodes) {
            @Override
            public View getView(int position, View cView, ViewGroup parent) {
                final View rowView = getLayoutInflater().inflate(R.layout.activity_sessions_stats_row, parent, false);

                final TextView mapObjectText = rowView.findViewById(R.id.mapobjectsrowtext);
                MapObject mapObject = mapObjectMap.get(mapobjectsCodes[position]);
                mapObjectText.setText(mapObject.getCode());

                final ImageButton undockButton = rowView.findViewById(R.id.deleteMObutton);
                undockButton.setOnClickListener(v -> {
//                    final String code = mapObjectText.getText().toString();
//                    final MapObject mapObject1 = mapObjectMap.get(code);
                    GPDialogs.yesNoMessageDialog(MapObjectJoinedActivity.this, getString(R.string.do_you_want_to_undock_this_map_object),
                            new Runnable() {
                                public void run() {
                                    new AsyncTask<String, Void, String>() {
                                        protected String doInBackground(String... params) {
                                            return ""; //$NON-NLS-1$
                                        }

                                        protected void onPostExecute(String response) {
                                            mapObject.setJoinId(null);

                                            MapObjectOperations.getInstance().save(mapObject);
                                            refreshList();
                                        }
                                    }.execute((String) null);

                                }
                            }, null);

                });

                final ImageButton goButton = rowView.findViewById(R.id.gotobutton);
                goButton.setOnClickListener(v -> {
                    final MapObject mapObject2 = mapObjectMap.get(mapObjectText.getText().toString());
                    if (mapObject2 != null) {

                        GPGeoPoint centerPoint = LatLongUtils.centerPoint(mapObject2.getCoord(), mapObject2.getObjectType().getGeomType());

                        Intent intent = new Intent(getContext(), MapsSupportService.class);
                        intent.putExtra(MapsSupportService.CENTER_ON_POSITION_REQUEST, true);
                        intent.putExtra(LibraryConstants.LONGITUDE, centerPoint.getLongitude());
                        intent.putExtra(LibraryConstants.LATITUDE, centerPoint.getLatitude());
                        intent.putExtra(LibraryConstants.ZOOMLEVEL, (int) mapObject2.getElevation() <= 0 ? LatLongUtils.getLineAndPolygonViewZoom() : (int) mapObject2.getElevation());
                        getContext().startService(intent);

                    }
                    finish();
                });

                return rowView;
            }

        };

        listView.setAdapter(arrayAdapter);

    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            // ignore
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // ignore
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            filterList(s.toString());
        }
    };
}
