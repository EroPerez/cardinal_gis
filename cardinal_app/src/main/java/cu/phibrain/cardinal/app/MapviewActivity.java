/*
 * Geopaparazzi - Digital field mapping on Android based devices
 * Copyright (C) 2016  HydroloGIS (www.hydrologis.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cu.phibrain.cardinal.app;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.BatteryManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.oscim.core.BoundingBox;
import org.oscim.core.MapPosition;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import cu.phibrain.cardinal.app.helpers.LatLongUtils;
import cu.phibrain.cardinal.app.helpers.SignalEventLogger;
import cu.phibrain.cardinal.app.helpers.StorageUtilities;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.cardinal.app.injections.UserMode;
import cu.phibrain.cardinal.app.ui.activities.MapObjectJoinedActivity;
import cu.phibrain.cardinal.app.ui.activities.SessionsStatsActivity;
import cu.phibrain.cardinal.app.ui.adapter.MtoAdapter;
import cu.phibrain.cardinal.app.ui.adapter.NetworkAdapter;
import cu.phibrain.cardinal.app.ui.fragment.BarcodeReaderDialogFragment;
import cu.phibrain.cardinal.app.ui.fragment.ObjectInspectorDialogFragment;
import cu.phibrain.cardinal.app.ui.layer.BifurcationLayer;
import cu.phibrain.cardinal.app.ui.layer.CardinalEdgesLayer;
import cu.phibrain.cardinal.app.ui.layer.CardinalGPMapView;
import cu.phibrain.cardinal.app.ui.layer.CardinalJoinsLayer;
import cu.phibrain.cardinal.app.ui.layer.CardinalLayerManager;
import cu.phibrain.cardinal.app.ui.layer.CardinalLineLayer;
import cu.phibrain.cardinal.app.ui.layer.CardinalPointLayer;
import cu.phibrain.cardinal.app.ui.layer.CardinalPolygonLayer;
import cu.phibrain.cardinal.app.ui.layer.CardinalSelectPointLayer;
import cu.phibrain.cardinal.app.ui.map.CardinalMapLayerListActivity;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Layer;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Networks;
import cu.phibrain.plugins.cardinal.io.database.entity.model.RouteSegment;
import cu.phibrain.plugins.cardinal.io.database.entity.model.SignalEvents;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkSession;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Zone;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.LayerOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjecTypeOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.NetworksOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.RouteSegmentOperations;
import cu.phibrain.plugins.cardinal.io.utils.ImageUtil;
import eu.geopaparazzi.core.database.DaoBookmarks;
import eu.geopaparazzi.core.database.DaoGpsLog;
import eu.geopaparazzi.core.mapview.GpsLogInfoTool;
import eu.geopaparazzi.core.mapview.PanLabelsTool;
import eu.geopaparazzi.core.mapview.TapMeasureTool;
import eu.geopaparazzi.core.ui.activities.BookmarksListActivity;
import eu.geopaparazzi.core.ui.activities.GpsDataListActivity;
import eu.geopaparazzi.core.utilities.Constants;
import eu.geopaparazzi.library.core.activities.GeocodeActivity;
import eu.geopaparazzi.library.core.dialogs.InsertCoordinatesDialogFragment;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.gps.GpsLoggingStatus;
import eu.geopaparazzi.library.gps.GpsServiceStatus;
import eu.geopaparazzi.library.gps.GpsServiceUtilities;
import eu.geopaparazzi.library.images.ImageUtilities;
import eu.geopaparazzi.library.network.NetworkUtilities;
import eu.geopaparazzi.library.share.ShareUtilities;
import eu.geopaparazzi.library.style.ColorUtilities;
import eu.geopaparazzi.library.util.AppsUtilities;
import eu.geopaparazzi.library.util.Compat;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.IActivitySupporter;
import eu.geopaparazzi.library.util.LibraryConstants;
import eu.geopaparazzi.library.util.PositionUtilities;
import eu.geopaparazzi.library.util.TextRunnable;
import eu.geopaparazzi.library.util.TimeUtilities;
import eu.geopaparazzi.map.GPBBox;
import eu.geopaparazzi.map.GPGeoPoint;
import eu.geopaparazzi.map.GPMapPosition;
import eu.geopaparazzi.map.GPMapView;
import eu.geopaparazzi.map.MapsSupportService;
import eu.geopaparazzi.map.features.Feature;
import eu.geopaparazzi.map.features.FeatureUtilities;
import eu.geopaparazzi.map.features.editing.EditManager;
import eu.geopaparazzi.map.features.editing.EditingView;
import eu.geopaparazzi.map.features.tools.MapTool;
import eu.geopaparazzi.map.features.tools.impl.NoEditableLayerToolGroup;
import eu.geopaparazzi.map.features.tools.impl.OnSelectionToolGroup;
import eu.geopaparazzi.map.features.tools.interfaces.Tool;
import eu.geopaparazzi.map.features.tools.interfaces.ToolGroup;
import eu.geopaparazzi.map.layers.interfaces.IEditableLayer;
import eu.geopaparazzi.map.layers.interfaces.IGpLayer;
import eu.geopaparazzi.map.layers.interfaces.ILabeledLayer;
import eu.geopaparazzi.map.layers.systemlayers.BookmarkLayer;
import eu.geopaparazzi.map.utils.MapUtilities;

import static eu.geopaparazzi.library.util.LibraryConstants.COORDINATE_FORMATTER;
import static eu.geopaparazzi.library.util.LibraryConstants.DEFAULT_LOG_WIDTH;
import static eu.geopaparazzi.library.util.LibraryConstants.LATITUDE;
import static eu.geopaparazzi.library.util.LibraryConstants.LONGITUDE;
import static eu.geopaparazzi.library.util.LibraryConstants.NAME;
import static eu.geopaparazzi.library.util.LibraryConstants.ROUTE;
import static eu.geopaparazzi.library.util.LibraryConstants.ZOOMLEVEL;


/**
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class MapviewActivity extends AppCompatActivity implements MtoAdapter.SelectedMto, IActivitySupporter, OnTouchListener, OnClickListener, OnLongClickListener, InsertCoordinatesDialogFragment.IInsertCoordinateListener, GPMapView.GPMapUpdateListener {
    public static final String MAPSCALE_X = "MAPSCALE_X"; //$NON-NLS-1$
    public static final String MAPSCALE_Y = "MAPSCALE_Y"; //$NON-NLS-1$
    //Update MOA
    public static final String ACTION_UPDATE_UI = "cu.phibrain.cardinal.app.UI_REFRESH";
    private static final String ARE_BUTTONSVISIBLE_OPEN = "ARE_BUTTONSVISIBLE_OPEN"; //$NON-NLS-1$
    private final int INSERTCOORD_RETURN_CODE = 666;
    private final int ZOOM_RETURN_CODE = 667;
    private final int MENU_GO_TO = 1;
    private final int MENU_COMPASS_ID = 2;
    private final int MENU_SHAREPOSITION_ID = 3;
    private DecimalFormat formatter = new DecimalFormat("00"); //$NON-NLS-1$
    private CardinalGPMapView mapView;
    private SharedPreferences mPeferences;
    private int currentZoomLevel;
    private BroadcastReceiver gpsServiceBroadcastReceiver;
    private double[] lastGpsPosition;
    private TextView zoomLevelText;
    private TextView descriptorMto;
    private ImageButton buttom_sheet_background;
    private ImageView selectMto;
    private ImageView selectMo;
    private FrameLayout fragmentCenter;
    private ImageButton centerOnGps;
    private ImageButton batteryButton;
    private BroadcastReceiver mapsSupportBroadcastReceiver;
    private TextView coordView;
    private String latString;
    private String lonString;
    private TextView batteryText;
    private Spinner filterNetworks;
    private ImageButton toggleEditingButton;
    private ImageButton toggleLabelsButton;
    private boolean hasLabelledLayers;
    private AppContainer appContainer;
    private ImageButton addRouteSegmentbutton;
    private ImageButton joinButton;

    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int maxValue = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int chargedPct = (level * 100) / maxValue;
            updateBatteryCondition(chargedPct);

            //register signal event only when battery is low
            boolean isBatteryLow = chargedPct <= 20; //intent.getBooleanExtra(BatteryManager.EXTRA_BATTERY_LOW, false);
            if (isBatteryLow && appContainer.getWorkSessionActive() != null) {
                SignalEventLogger.addEventLogEntry(SignalEvents.SignalTypes.POWER, appContainer.getWorkSessionActive().getId(), chargedPct, new Date(), lastGpsPosition);
            }
        }

    };

    private BroadcastReceiver storageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long level = StorageUtilities.getAvailableMemorySizeInMainStorageDir(context);

            //register signal event only when battery is low
            if (appContainer.getWorkSessionActive() != null) {
                SignalEventLogger.addEventLogEntry(SignalEvents.SignalTypes.STORAGE, appContainer.getWorkSessionActive().getId(), level, new Date(), lastGpsPosition);
            }
        }

    };

    private BroadcastReceiver mMessageUiUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            boolean update_map_object_active = intent.getBooleanExtra("update_map_object_active", false);
            if (update_map_object_active) {
                MapObject moa = appContainer.getCurrentMapObject();
                setToggleMapObjectTools(moa);
                if (moa != null) {
                    updateSelectMapObj(moa.getObjectType());
                    GPGeoPoint point = moa.getCentroid();
                    GPMapPosition mapPosition = mapView.getMapPosition();
                    int zoomLevel = mapPosition.getZoomLevel();
                    zoomLevel = (moa.getLayer().getEditZoomLevel() < zoomLevel) ? zoomLevel : moa.getLayer().getEditZoomLevel();
                    setNewCenterAtZoom(point.getLongitude(), point.getLatitude(), zoomLevel);
                } else {
                    updateSelectMapObj(null);
                }

            }


            boolean update_map_object_type_active = intent.getBooleanExtra("update_map_object_type_active", false);

            if (update_map_object_type_active) {
                MapObjecType mot = appContainer.getMapObjecTypeActive();
                if (mot != null) {
                    selectedMto(mot);
                } else {
                    selectedMto(null);

                }
            }

            boolean edit_map_object_active_coord = intent.getBooleanExtra("edit_map_object_active_coord", false);

            if (edit_map_object_active_coord) {
                MapObject moa = appContainer.getCurrentMapObject();
                if (moa != null) {
                    GPGeoPoint point = moa.getCentroid();
                    GPMapPosition mapPosition = mapView.getMapPosition();
                    int zoomLevel = mapPosition.getZoomLevel();

                    zoomLevel = (moa.getLayer().getEditZoomLevel() < zoomLevel) ? zoomLevel : moa.getLayer().getEditZoomLevel();

                    setNewCenterAtZoom(point.getLongitude(), point.getLatitude(), zoomLevel);

                    MapObjecType mot = moa.getObjectType();
                    if (mot != null) {
                        appContainer.setMode(UserMode.OBJECT_COORD_EDITION);
                        selectedMto(mot);
                    }
                }
            }

            boolean is_map_object_terminal = intent.getBooleanExtra("is_map_object_terminal", false);

            if (is_map_object_terminal) {
                disableEditing();
            }

            Long create_map_object_by_select_edge = intent.getLongExtra("create_map_object_by_select_edge", -1L);

            if (create_map_object_by_select_edge > 0) {
                RouteSegment edge = RouteSegmentOperations.getInstance().load(create_map_object_by_select_edge);
                appContainer.setRouteSegmentActive(edge);
                onMenuMTO();

            }
        }

    };


    private BoundingBox sessionBBox;

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(cu.phibrain.cardinal.app.R.layout.activity_mapview);

        this.appContainer = ((CardinalApplication) getApplication()).getContainer();

        mapsSupportBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (intent.hasExtra(MapsSupportService.CENTER_ON_POSITION_REQUEST)) {
                    boolean centerOnPosition = intent.getBooleanExtra(MapsSupportService.CENTER_ON_POSITION_REQUEST, false);
                    if (centerOnPosition) {
                        double lon = intent.getDoubleExtra(LONGITUDE, 0.0);
                        double lat = intent.getDoubleExtra(LATITUDE, 0.0);
                        setNewCenter(lon, lat);
                    }
                }
            }
        };
        registerReceiver(mapsSupportBroadcastReceiver, new IntentFilter(
                MapsSupportService.MAPSSUPPORT_SERVICE_BROADCAST_NOTIFICATION));

        gpsServiceBroadcastReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                onGpsServiceUpdate(intent);
            }
        };

        mPeferences = PreferenceManager.getDefaultSharedPreferences(this);

        // COORDINATE TEXT VIEW
        coordView = findViewById(cu.phibrain.cardinal.app.R.id.coordsText);
        latString = getString(R.string.lat);
        lonString = getString(R.string.lon);

        // CENTER CROSS
        setCenterCross();

        // FLOATING BUTTONS
        FloatingActionButton menuButton = findViewById(cu.phibrain.cardinal.app.R.id.menu_map_button);
        menuButton.setOnClickListener(this);
        menuButton.setOnLongClickListener(this);
        registerForContextMenu(menuButton);

        FloatingActionButton layerButton = findViewById(cu.phibrain.cardinal.app.R.id.layers_map_button);
        layerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapTagsIntent = new Intent(MapviewActivity.this, CardinalMapLayerListActivity.class);
                startActivity(mapTagsIntent);
            }
        });


        // register for battery updates
        registerReceiver(batteryReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        double[] mapCenterLocation = PositionUtilities.getMapCenterFromPreferences(mPeferences, true, true);
        // check for screen on
        boolean keepScreenOn = mPeferences.getBoolean(Constants.PREFS_KEY_SCREEN_ON, false);
        if (keepScreenOn) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
        boolean areButtonsVisible = mPeferences.getBoolean(ARE_BUTTONSVISIBLE_OPEN, true);

        /*
         * create main mapview
         */
        try {
            mapView = new CardinalGPMapView(this);
        } catch (Exception ex) {
            GPLog.error(this, null, ex);
            mapView = new CardinalGPMapView(this);
        }

        mapView.setClickable(true);
        mapView.setOnTouchListener(this);

        float mapScaleX = mPeferences.getFloat(MAPSCALE_X, 1f);
        float mapScaleY = mPeferences.getFloat(MAPSCALE_Y, 1f);
        if (mapScaleX > 1 || mapScaleY > 1) {
            mapView.setScaleX(mapScaleX);
            mapView.setScaleY(mapScaleY);
        }

        setTextScale();

        final RelativeLayout rl = findViewById(cu.phibrain.cardinal.app.R.id.innerlayout);
        rl.addView(mapView, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        rl.setVisibility(View.VISIBLE);
        ImageButton zoomInButton = findViewById(cu.phibrain.cardinal.app.R.id.zoomin);
        zoomInButton.setOnClickListener(this);
        zoomInButton.setOnLongClickListener(this);

        zoomLevelText = findViewById(cu.phibrain.cardinal.app.R.id.zoomlevel);
        zoomLevelText.setVisibility(View.VISIBLE);

        ImageButton zoomOutButton = findViewById(cu.phibrain.cardinal.app.R.id.zoomout);
        zoomOutButton.setOnClickListener(this);
        zoomOutButton.setOnLongClickListener(this);

        batteryButton = findViewById(cu.phibrain.cardinal.app.R.id.battery);
        batteryText = findViewById(cu.phibrain.cardinal.app.R.id.batterytext);

        selectMto = findViewById(cu.phibrain.cardinal.app.R.id.selectMto);
        selectMto.setOnClickListener(this);
        selectMto.setOnLongClickListener(this);

        selectMo = findViewById(cu.phibrain.cardinal.app.R.id.selectMo);
        selectMo.setOnClickListener(this);
        selectMo.setOnLongClickListener(this);

        fragmentCenter = findViewById(cu.phibrain.cardinal.app.R.id.frameLayout);
        fragmentCenter.setOnClickListener(this);
        fragmentCenter.setOnLongClickListener(this);

        centerOnGps = findViewById(cu.phibrain.cardinal.app.R.id.center_on_gps_btn);
        centerOnGps.setOnClickListener(this);
        centerOnGps.setOnLongClickListener(this);
        buttom_sheet_background = findViewById(cu.phibrain.cardinal.app.R.id.buttom_sheet_background);
        buttom_sheet_background.setOnClickListener(this);
        buttom_sheet_background.setOnLongClickListener(this);
//        ImageButton addnotebytagButton = findViewById(cu.phibrain.cardinal.app.R.id.addnotebytagbutton);
//        addnotebytagButton.setOnClickListener(this);
//        addnotebytagButton.setOnLongClickListener(this);
//
        ImageButton addBookmarkButton = findViewById(cu.phibrain.cardinal.app.R.id.addbookmarkbutton);
        addBookmarkButton.setOnClickListener(this);
        addBookmarkButton.setOnLongClickListener(this);
//
        final ImageButton toggleMeasuremodeButton = findViewById(cu.phibrain.cardinal.app.R.id.togglemeasuremodebutton);
        toggleMeasuremodeButton.setOnClickListener(this);
        toggleMeasuremodeButton.setOnLongClickListener(this);
//
        final ImageButton toggleLogInfoButton = findViewById(cu.phibrain.cardinal.app.R.id.toggleloginfobutton);
        toggleLogInfoButton.setOnClickListener(this);
        toggleLogInfoButton.setOnLongClickListener(this);

        addRouteSegmentbutton = findViewById(cu.phibrain.cardinal.app.R.id.addroutesegmentbutton);
        addRouteSegmentbutton.setOnClickListener(this);
        addRouteSegmentbutton.setOnLongClickListener(this);
        addRouteSegmentbutton.setVisibility(View.GONE);

        joinButton = findViewById(cu.phibrain.cardinal.app.R.id.jointobutton);
        joinButton.setOnClickListener(this);
        joinButton.setOnLongClickListener(this);
        joinButton.setVisibility(View.GONE);

        toggleEditingButton = findViewById(cu.phibrain.cardinal.app.R.id.toggleEditingButton);
        toggleEditingButton.setOnClickListener(this);

        toggleLabelsButton = findViewById(cu.phibrain.cardinal.app.R.id.toggleLabels);
        toggleLabelsButton.setOnClickListener(this);

        if (mapCenterLocation != null)
            setNewCenterAtZoom(mapCenterLocation[0], mapCenterLocation[1], (int) mapCenterLocation[2]);

        setAllButtoonsEnablement(areButtonsVisible);
        batteryText.setVisibility(areButtonsVisible ? View.VISIBLE : View.INVISIBLE);
        EditingView editingView = findViewById(cu.phibrain.cardinal.app.R.id.editingview);
        LinearLayout editingToolsLayout = findViewById(cu.phibrain.cardinal.app.R.id.editingToolsLayout);
        EditManager.INSTANCE.setEditingView(editingView, editingToolsLayout);
        mapView.setEditingView(editingView);

        GpsServiceUtilities.registerForBroadcasts(this, gpsServiceBroadcastReceiver);
        GpsServiceUtilities.triggerBroadcast(this);


        mapView.addMapUpdateListener(this);

        //Register for storage update
        registerReceiver(storageReceiver, new IntentFilter(Intent.ACTION_DEVICE_STORAGE_LOW));
        registerReceiver(mMessageUiUpdateReceiver, new IntentFilter(MapviewActivity.ACTION_UPDATE_UI));


        //Fit map to session Bounding box
        WorkSession session = appContainer.getWorkSessionActive();
        Zone zone = session.getZoneObj();
        sessionBBox = LatLongUtils.toBoundingBox(zone);
        if (sessionBBox != null) {
            MapPosition mapPosition = mapView.map().getMapPosition();
            mapPosition.setByBoundingBox(sessionBBox, mapView.getViewportWidth(), mapView.getViewportHeight());
            mapPosition.setZoomLevel(17);
            mapView.setMapPosition(new GPMapPosition(mapPosition));
        }


    }

    @Override
    public void onUpdate(GPMapPosition mapPosition) {
        setGuiZoomText(mapPosition.getZoomLevel(), (int) mapView.getScaleX());
        if (currentZoomLevel != mapPosition.getZoomLevel()) {
            currentZoomLevel = mapPosition.getZoomLevel();

            try {
                mapView.reloadLayer(CardinalLineLayer.class);
                mapView.reloadLayer(CardinalPolygonLayer.class);
                mapView.reloadLayer(CardinalEdgesLayer.class);
                mapView.reloadLayer(CardinalSelectPointLayer.class);
                mapView.reloadLayer(CardinalJoinsLayer.class);
                mapView.reloadLayer(BifurcationLayer.class);
                for (Layer layer :
                        LayerOperations.getInstance().getAll()) {
                    if (layer.getEnabled())
                        mapView.reloadLayer(layer.getId());
                }
            } catch (Exception e) {
                e.printStackTrace();
                GPLog.error(this, null, e);
            }

        }

//        if (!sessionBBox.contains(new GeoPoint(mapPosition.getLatitude(), mapPosition.getLongitude())))
//            setNewCenterAtZoom(sessionBBox.getCenterPoint().getLatitude(), sessionBBox.getCenterPoint().getLongitude(), currentZoomLevel);
    }

    /**
     * Returns the relative size of a map view in relation to the screen size of the device. This
     * is used for cache size calculations.
     * By default this returns 1.0, for a full size map view.
     *
     * @return the screen ratio of the mapview
     */
    private float getScreenRatio() {
        return 1f;
    }

    private void setCenterCross() {
        String crossColorStr = mPeferences.getString(Constants.PREFS_KEY_CROSS_COLOR, "red"); //$NON-NLS-1$
        int crossColor = ColorUtilities.toColor(crossColorStr);
        String crossWidthStr = mPeferences.getString(Constants.PREFS_KEY_CROSS_WIDTH, "3"); //$NON-NLS-1$
        int crossThickness = 3;
        try {
            crossThickness = (int) Double.parseDouble(Objects.requireNonNull(crossWidthStr));
        } catch (NumberFormatException e) {
            // ignore and use default
        }
        String crossSizeStr = mPeferences.getString(Constants.PREFS_KEY_CROSS_SIZE, "50"); //$NON-NLS-1$
        int crossLength = 20;
        try {
            crossLength = (int) Double.parseDouble(crossSizeStr);
        } catch (NumberFormatException e) {
            // ignore and use default
        }
        FrameLayout crossHor = findViewById(cu.phibrain.cardinal.app.R.id.centerCrossHorizontal);
        FrameLayout crossVer = findViewById(cu.phibrain.cardinal.app.R.id.centerCrossVertical);
        crossHor.setBackgroundColor(crossColor);
        ViewGroup.LayoutParams layHor = crossHor.getLayoutParams();
        layHor.width = crossLength;
        layHor.height = crossThickness;
        crossVer.setBackgroundColor(crossColor);
        ViewGroup.LayoutParams layVer = crossVer.getLayoutParams();
        layVer.width = crossThickness;
        layVer.height = crossLength;
    }

    @Override
    protected void onPause() {
        if (mapView != null) {
            CardinalLayerManager.INSTANCE.onPause(mapView);
            mapView.onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (mapView != null) {
            mapView.onResume();
            CardinalLayerManager.INSTANCE.onResume(mapView, this);

            GPMapPosition mapPosition = mapView.getMapPosition();
            setNewCenter(mapPosition.getLongitude() + 0.000001, mapPosition.getLatitude() + 0.000001);
        }
        checkLabelButton();

        //disableEditing();


        //Update ui

        MapObject moa = appContainer.getCurrentMapObject();
        setToggleMapObjectTools(moa);
        if (moa != null) {
            updateSelectMapObj(moa.getObjectType());
            GPGeoPoint point = moa.getCentroid();
            setNewCenterAtZoom(point.getLongitude(), point.getLatitude(), moa.getLayer().getEditZoomLevel());
        } else {
            updateSelectMapObj(null);
        }

        MapObjecType mot = appContainer.getMapObjecTypeActive();
        if (mot != null) {
            selectedMto(mot);
        } else {
            selectedMto(null);
        }


        super.onResume();
    }

    private void checkLabelButton() {
        hasLabelledLayers = false;
        List<IGpLayer> layers = mapView.getLayers();
        for (IGpLayer layer : layers) {
            if (layer instanceof ILabeledLayer) {
                hasLabelledLayers = true;
                break;
            }
        }
        if (!hasLabelledLayers) {
            toggleLabelsButton.setVisibility(View.GONE);
        } else {
            toggleLabelsButton.setVisibility(View.VISIBLE);
        }
    }

    private void setTextScale() {
//        String textSizeFactorStr = mPeferences.getString(Constants.PREFS_KEY_MAPSVIEW_TEXTSIZE_FACTOR, "1.0"); //$NON-NLS-1$
//        float textSizeFactor = 1f;
//        try {
//            textSizeFactor = Float.parseFloat(textSizeFactorStr);
//        } catch (NumberFormatException e) {
//            // ignore
//        }
//        if (textSizeFactor < 0.5f) {
//            textSizeFactor = 1f;
//        }
//        mapView.setTextScale(textSizeFactor);
    }

    @Override
    protected void onDestroy() {
        EditManager.INSTANCE.setEditingView(null, null);
        unregisterReceiver(batteryReceiver);
        unregisterReceiver(storageReceiver);
        unregisterReceiver(mMessageUiUpdateReceiver);

        if (mapsSupportBroadcastReceiver != null) {
            unregisterReceiver(mapsSupportBroadcastReceiver);
        }

        if (gpsServiceBroadcastReceiver != null)
            GpsServiceUtilities.unregisterFromBroadcasts(this, gpsServiceBroadcastReceiver);


        try {
            CardinalLayerManager.INSTANCE.dispose(mapView);
        } catch (JSONException e) {
            GPLog.error(this, null, e);
        }
        if (mapView != null) {
            mapView.destroyAll();
        }

        super.onDestroy();
    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if (GPLog.LOG_ABSURD)
            GPLog.addLogEntry(this, "onTouch issued with motionevent: " + action); //$NON-NLS-1$

        GPMapPosition mapPosition = mapView.getMapPosition();
        if (action == MotionEvent.ACTION_MOVE) {
            double lon = mapPosition.getLongitude();
            double lat = mapPosition.getLatitude();
            if (coordView != null) {
                coordView.setText(lonString + " " + COORDINATE_FORMATTER.format(lon) //
                        + "\n" + latString + " " + COORDINATE_FORMATTER.format(lat));
            }
        }
        if (action == MotionEvent.ACTION_UP) {
            if (coordView != null)
                coordView.setText("");
            saveCenterPref();
        }
        return false;
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            double[] lastCenter = PositionUtilities.getMapCenterFromPreferences(preferences, true, true);
            if (lastCenter != null)
                setNewCenterAtZoom(lastCenter[0], lastCenter[1], (int) lastCenter[2]);
        }
        super.onWindowFocusChanged(hasFocus);
    }

    /**
     * Return current Zoom.
     *
     * @return integer current zoom level.
     */
    private int getZoom() {
        GPMapPosition mapPosition = mapView.getMapPosition();
        return (byte) mapPosition.getZoomLevel();
    }

    public void setZoom(int zoom) {
        GPMapPosition mapPosition = mapView.getMapPosition();
        mapPosition.setZoomLevel(zoom);
        mapView.setMapPosition(mapPosition);
        saveCenterPref();
    }

    private void setGuiZoomText(int newZoom, int newScale) {
        String scalePart = "";
        if (newScale > 1)
            scalePart = "*" + newScale;
        String text = formatter.format(newZoom) + scalePart;
        zoomLevelText.setText(text);

    }

    public void setNewCenterAtZoom(double lon, double lat, int zoom) {
        GPMapPosition mapPosition = mapView.getMapPosition();
        mapPosition.setZoomLevel(zoom);
        mapPosition.setPosition(lat, lon);
        mapView.setMapPosition(mapPosition);

        saveCenterPref(lon, lat, zoom);
    }


    public void setNewCenter(double lon, double lat) {
        GPMapPosition mapPosition = mapView.getMapPosition();
        mapPosition.setPosition(lat, lon);
        mapView.setMapPosition(mapPosition);

        saveCenterPref();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(Menu.NONE, MENU_COMPASS_ID, 1, R.string.mapsactivity_menu_toggle_compass);//.setIcon(                android.R.drawable.ic_menu_compass);
        menu.add(Menu.NONE, MENU_GO_TO, 2, R.string.go_to);//.setIcon(android.R.drawable.ic_menu_myplaces);
        menu.add(Menu.NONE, MENU_SHAREPOSITION_ID, 3, R.string.share_position);//.setIcon(android.R.drawable.ic_menu_send);
    }

    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_COMPASS_ID:
                AppsUtilities.checkAndOpenGpsStatus(this);
                return true;
            case MENU_SHAREPOSITION_ID:
                try {
                    if (!NetworkUtilities.isNetworkAvailable(this)) {
                        GPDialogs.infoDialog(this, getString(R.string.available_only_with_network), null);
                    } else {
                        // sendData();
                        ShareUtilities.sharePositionUrl(this);
                    }
                    return true;
                } catch (Exception e1) {
                    GPLog.error(this, null, e1); //$NON-NLS-1$
                    return false;
                }
            case MENU_GO_TO: {
                return goTo();
            }
            default:
        }
        return super.onContextItemSelected(item);
    }

    private boolean goTo() {
        String[] items = new String[]{getString(R.string.goto_coordinate), getString(R.string.geocoding)};
        boolean[] checked = new boolean[2];
        GPDialogs.singleOptionDialog(this, items, checked, () -> runOnUiThread(() -> {
            int selectedPosition = checked[1] ? 1 : 0;
            if (selectedPosition == 0) {
                InsertCoordinatesDialogFragment insertCoordinatesDialogFragment = InsertCoordinatesDialogFragment.newInstance(null);
                insertCoordinatesDialogFragment.show(getSupportFragmentManager(), "Insert Coord"); //NON-NLS
            } else {
                Intent intent = new Intent(MapviewActivity.this, GeocodeActivity.class);
                startActivityForResult(intent, INSERTCOORD_RETURN_CODE);
            }
        }));
        return true;
    }

    /**
     * Retrieves the map world bounds in degrees.
     *
     * @return the [n,s,w,e] in degrees.
     */
    private double[] getMapWorldBounds() {
        GPBBox bbox = mapView.getBoundingBox();
        double[] nswe = {bbox.getMaxLatitude(), bbox.getMinLatitude(), bbox.getMinLongitude(), bbox.getMaxLongitude()};
        return nswe;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (GPLog.LOG_ABSURD)
            GPLog.addLogEntry(this, "Activity returned"); //$NON-NLS-1$
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case (INSERTCOORD_RETURN_CODE): {
                if (resultCode == Activity.RESULT_OK) {

                    float[] routePoints = data.getFloatArrayExtra(ROUTE);
                    if (routePoints != null) {
                        // it is a routing request
                        try {
                            String name = data.getStringExtra(NAME);
                            if (name == null) {
                                name = "ROUTE_" + TimeUtilities.INSTANCE.TIME_FORMATTER_LOCAL.format(new Date()); //$NON-NLS-1$
                            }
                            DaoGpsLog logDumper = new DaoGpsLog();
                            SQLiteDatabase sqliteDatabase = logDumper.getDatabase();
                            long now = new java.util.Date().getTime();
                            long newLogId = logDumper.addGpsLog(now, now, 0, name, DEFAULT_LOG_WIDTH, ColorUtilities.BLUE.getHex(), true); //$NON-NLS-1$

                            sqliteDatabase.beginTransaction();
                            try {
                                long nowPlus10Secs = now;
                                for (int i = 0; i < routePoints.length; i = i + 2) {
                                    double lon = routePoints[i];
                                    double lat = routePoints[i + 1];
                                    double altim = -1;

                                    // dummy time increment
                                    nowPlus10Secs = nowPlus10Secs + 10000;
                                    logDumper.addGpsLogDataPoint(sqliteDatabase, newLogId, lon, lat, altim, nowPlus10Secs);
                                }

                                sqliteDatabase.setTransactionSuccessful();
                            } finally {
                                sqliteDatabase.endTransaction();
                            }
                        } catch (Exception e) {
                            GPLog.error(this, "Cannot draw route.", e); //$NON-NLS-1$
                        }

                    } else {
                        // it is a single point geocoding request
                        double lon = data.getDoubleExtra(LONGITUDE, 0d);
                        double lat = data.getDoubleExtra(LATITUDE, 0d);
                        setCenterAndZoomForMapWindowFocus(lon, lat, null);
                    }
                }
                break;
            }
            case (ZOOM_RETURN_CODE): {
                if (resultCode == Activity.RESULT_OK) {
                    double lon = data.getDoubleExtra(LONGITUDE, 0d);
                    double lat = data.getDoubleExtra(LATITUDE, 0d);
                    int zoom = data.getIntExtra(ZOOMLEVEL, 1);
                    setCenterAndZoomForMapWindowFocus(lon, lat, zoom);
                }
                break;
            }
//            case (NotesLayer.FORMUPDATE_RETURN_CODE): {
//                if (resultCode == Activity.RESULT_OK) {
//                    FormInfoHolder formInfoHolder = (FormInfoHolder) data.getSerializableExtra(FormInfoHolder.BUNDLE_KEY_INFOHOLDER);
//                    if (formInfoHolder != null) {
//                        try {
//                            long noteId = formInfoHolder.noteId;
//                            String nameStr = formInfoHolder.renderingLabel;
//                            String jsonStr = formInfoHolder.sectionObjectString;
//
//                            DaoNotes.updateForm(noteId, nameStr, jsonStr);
//                        } catch (Exception e) {
//                            GPLog.error(this, null, e);
//                            GPDialogs.warningDialog(this, getString(eu.geopaparazzi.library.R.string.notenonsaved), null);
//                        }
//                    }
//                }
//                break;
//            }
            case (MapUtilities.SELECTED_FEATURES_UPDATED_RETURN_CODE):
                if (resultCode == Activity.RESULT_OK) {
                    ToolGroup activeToolGroup = EditManager.INSTANCE.getActiveToolGroup();
                    if (activeToolGroup != null) {
                        if (activeToolGroup instanceof OnSelectionToolGroup) {
                            Bundle extras = data.getExtras();
                            ArrayList<Feature> featuresList = extras.getParcelableArrayList(FeatureUtilities.KEY_FEATURESLIST);
                            OnSelectionToolGroup selectionGroup = (OnSelectionToolGroup) activeToolGroup;
                            selectionGroup.setSelectedFeatures(featuresList);
                        }
                    }
                }
                break;
        }
    }

    private void addBookmark() {
        GPMapPosition mapPosition = mapView.getMapPosition();
        final double centerLat = mapPosition.getLatitude();
        final double centerLon = mapPosition.getLongitude();

        final String newDate = TimeUtilities.INSTANCE.TIME_FORMATTER_LOCAL.format(new Date());
        final String proposedName = "bookmark " + newDate;//NON-NLS

        String message = getString(R.string.mapsactivity_enter_bookmark_name);
        GPDialogs.inputMessageDialog(this, message, proposedName, new TextRunnable() {
            @Override
            public void run() {
                try {
                    if (theTextToRunOn.length() < 1) {
                        theTextToRunOn = proposedName;
                    }
                    int zoom = mapPosition.getZoomLevel();
                    DaoBookmarks.addBookmark(centerLon, centerLat, theTextToRunOn, zoom);
                    mapView.reloadLayer(BookmarkLayer.class);
                } catch (Exception e) {
                    GPLog.error(this, e.getLocalizedMessage(), e);
                    Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private boolean boundsContain(int latE6, int lonE6, int nE6, int sE6, int wE6, int eE6) {
        return lonE6 > wE6 && lonE6 < eE6 && latE6 > sE6 && latE6 < nE6;
    }

    /**
     * Save the current mapview position to preferences.
     *
     * @param lonLatZoom optional position + zoom. If null, position is taken from the mapview.
     */
    private synchronized void saveCenterPref(double... lonLatZoom) {
        double lon;
        double lat;
        int zoom;
        if (lonLatZoom != null && lonLatZoom.length == 3) {
            lon = lonLatZoom[0];
            lat = lonLatZoom[1];
            zoom = (int) lonLatZoom[2];
        } else {
            GPMapPosition mapPosition = mapView.getMapPosition();
            lat = mapPosition.getLatitude();
            lon = mapPosition.getLongitude();
            zoom = mapPosition.getZoomLevel();
        }

        if (GPLog.LOG_ABSURD) {
            StringBuilder sb = new StringBuilder();
            sb.append("Map Center moved: "); //$NON-NLS-1$
            sb.append(lon);
            sb.append("/"); //$NON-NLS-1$
            sb.append(lat);
            GPLog.addLogEntry(this, sb.toString());
        }

        PositionUtilities.putMapCenterInPreferences(mPeferences, lon, lat, zoom);

        EditManager.INSTANCE.invalidateEditingView();
    }

    /**
     * Set center coords and zoom ready for the {@link eu.geopaparazzi.core.mapview.MapviewActivity} to focus again.
     * <p/>
     * <p>In {@link eu.geopaparazzi.core.mapview.MapviewActivity} the {@link eu.geopaparazzi.core.mapview.MapviewActivity#onWindowFocusChanged(boolean)}
     * will take care to zoom properly.
     *
     * @param centerX the lon coordinate. Can be <code>null</code>.
     * @param centerY the lat coordinate. Can be <code>null</code>.
     * @param zoom    the zoom. Can be <code>null</code>.
     */
    public void setCenterAndZoomForMapWindowFocus(Double centerX, Double centerY, Integer zoom) {
        GPMapPosition mapPosition = mapView.getMapPosition();

        int zoomLevel = mapPosition.getZoomLevel();
        double cx = 0f;
        double cy = 0f;
        if (centerX != null) {
            cx = centerX.floatValue();
        } else {
            cx = mapPosition.getLongitude();
        }
        if (centerY != null) {
            cy = centerY.floatValue();
        } else {
            cy = mapPosition.getLatitude();
        }
        if (zoom != null) {
            zoomLevel = zoom;
        }
        PositionUtilities.putMapCenterInPreferences(mPeferences, cx, cy, zoomLevel);
    }

    private void updateBatteryCondition(int level) {
        if (GPLog.LOG_ABSURD)
            GPLog.addLogEntry(this, "BATTERY LEVEL GEOPAP: " + level); //$NON-NLS-1$
        StringBuilder sb = new StringBuilder();
        sb.append(level);
        if (level < 100) {
            sb.append("%"); //$NON-NLS-1$
        }
        batteryText.setText(sb.toString());
    }

    private void onGpsServiceUpdate(Intent intent) {
        GpsServiceStatus lastGpsServiceStatus = GpsServiceUtilities.getGpsServiceStatus(intent);
        GpsLoggingStatus lastGpsLoggingStatus = GpsServiceUtilities.getGpsLoggingStatus(intent);
        lastGpsPosition = GpsServiceUtilities.getPosition(intent);

        if (lastGpsServiceStatus == GpsServiceStatus.GPS_OFF) {
            centerOnGps.setImageDrawable(Compat.getDrawable(this, eu.geopaparazzi.core.R.drawable.ic_mapview_center_gps_red_24dp));
            if (lastGpsPosition != null && appContainer.getWorkSessionActive() != null) {
                long currentTime = GpsServiceUtilities.getPositionTime(intent);
                SignalEventLogger.addEventLogEntry(SignalEvents.SignalTypes.GPS, appContainer.getWorkSessionActive().getId(), 0, currentTime > 0 ? new Date(currentTime) : new Date(), lastGpsPosition);
            }

        } else {
            if (lastGpsLoggingStatus == GpsLoggingStatus.GPS_DATABASELOGGING_ON) {
                centerOnGps.setImageDrawable(Compat.getDrawable(this, eu.geopaparazzi.core.R.drawable.ic_mapview_center_gps_blue_24dp));
            } else {
                if (lastGpsServiceStatus == GpsServiceStatus.GPS_FIX) {
                    centerOnGps.setImageDrawable(Compat.getDrawable(this, eu.geopaparazzi.core.R.drawable.ic_mapview_center_gps_green_24dp));
                } else {
                    centerOnGps.setImageDrawable(Compat.getDrawable(this, eu.geopaparazzi.core.R.drawable.ic_mapview_center_gps_orange_24dp));
                }
            }
        }
        if (lastGpsPosition == null) {
            return;
        }

        float[] lastGpsPositionExtras = GpsServiceUtilities.getPositionExtras(intent);
        int[] lastGpsStatusExtras = GpsServiceUtilities.getGpsStatusExtras(intent);

        mapView.setGpsStatus(lastGpsServiceStatus, lastGpsPosition, lastGpsPositionExtras, lastGpsStatusExtras, lastGpsLoggingStatus);


        if (mapView.getViewportWidth() <= 0 || mapView.getViewportWidth() <= 0) {
            return;
        }
        try {
            double lat = lastGpsPosition[1];
            double lon = lastGpsPosition[0];

            // send updates to the editing framework
            EditManager.INSTANCE.onGpsUpdate(lon, lat);


        } catch (Exception e) {
            GPLog.error(this, "On location change error", e); //$NON-NLS-1$
        }
    }

    public boolean onLongClick(View v) {
        int i = v.getId();
//        if (i == cu.phibrain.cardinal.app.R.id.addnotebytagbutton) {
//            Intent intent = new Intent(MapviewActivity.this, NotesListActivity.class);
//            intent.putExtra(LibraryConstants.PREFS_KEY_MAP_ZOOM, true);
//            startActivityForResult(intent, ZOOM_RETURN_CODE);
//        } else

        if (i == cu.phibrain.cardinal.app.R.id.toggleloginfobutton) {
            Intent gpsDatalistIntent = new Intent(this, GpsDataListActivity.class);
            startActivity(gpsDatalistIntent);
        } else if (i == cu.phibrain.cardinal.app.R.id.addbookmarkbutton) {
            Intent bookmarksListIntent = new Intent(MapviewActivity.this, BookmarksListActivity.class);
            startActivityForResult(bookmarksListIntent, ZOOM_RETURN_CODE);
        } else if (i == cu.phibrain.cardinal.app.R.id.menu_map_button) {
//            boolean areButtonsVisible = mPeferences.getBoolean(ARE_BUTTONSVISIBLE_OPEN, false);
//           setAllButtoonsEnablement(!areButtonsVisible);
//            batteryText.setVisibility(!areButtonsVisible ? View.VISIBLE : View.INVISIBLE);
//            Editor edit = mPeferences.edit();
//            edit.putBoolean(ARE_BUTTONSVISIBLE_OPEN, !areButtonsVisible);
//            edit.apply();
            return true;
        } else if (i == cu.phibrain.cardinal.app.R.id.zoomin) {
            float scaleX1 = mapView.getScaleX() * 2;
            float scaleY1 = mapView.getScaleY() * 2;
            mapView.setScaleX(scaleX1);
            mapView.setScaleY(scaleY1);
            Editor edit1 = mPeferences.edit();
            edit1.putFloat(MAPSCALE_X, scaleX1);
            edit1.putFloat(MAPSCALE_Y, scaleY1);
            edit1.apply();
            return true;
        } else if (i == cu.phibrain.cardinal.app.R.id.zoomout) {
            float scaleX2 = mapView.getScaleX();
            float scaleY2 = mapView.getScaleY();
            if (scaleX2 > 1 && scaleY2 > 1) {
                scaleX2 = scaleX2 / 2;
                scaleY2 = scaleY2 / 2;
                mapView.setScaleX(scaleX2);
                mapView.setScaleY(scaleY2);
            }

            Editor edit2 = mPeferences.edit();
            edit2.putFloat(MAPSCALE_X, scaleX2);
            edit2.putFloat(MAPSCALE_Y, scaleY2);
            edit2.apply();
            return true;
        } else if (i == cu.phibrain.cardinal.app.R.id.center_on_gps_btn) {
            Context context = getContext();
            String[] items = new String[]{context.getString(R.string.option_center_on_gps), context.getString(R.string.option_rotate_with_bearing), context.getString(R.string.option_show_gps_info), context.getString(R.string.option_hide_gps_accuracy)};
            boolean[] checkedItems = new boolean[items.length];
            checkedItems[0] = mPeferences.getBoolean(LibraryConstants.PREFS_KEY_AUTOMATIC_CENTER_GPS, false);
            checkedItems[1] = mPeferences.getBoolean(LibraryConstants.PREFS_KEY_ROTATE_MAP_WITH_GPS, false);
            checkedItems[2] = mPeferences.getBoolean(LibraryConstants.PREFS_KEY_SHOW_GPS_INFO, false);
            checkedItems[3] = mPeferences.getBoolean(LibraryConstants.PREFS_KEY_IGNORE_GPS_ACCURACY, false);

            DialogInterface.OnMultiChoiceClickListener dialogListener = (dialog, which, isChecked) -> {
                checkedItems[which] = isChecked;

                if (which == 0) {
                    // check center on gps
                    boolean centerOnGps = checkedItems[0];
                    Editor edit = mPeferences.edit();
                    edit.putBoolean(LibraryConstants.PREFS_KEY_AUTOMATIC_CENTER_GPS, centerOnGps);
                    edit.apply();
                } else if (which == 1) {
                    // check rotate map
                    boolean rotateMapWithGps = checkedItems[1];
                    Editor edit = mPeferences.edit();
                    edit.putBoolean(LibraryConstants.PREFS_KEY_ROTATE_MAP_WITH_GPS, rotateMapWithGps);
                    edit.apply();
                } else if (which == 2) {
                    // check show info
                    boolean showGpsInfo = checkedItems[2];
                    Editor edit = mPeferences.edit();
                    edit.putBoolean(LibraryConstants.PREFS_KEY_SHOW_GPS_INFO, showGpsInfo);
                    edit.apply();

                    mapView.toggleLocationTextLayer(showGpsInfo);
                } else {
                    // check ignore gps
                    boolean ignoreAccuracy = checkedItems[3];
                    Editor edit = mPeferences.edit();
                    edit.putBoolean(LibraryConstants.PREFS_KEY_IGNORE_GPS_ACCURACY, ignoreAccuracy);
                    edit.apply();
                }
            };
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("");
            builder.setMultiChoiceItems(items, checkedItems, dialogListener);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else if (i == cu.phibrain.cardinal.app.R.id.frameLayout) {
            //temporal test, setando a null el map object activo en mapa
            return true;

        } else if (i == cu.phibrain.cardinal.app.R.id.buttom_sheet_background) {

            Intent intent = new Intent(getContext(), SessionsStatsActivity.class);
            startActivity(intent);
            return true;

        } else if (i == cu.phibrain.cardinal.app.R.id.selectMto) {
            if (appContainer.getMapObjecTypeActive() != null)
                toggleEditing();
            return true;
        } else if (i == cu.phibrain.cardinal.app.R.id.selectMo) {
            MapObject mapObject = appContainer.getCurrentMapObject();
            if (mapObject != null) {
                GPGeoPoint point = mapObject.getCentroid();
                setNewCenterAtZoom(point.getLongitude(), point.getLatitude(), mapObject.getLayer().getViewZoomLevel());

            }
            return true;

        } else if (i == cu.phibrain.cardinal.app.R.id.addroutesegmentbutton) {
            MapObject currentMO = appContainer.getCurrentMapObject();
            //preguntar si tienen que ser topologico el mo
            if (currentMO != null && currentMO.belongToTopoLayer() && !currentMO.isTerminal()) {
                currentMO.setNodeGrade(currentMO.getNodeGrade() + 1);
                MapObjectOperations.getInstance().save(currentMO);
                try {
                    mapView.reloadLayer(BifurcationLayer.class);
                } catch (Exception e) {
                    e.printStackTrace();
                    GPLog.error(this, null, e);
                }

                GPDialogs.quickInfo(mapView, getString(R.string.inspector_object_grade) + ": " + currentMO.getNodeGrade());
            }
            return true;
        } else if (i == cu.phibrain.cardinal.app.R.id.jointobutton) { // lista de mapobjects acoplados al mapobject selccionado

            Intent intent = new Intent(getContext(), MapObjectJoinedActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    public void onClick(View v) {
        boolean isInNonClickableMode = !mapView.isClickable();
        ImageButton toggleLoginfoButton = findViewById(cu.phibrain.cardinal.app.R.id.toggleloginfobutton);
        ImageButton toggleMeasuremodeButton = findViewById(cu.phibrain.cardinal.app.R.id.togglemeasuremodebutton);
//        ImageButton toggleViewingconeButton;
        int i = v.getId();
        if (i == cu.phibrain.cardinal.app.R.id.menu_map_button) {
            FloatingActionButton menuButton = findViewById(cu.phibrain.cardinal.app.R.id.menu_map_button);
            openContextMenu(menuButton);

        } else if (i == cu.phibrain.cardinal.app.R.id.zoomin) {
            int currentZoom = getZoom();
            int newZoom = currentZoom + 1;
            int maxZoom = 24;
            if (newZoom > maxZoom) newZoom = maxZoom;
            setZoom(newZoom);
            Tool activeTool = EditManager.INSTANCE.getActiveTool();
            if (activeTool instanceof MapTool) {
                ((MapTool) activeTool).onViewChanged();
            }

        } else if (i == cu.phibrain.cardinal.app.R.id.zoomout) {
            int newZoom;
            int currentZoom;
            currentZoom = getZoom();
            newZoom = currentZoom - 1;
            int minZoom = 0;
            if (newZoom < minZoom) newZoom = minZoom;
            setZoom(newZoom);
            Tool activeTool1 = EditManager.INSTANCE.getActiveTool();
            if (activeTool1 instanceof MapTool) {
                ((MapTool) activeTool1).onViewChanged();
            }

        } else if (i == cu.phibrain.cardinal.app.R.id.center_on_gps_btn) {
            if (lastGpsPosition != null) {
                setNewCenter(lastGpsPosition[0], lastGpsPosition[1]);
            }

        } else if (i == cu.phibrain.cardinal.app.R.id.addbookmarkbutton) {
            addBookmark();
        } else if (i == cu.phibrain.cardinal.app.R.id.togglemeasuremodebutton) {
            if (!isInNonClickableMode) {
                toggleMeasuremodeButton.setImageDrawable(Compat.getDrawable(this, eu.geopaparazzi.core.R.drawable.ic_mapview_measuremode_on_24dp));
                toggleLoginfoButton.setImageDrawable(Compat.getDrawable(this, eu.geopaparazzi.core.R.drawable.ic_mapview_loginfo_off_24dp));
                toggleLabelsButton.setImageDrawable(Compat.getDrawable(this, eu.geopaparazzi.core.R.drawable.ic_mapview_toggle_labels_off_24dp));
                TapMeasureTool measureTool = new TapMeasureTool(mapView);

                EditManager.INSTANCE.setActiveTool(measureTool);
            } else {
                toggleMeasuremodeButton.setImageDrawable(Compat.getDrawable(this, eu.geopaparazzi.core.R.drawable.ic_mapview_measuremode_off_24dp));
                EditManager.INSTANCE.setActiveTool(null);
            }
        } else if (i == cu.phibrain.cardinal.app.R.id.toggleloginfobutton) {
            if (!isInNonClickableMode) {
                toggleLoginfoButton.setImageDrawable(Compat.getDrawable(this, eu.geopaparazzi.core.R.drawable.ic_mapview_loginfo_on_24dp));
                toggleMeasuremodeButton.setImageDrawable(Compat.getDrawable(this, eu.geopaparazzi.core.R.drawable.ic_mapview_measuremode_off_24dp));
                toggleLabelsButton.setImageDrawable(Compat.getDrawable(this, eu.geopaparazzi.core.R.drawable.ic_mapview_toggle_labels_off_24dp));
                try {
                    GpsLogInfoTool measureTool = new GpsLogInfoTool(mapView);
                    EditManager.INSTANCE.setActiveTool(measureTool);
                } catch (Exception e) {
                    GPLog.error(this, null, e);
                }
                mapView.blockMap();
            } else {
                toggleLoginfoButton.setImageDrawable(Compat.getDrawable(this, eu.geopaparazzi.core.R.drawable.ic_mapview_loginfo_off_24dp));
                EditManager.INSTANCE.setActiveTool(null);
                mapView.releaseMapBlock();
            }
        } else if (i == R.id.toggleEditingButton) {
            toggleEditing();
        } else if (i == cu.phibrain.cardinal.app.R.id.toggleLabels) {
            Tool activeTool = EditManager.INSTANCE.getActiveTool();
            if (activeTool instanceof PanLabelsTool) {
                toggleLabelsButton.setImageDrawable(Compat.getDrawable(this, eu.geopaparazzi.core.R.drawable.ic_mapview_toggle_labels_off_24dp));
                EditManager.INSTANCE.setActiveTool(null);
                mapView.releaseMapBlock();
            } else {
                toggleLabelsButton.setImageDrawable(Compat.getDrawable(this, eu.geopaparazzi.core.R.drawable.ic_mapview_toggle_labels_on_24dp));
                toggleMeasuremodeButton.setImageDrawable(Compat.getDrawable(this, eu.geopaparazzi.core.R.drawable.ic_mapview_measuremode_off_24dp));
                toggleLoginfoButton.setImageDrawable(Compat.getDrawable(this, eu.geopaparazzi.core.R.drawable.ic_mapview_loginfo_off_24dp));

                PanLabelsTool panLabelsTool = new PanLabelsTool(mapView);
                EditManager.INSTANCE.setActiveTool(panLabelsTool);
                mapView.blockMap();
            }
        } else if (i == cu.phibrain.cardinal.app.R.id.buttom_sheet_background) {
            appContainer.setRouteSegmentActive(null);
            onMenuMTO();

        } else if (i == cu.phibrain.cardinal.app.R.id.selectMto) {
            //Evento del Mot Selcecionado
            if (appContainer.getMapObjecTypeActive() != null) {
                appContainer.setCurrentMapObject(null);
                appContainer.setMapObjecTypeActive(null);
                appContainer.setRouteSegmentActive(null);
                try {
                    mapView.reloadLayer(CardinalSelectPointLayer.class);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                disableEditing();
                //Update ui
                Intent intent = new Intent(MapviewActivity.ACTION_UPDATE_UI);
                intent.putExtra("update_map_object_active", true);
                intent.putExtra("update_map_object_type_active", true);
                sendBroadcast(intent);

                GPDialogs.toast(this, getString(R.string.reset_route), Toast.LENGTH_SHORT);
            }

        } else if (i == cu.phibrain.cardinal.app.R.id.selectMo) {
            MapObject mapObject = appContainer.getCurrentMapObject();
            if (mapObject != null) {
                GPGeoPoint point = mapObject.getCentroid();
                setNewCenterAtZoom(point.getLongitude(), point.getLatitude(), mapObject.getLayer().getEditZoomLevel());

                ObjectInspectorDialogFragment.newInstance(mapView, mapObject.getId()).show(
                        this.getSupportFragmentManager(),
                        "dialog"
                );
            }

        } else if (i == cu.phibrain.cardinal.app.R.id.addroutesegmentbutton) {
            MapObject mapObject = appContainer.getCurrentMapObject();

            if (mapObject != null && appContainer.IsCurrentActiveLayerTopological() && !mapObject.isTerminal()) {
                // toggle button
                if (appContainer.getMode() == UserMode.OBJECT_ADDING_EDGE)
                    appContainer.setMode(UserMode.NONE);
                else {
                    appContainer.setMode(UserMode.OBJECT_ADDING_EDGE);
                }

                if (appContainer.getMode() == UserMode.OBJECT_ADDING_EDGE) {
                    addRouteSegmentbutton.setImageDrawable(Compat.getDrawable(this, R.drawable.ic_create_route_segment_line_active_24dp));
                    joinButton.setImageDrawable(Compat.getDrawable(this, R.drawable.ic_link_object_24dp));

                } else {
                    addRouteSegmentbutton.setImageDrawable(Compat.getDrawable(this, R.drawable.ic_create_route_segment_line_24dp));
                }

                toggleEditing();
            }
        } else if (i == cu.phibrain.cardinal.app.R.id.jointobutton) {

            MapObject mapObject = appContainer.getCurrentMapObject();

            if (mapObject != null) {
                if (appContainer.getMode() == UserMode.OBJECT_DOCK) {
                    appContainer.setMode(UserMode.NONE);
                } else {
                    appContainer.setMode(UserMode.OBJECT_DOCK);
                }

                if (appContainer.getMode() == UserMode.OBJECT_DOCK) {
                    joinButton.setImageDrawable(Compat.getDrawable(this, R.drawable.ic_link_object_active_24dp));
                    addRouteSegmentbutton.setImageDrawable(Compat.getDrawable(this, R.drawable.ic_create_route_segment_line_24dp));
                } else {
                    joinButton.setImageDrawable(Compat.getDrawable(this, R.drawable.ic_link_object_24dp));
                }

                toggleEditing();
            }
        }
    }

    private void editByGeometry() {
        ToolGroup activeToolGroup = EditManager.INSTANCE.getActiveToolGroup();

        IEditableLayer editLayer = EditManager.INSTANCE.getEditLayer();
        if (editLayer == null) {
            // if not layer is
            activeToolGroup = new NoEditableLayerToolGroup(mapView);
        } else if (editLayer.getGeometryType().isPolygon())
            activeToolGroup = new cu.phibrain.cardinal.app.ui.map.tools.CardinalPolygonMainEditingToolGroup(mapView, MapviewActivity.this);
        else if (editLayer.getGeometryType().isLine())
            activeToolGroup = new cu.phibrain.cardinal.app.ui.map.tools.CardinalLineMainEditingToolGroup(mapView, MapviewActivity.this);
        else if (editLayer.getGeometryType().isPoint())
            activeToolGroup = new cu.phibrain.cardinal.app.ui.map.tools.CardinalPointMainEditingToolGroup(mapView, MapviewActivity.this);

        EditManager.INSTANCE.setActiveToolGroup(activeToolGroup);
        setLeftButtoonsEnablement(false);

        mapView.blockMap();
    }


    public void onMenuMTO() {

        View bottomSheetView = LayoutInflater.from(MapviewActivity.this.getApplicationContext())
                .inflate(
                        cu.phibrain.cardinal.app.R.layout.layout_bottom_sheet,
                        (LinearLayout) findViewById(cu.phibrain.cardinal.app.R.id.bottomSheetContainer)
                );

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                MapviewActivity.this,
                cu.phibrain.cardinal.app.R.style.BottomSheetDialogTheme
        );
        this.descriptorMto = bottomSheetView.findViewById(cu.phibrain.cardinal.app.R.id.descriptorMto);

        //filter Networks
        try {
            List<Networks> networks = MapviewActivity.this.appContainer.getProjectActive().getNetworks();

            NetworkAdapter networksAdapter = new NetworkAdapter(MapviewActivity.this, cu.phibrain.cardinal.app.R.layout.spinner, networks);
            this.filterNetworks = bottomSheetView.findViewById(cu.phibrain.cardinal.app.R.id.spinnerNetworks);
            this.filterNetworks.setAdapter(networksAdapter);

            //Recivler View Menu Mto
            RecyclerView recyclerView = bottomSheetView.findViewById(cu.phibrain.cardinal.app.R.id.rvMto);

            LinearLayoutManager horizontalLayoutManager
                    = new LinearLayoutManager(bottomSheetView.getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(horizontalLayoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(bottomSheetView.getContext(), DividerItemDecoration.VERTICAL));
            //update Network Select
            this.appContainer.setNetworksActive(((Networks) this.filterNetworks.getSelectedItem()));
            List<MapObjecType> mtoList;
            if (this.appContainer.getRouteSegmentActive() == null) {
                if (this.appContainer.getCurrentMapObject() == null || appContainer.getMode() == UserMode.OBJECT_EDITION) {
                    //Muestro todos por capas
                    mtoList = NetworksOperations.getInstance().getMapObjectTypes((Networks) this.filterNetworks.getSelectedItem());
                } else {
                    //Muestro solo los aptos segun reglas topologicas
                    mtoList = MapObjecTypeOperations.getInstance().topologicalMtoFirewall(this.appContainer.getCurrentMapObject().getObjectType(), null);
                }
            } else {
                mtoList = NetworksOperations.getInstance().getMapObjectTypes((Networks) this.filterNetworks.getSelectedItem(), MapObjecType.GeomType.POLYLINE);
            }

            MtoAdapter mtoAdapter = new MtoAdapter(mtoList, MapviewActivity.this);
            recyclerView.setAdapter(mtoAdapter);
            bottomSheetDialog.setContentView(bottomSheetView);

            if (this.appContainer.getCurrentMapObject() == null || this.appContainer.getMode() == UserMode.OBJECT_EDITION) {
                filterNetworks.setVisibility(View.VISIBLE);
                filterNetworks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        //update Network Select
                        MapviewActivity.this.appContainer.setNetworksActive(networks.get(position));
                        MapviewActivity.this.descriptorMto.setText("");
                        mtoAdapter.getFilter().filter(MapviewActivity.this.appContainer.getNetworksActive().getId().toString());

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // your code here
                    }

                });
            } else {
                MapviewActivity.this.filterNetworks.setVisibility(View.GONE);
            }
            bottomSheetDialog.show();
        } catch (Exception e) {
            GPLog.error(MapviewActivity.this, null, e);
            e.printStackTrace();
        }
    }

    private void updateSelectMapObj(MapObjecType mto) {
        if (mto != null) {
            byte[] icon = mto.getIconAsByteArray();
            if (icon != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(icon, 0, icon.length);
                MapviewActivity.this.selectMo.setImageBitmap(Bitmap.createScaledBitmap(bmp, 48,
                        48, false));
            } else {
                Bitmap bitmap = ImageUtil.getBitmap(getContext(), R.drawable.ic_mapview_mot_parent_24dp);
                MapviewActivity.this.selectMo.setImageBitmap(bitmap);
            }
        } else {
            Bitmap bitmap = ImageUtil.getBitmap(getContext(), R.drawable.ic_mapview_mot_parent_24dp);
            MapviewActivity.this.selectMo.setImageBitmap(bitmap);
        }
        MapviewActivity.this.appContainer.setMode(UserMode.NONE);
        MapviewActivity.this.addRouteSegmentbutton.setImageDrawable(Compat.getDrawable(this, R.drawable.ic_create_route_segment_line_24dp));
        try {
            MapviewActivity.this.mapView.reloadLayer(CardinalSelectPointLayer.class);
            MapviewActivity.this.mapView.reloadLayer(BifurcationLayer.class);
        } catch (Exception e) {
            GPLog.error(MapviewActivity.this, null, e);
            e.printStackTrace();
        }
    }

    private void toggleEditing() {
        ToolGroup activeToolGroup = EditManager.INSTANCE.getActiveToolGroup();
        boolean isEditing = activeToolGroup != null;

        checkLabelButton();

        if (isEditing) {
            disableEditing();
            MapviewActivity.this.mapView.releaseMapBlock();
        } else if (this.appContainer.getMode() != UserMode.OBJECT_DOCK && this.appContainer.getMode() != UserMode.OBJECT_ADDING_EDGE) {
            editByGeometry();
        }

    }

    private void disableEditing() {
        Tool activeTool = EditManager.INSTANCE.getActiveTool();
        if (activeTool != null) {
            activeTool.disable();
            EditManager.INSTANCE.setActiveTool(null);
        }
        ToolGroup activeToolGroup = EditManager.INSTANCE.getActiveToolGroup();
        if (activeToolGroup != null) {
            activeToolGroup.disable();
            EditManager.INSTANCE.setActiveToolGroup(null);
        }
        setLeftButtoonsEnablement(true);
    }

    private void setLeftButtoonsEnablement(boolean enable) {
//        ImageButton addnotebytagButton = findViewById(cu.phibrain.cardinal.app.R.id.addnotebytagbutton);

        ImageButton addBookmarkButton = findViewById(cu.phibrain.cardinal.app.R.id.addbookmarkbutton);
        ImageButton toggleLoginfoButton = findViewById(cu.phibrain.cardinal.app.R.id.toggleloginfobutton);
        ImageButton toggleMeasuremodeButton = findViewById(cu.phibrain.cardinal.app.R.id.togglemeasuremodebutton);
        if (enable) {
//            addnotebytagButton.setVisibility(View.VISIBLE);
            addBookmarkButton.setVisibility(View.VISIBLE);
            toggleLoginfoButton.setVisibility(View.VISIBLE);
            toggleMeasuremodeButton.setVisibility(View.VISIBLE);
        } else {
//            addnotebytagButton.setVisibility(View.GONE);
            addBookmarkButton.setVisibility(View.GONE);
            toggleLoginfoButton.setVisibility(View.GONE);
            toggleMeasuremodeButton.setVisibility(View.GONE);

            MapObject mapObject = appContainer.getCurrentMapObject();

            joinButton = findViewById(cu.phibrain.cardinal.app.R.id.jointobutton);
            addRouteSegmentbutton = findViewById(cu.phibrain.cardinal.app.R.id.addroutesegmentbutton);
            if (mapObject != null) {
                if (appContainer.IsCurrentActiveLayerTopological() && !mapObject.getIsCompleted() && !mapObject.getObjectType().getIsTerminal()) {
                    if (appContainer.getMode() == UserMode.OBJECT_ADDING_EDGE) {
                        addRouteSegmentbutton.setImageDrawable(Compat.getDrawable(this, R.drawable.ic_create_route_segment_line_active_24dp));

                    } else {
                        addRouteSegmentbutton.setImageDrawable(Compat.getDrawable(this, R.drawable.ic_create_route_segment_line_24dp));
                    }
                    addRouteSegmentbutton.setVisibility(View.VISIBLE);
                } else {
                    addRouteSegmentbutton.setVisibility(View.GONE);
                }

                if (appContainer.getMode() == UserMode.OBJECT_DOCK) {
                    joinButton.setImageDrawable(Compat.getDrawable(this, R.drawable.ic_link_object_active_24dp));
                } else {
                    joinButton.setImageDrawable(Compat.getDrawable(this, R.drawable.ic_link_object_24dp));
                }
                joinButton.setVisibility(View.VISIBLE);
            } else {
                addRouteSegmentbutton.setVisibility(View.GONE);
                joinButton.setVisibility(View.GONE);
            }
        }
    }

    private void setAllButtoonsEnablement(boolean enable) {
//      ImageButton addnotebytagButton = findViewById(R.id.addnotebytagbutton);
        ImageButton addBookmarkButton = findViewById(cu.phibrain.cardinal.app.R.id.addbookmarkbutton);
        ImageButton toggleLoginfoButton = findViewById(cu.phibrain.cardinal.app.R.id.toggleloginfobutton);
        ImageButton toggleMeasuremodeButton = findViewById(cu.phibrain.cardinal.app.R.id.togglemeasuremodebutton);
        ImageButton zoomInButton = findViewById(cu.phibrain.cardinal.app.R.id.zoomin);
        TextView zoomLevelTextview = findViewById(cu.phibrain.cardinal.app.R.id.zoomlevel);
        ImageButton zoomOutButton = findViewById(cu.phibrain.cardinal.app.R.id.zoomout);
        ImageButton toggleEditingButton = findViewById(cu.phibrain.cardinal.app.R.id.toggleEditingButton);

        int visibility = View.VISIBLE;
        if (!enable) {
            visibility = View.GONE;
        }
//      addnotebytagButton.setVisibility(visibility);
        addBookmarkButton.setVisibility(visibility);
        toggleLoginfoButton.setVisibility(visibility);
        toggleMeasuremodeButton.setVisibility(visibility);
        batteryButton.setVisibility(visibility);
        centerOnGps.setVisibility(visibility);
        zoomInButton.setVisibility(visibility);
        zoomLevelTextview.setVisibility(visibility);
        zoomOutButton.setVisibility(visibility);
        toggleEditingButton.setVisibility(View.GONE);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && EditManager.INSTANCE.getActiveToolGroup() != null) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onCoordinateInserted(double lon, double lat) {
        setNewCenter(lon, lat);
    }


    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void selectedMto(MapObjecType _mtoModel) {

        ToolGroup activeToolGroup = EditManager.INSTANCE.getActiveToolGroup();
        boolean isEditing = activeToolGroup != null;

        checkLabelButton();

        if (isEditing && appContainer.getMode() == UserMode.NONE) {
            disableEditing();
            mapView.releaseMapBlock();
        }


        appContainer.setMapObjecTypeActive(_mtoModel);
        if (appContainer.getMapObjecTypeActive() != null) {

            if (descriptorMto != null) { // Evitar exception cuando se invoca este método en un lugar distinto del buttonsheet

                String objectType = _mtoModel.getCaption();
                if(_mtoModel.getIsTerminal()){
                    objectType = String.format("%s - (Terminal)", objectType);
                }
                descriptorMto.setText(objectType);
            }

            byte[] icon = _mtoModel.getIconAsByteArray();
            if (icon != null) {
                selectMto.setImageBitmap(
                        ImageUtil.getScaledBitmap(ImageUtilities.getImageFromImageData(icon),
                                48,
                                48, false));
            } else {
                Bitmap bitmap = ImageUtil.getBitmap(getContext(), R.drawable.ic_mapview_mot_parent_24dp);
                selectMto.setImageBitmap(bitmap);
            }
            Layer editLayer = _mtoModel.getLayerObj();
            switch (_mtoModel.getGeomType()) {
                case POLYLINE:
                    EditManager.INSTANCE.setEditLayer(((CardinalLineLayer) mapView.getLayer(CardinalLineLayer.class)));
                    break;
                case POLYGON:
                    EditManager.INSTANCE.setEditLayer(((CardinalPolygonLayer) mapView.getLayer(CardinalPolygonLayer.class)));
                    break;
                default:
                    EditManager.INSTANCE.setEditLayer(((CardinalPointLayer) mapView.getLayer(CardinalPointLayer.class, editLayer.getId())));
                    break;
            }

            setZoom(editLayer.getEditZoomLevel());
            editByGeometry();
        } else {
            Bitmap bitmap = ImageUtil.getBitmap(getContext(), R.drawable.ic_mapview_mot_parent_24dp);
            selectMto.setImageBitmap(bitmap);
        }

        if (appContainer.getRouteSegmentActive() != null && _mtoModel != null && _mtoModel.getGeomType() == MapObjecType.GeomType.POLYLINE) {
            AppCompatActivity activity = this;
            List<GPGeoPoint> points = new ArrayList<>();
            MapObject origin = appContainer.getRouteSegmentActive().getOriginObj();
            MapObject destiny = appContainer.getRouteSegmentActive().getDestinyObj();
            points.add(origin.getCentroid());
            points.add(destiny.getCentroid());
            BarcodeReaderDialogFragment.newInstance(
                    mapView, points, 0
            ).show(
                    activity.getSupportFragmentManager(),
                    "dialog"
            );

        }

    }

    private void setToggleMapObjectTools(MapObject mapObject) {

        joinButton = findViewById(cu.phibrain.cardinal.app.R.id.jointobutton);
        addRouteSegmentbutton = findViewById(cu.phibrain.cardinal.app.R.id.addroutesegmentbutton);
        if (mapObject != null) {

            if (appContainer.IsCurrentActiveLayerTopological() && !mapObject.getIsCompleted() && !mapObject.getObjectType().getIsTerminal()) {
                addRouteSegmentbutton.setImageDrawable(Compat.getDrawable(this, R.drawable.ic_create_route_segment_line_24dp));
                addRouteSegmentbutton.setVisibility(View.VISIBLE);
            } else
                addRouteSegmentbutton.setVisibility(View.GONE);

            joinButton.setImageDrawable(Compat.getDrawable(this, R.drawable.ic_link_object_24dp));
            joinButton.setVisibility(View.VISIBLE);
        } else {
            addRouteSegmentbutton.setVisibility(View.GONE);
            joinButton.setVisibility(View.GONE);
        }

    }

}


