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

import java.io.IOException;
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
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkSession;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkerRoute;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.WorkSessionOperations;
import cu.phibrain.plugins.cardinal.io.database.objects.ItemComparators;
import eu.geopaparazzi.core.database.DaoGpsLog;
import eu.geopaparazzi.core.database.objects.Line;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.util.Compat;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.LibraryConstants;
import eu.geopaparazzi.library.util.StringAsyncTask;
import eu.geopaparazzi.map.GPGeoPoint;
import eu.geopaparazzi.map.MapsSupportService;

public class SessionsStatsActivity extends AppCompatActivity {

    private String[] mapobjectsCodes;
    private Map<String, MapObject> mapObjectMap = new HashMap<>();
    private Comparator<MapObject> mapObjectComparator = new ItemComparators.MapObjectComparator(false);
    private ListView listView;
    private EditText filterText;
    private TextView tvCapturePoint;
    private TextView trackLengthTextView;
    private WorkSession wsa;
    private AppContainer appContainer;
    private int incompledCounter;

    private double lengthm;
    private StringAsyncTask task = null;
    private TextView incompleteMOTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions_stats);

        appContainer = ((CardinalApplication) getApplication()).getContainer();

        Toolbar toolbar = findViewById(R.id.map_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (null != toolbar) {
            toolbar.setNavigationOnClickListener(v -> NavUtils.navigateUpFromSameTask(SessionsStatsActivity.this));

        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        listView = findViewById(R.id.mapObjectsList);
        wsa = appContainer.getWorkSessionActive();
        incompledCounter = 0;

        tvCapturePoint = findViewById(R.id.tvCapturePoint);
        trackLengthTextView = findViewById(R.id.tvKm);

        incompleteMOTextView = findViewById(R.id.tvIncompleteMO);
        try {


            task = new StringAsyncTask(SessionsStatsActivity.this) {
                @Override
                protected void doUiPostWork(String response) {
                    trackLengthTextView.setText(response);
                    dispose();
                }

                @Override
                protected String doBackgroundWork() {
                    lengthm = 0.0;
                    wsa.resetWorkerRoute();
                    for (WorkerRoute log :
                            wsa.getWorkerRoute()) {
                        try {
                            Line line = DaoGpsLog.getGpslogAsLine(log.getGpsLogsTableId(), -1);
                            lengthm += line.getLength();
                        } catch (IOException e) {
                            GPLog.error(this, "ERROR", e);//NON-NLS
                        }
                    }
                    return getString(R.string.km_covered, lengthm / 1000);//NON-NLS
                }
            };
            task.setProgressDialog(null, getString(eu.geopaparazzi.core.R.string.calculate_length), false, null);
            task.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        final ImageButton chartButton = findViewById(R.id.stats_gpslog_chart);
//        chartButton.setOnClickListener(v -> {
//            Intent intent = new Intent(SessionsStatsActivity.this, ProfileChartActivity.class);
//            intent.putExtra(Constants.ID, item.getId());
//            startActivity(intent);
//        });

        refreshList();

        filterText = findViewById(R.id.search_box_mo);
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

        incompledCounter = 0;

        List<MapObject> mapObjectList = WorkSessionOperations.getInstance().getMapObjects(wsa.getId());

        Collections.sort(mapObjectList, mapObjectComparator);
        mapobjectsCodes = new String[mapObjectList.size()];
        mapObjectMap.clear();
        int index = 0;
        for (MapObject mapObject : mapObjectList) {
            String code = mapObject.getCode();
            mapObjectMap.put(code, mapObject);
            mapobjectsCodes[index] = code;
            //check completitud del nodo
            if (!mapObject.getIsCompleted())
                incompledCounter++;
            index++;
        }

        tvCapturePoint.setText(getString(R.string.captured_points, mapObjectList.size()));
        incompleteMOTextView.setText(getString(R.string.map_object_incomplete_count, incompledCounter));

        redoAdapter();
    }

    private void filterList(String filterText) {
        if (GPLog.LOG_HEAVY)
            GPLog.addLogEntry(this, "filter a map objects list"); //$NON-NLS-1$

        List<MapObject> mapObjectList = WorkSessionOperations.getInstance().getMapObjects(wsa.getId());

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

                if (!mapObject.getIsCompleted())
                    mapObjectText.setTextColor(Compat.getColor(parent.getContext(), R.color.main_selection));
//                    rowView.setBackgroundColor(Compat.getColor(parent.getContext(), R.color.main_selection));

                final ImageButton deleteButton = rowView.findViewById(R.id.deleteMObutton);
                deleteButton.setOnClickListener(v -> {
                    final String code = mapObjectText.getText().toString();
                    final MapObject mapObject1 = mapObjectMap.get(code);
                    GPDialogs.yesNoMessageDialog(SessionsStatsActivity.this, getString(R.string.do_you_want_to_delete_this_map_object),
                            new Runnable() {
                                public void run() {
                                    new AsyncTask<String, Void, String>() {
                                        protected String doInBackground(String... params) {
                                            return ""; //$NON-NLS-1$
                                        }

                                        protected void onPostExecute(String response) {
                                            MapObjectOperations.getInstance().delete(mapObject1.getId());
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

                        GPGeoPoint centerPoint = mapObject2.getCentroid();

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
            // arrayAdapter.getFilter().filter(s);
            filterList(s.toString());
        }
    };
}
