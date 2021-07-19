package cu.phibrain.cardinal.app.ui.layer;

import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.oscim.layers.Layer;
import org.oscim.layers.tile.buildings.BuildingLayer;
import org.oscim.layers.tile.vector.labeling.LabelLayer;
import org.oscim.map.Layers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import cu.phibrain.plugins.cardinal.io.database.entity.operations.LayerOperations;
import eu.geopaparazzi.library.GPApplication;
import eu.geopaparazzi.library.core.ResourcesManager;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.profiles.Profile;
import eu.geopaparazzi.library.profiles.ProfilesHandler;
import eu.geopaparazzi.library.profiles.objects.ProfileBasemaps;
import eu.geopaparazzi.library.profiles.objects.ProfileSpatialitemaps;
import eu.geopaparazzi.library.util.FileUtilities;
import eu.geopaparazzi.library.util.IActivitySupporter;
import eu.geopaparazzi.map.GPMapThemes;
import eu.geopaparazzi.map.GPMapView;
import eu.geopaparazzi.map.features.editing.EditManager;
import eu.geopaparazzi.map.layers.ELayerTypes;
import eu.geopaparazzi.map.layers.LayerManager;
import eu.geopaparazzi.map.layers.interfaces.IEditableLayer;
import eu.geopaparazzi.map.layers.interfaces.IGpLayer;
import eu.geopaparazzi.map.layers.interfaces.ISystemLayer;
import eu.geopaparazzi.map.layers.interfaces.IVectorDbLayer;
import eu.geopaparazzi.map.layers.interfaces.IVectorTileOfflineLayer;
import eu.geopaparazzi.map.layers.interfaces.IVectorTileOnlineLayer;
import eu.geopaparazzi.map.layers.systemlayers.BookmarkLayer;
import eu.geopaparazzi.map.layers.systemlayers.CurrentGpsLogLayer;
import eu.geopaparazzi.map.layers.systemlayers.GPMapScaleBarLayer;
import eu.geopaparazzi.map.layers.systemlayers.GpsLogsLayer;
import eu.geopaparazzi.map.layers.systemlayers.GpsPositionLayer;
import eu.geopaparazzi.map.layers.systemlayers.GpsPositionTextLayer;
import eu.geopaparazzi.map.layers.userlayers.BitmapTileServiceLayer;
import eu.geopaparazzi.map.layers.userlayers.GeopackageTableLayer;
import eu.geopaparazzi.map.layers.userlayers.GeopackageTilesLayer;
import eu.geopaparazzi.map.layers.userlayers.MBTilesLayer;
import eu.geopaparazzi.map.layers.userlayers.MapsforgeLayer;
import eu.geopaparazzi.map.layers.userlayers.SpatialiteTableLayer;
import eu.geopaparazzi.map.layers.userlayers.VectorTilesServiceLayer;
import eu.geopaparazzi.map.layers.utils.EOnlineTileSources;
import eu.geopaparazzi.map.utils.MapUtilities;


@SuppressWarnings("ALL")
public enum CardinalLayerManager {
    INSTANCE;

    public static final String GP_LOADED_CARDINAL_KEY = "GP_LOADED_CARDINAL_KEY_2";
    private List<JSONObject> userLayersDefinitions = new ArrayList<>();
    private List<JSONObject> systemLayersDefinitions = new ArrayList<>();
    private List<JSONObject> cardinalLayersDefinitions = new ArrayList<>();

    /**
     * Initialize the layers from preferences
     */
    public void init() throws Exception {
        GPApplication context = GPApplication.getInstance();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String loadedUserMapsJson = preferences.getString(LayerManager.GP_LOADED_USERMAPS_KEY, "{}");


        JSONObject root = new JSONObject(loadedUserMapsJson);
        if (root.has(LayerManager.LAYERS)) {
            JSONArray layersArray = root.getJSONArray(LayerManager.LAYERS);
            int length = layersArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = layersArray.getJSONObject(i);
                userLayersDefinitions.add(jsonObject);
            }
        }

        String loadedSystemMapsJson = preferences.getString(LayerManager.GP_LOADED_SYSTEMMAPS_KEY, "{}");
        root = new JSONObject(loadedSystemMapsJson);
        if (root.has(LayerManager.LAYERS)) {
            JSONArray layersArray = root.getJSONArray(LayerManager.LAYERS);
            int length = layersArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonObject = layersArray.getJSONObject(i);
                systemLayersDefinitions.add(jsonObject);
            }
        } else {
            // define system layers
            JSONObject jo = new JSONObject();
            jo.put(IGpLayer.LAYERTYPE_TAG, GpsLogsLayer.class.getCanonicalName());
            jo.put(IGpLayer.LAYERNAME_TAG, GpsLogsLayer.getName(context));
            jo.put(IGpLayer.LAYERENABLED_TAG, true);
            systemLayersDefinitions.add(jo);

            jo = new JSONObject();
            jo.put(IGpLayer.LAYERTYPE_TAG, CurrentGpsLogLayer.class.getCanonicalName());
            jo.put(IGpLayer.LAYERNAME_TAG, CurrentGpsLogLayer.getName(context));
            jo.put(IGpLayer.LAYERENABLED_TAG, true);
            systemLayersDefinitions.add(jo);

            jo = new JSONObject();
            jo.put(IGpLayer.LAYERTYPE_TAG, BookmarkLayer.class.getCanonicalName());
            jo.put(IGpLayer.LAYERNAME_TAG, BookmarkLayer.getName(context));
            jo.put(IGpLayer.LAYERENABLED_TAG, true);
            systemLayersDefinitions.add(jo);
//
//            jo = new JSONObject();
//            jo.put(IGpLayer.LAYERTYPE_TAG, ImagesLayer.class.getCanonicalName());
//            jo.put(IGpLayer.LAYERNAME_TAG, ImagesLayer.getName(context));
//            jo.put(IGpLayer.LAYERENABLED_TAG, true);
//            systemLayersDefinitions.add(jo);
//
//            jo = new JSONObject();
//            jo.put(IGpLayer.LAYERTYPE_TAG, NotesLayer.class.getCanonicalName());
//            jo.put(IGpLayer.LAYERNAME_TAG, NotesLayer.getName(context));
//            jo.put(IGpLayer.LAYERENABLED_TAG, true);
//            systemLayersDefinitions.add(jo);

            jo = new JSONObject();
            jo.put(IGpLayer.LAYERTYPE_TAG, GpsPositionLayer.class.getCanonicalName());
            jo.put(IGpLayer.LAYERNAME_TAG, GpsPositionLayer.getName(context));
            jo.put(IGpLayer.LAYERENABLED_TAG, true);
            systemLayersDefinitions.add(jo);

            jo = new JSONObject();
            jo.put(IGpLayer.LAYERTYPE_TAG, GpsPositionTextLayer.class.getCanonicalName());
            jo.put(IGpLayer.LAYERNAME_TAG, GpsPositionTextLayer.getName(context));
            jo.put(IGpLayer.LAYERENABLED_TAG, false);
            systemLayersDefinitions.add(jo);

            jo = new JSONObject();
            jo.put(IGpLayer.LAYERTYPE_TAG, GPMapScaleBarLayer.class.getCanonicalName());
            jo.put(IGpLayer.LAYERNAME_TAG, GPMapScaleBarLayer.getName(context));
            jo.put(IGpLayer.LAYERENABLED_TAG, true);
            systemLayersDefinitions.add(jo);
        }



//        //Init Layer Cardinal
          List<cu.phibrain.plugins.cardinal.io.database.entity.model.Layer> cardinalLayers = LayerOperations.getInstance().getAll();
          for (cu.phibrain.plugins.cardinal.io.database.entity.model.Layer layerIndex: cardinalLayers) {
                    JSONObject jo = new JSONObject();
                    jo.put(IGpLayer.LAYERTYPE_TAG, CardinalPointLayer.class.getCanonicalName());
                    jo.put(IGpLayer.LAYERNAME_TAG, layerIndex.getName());
                    jo.put(ICardinalLayer.LAYERID_TAG, layerIndex.getId()); // Daniel no harcodee texto en el codigo man
                    jo.put(IGpLayer.LAYERENABLED_TAG, layerIndex.getEnabled());
                    cardinalLayersDefinitions.add(jo);
                }

            //Capa de segmento de rutas
            JSONObject jo = new JSONObject();
            jo.put(IGpLayer.LAYERTYPE_TAG, EdgesLayer.class.getCanonicalName());
            jo.put(IGpLayer.LAYERNAME_TAG, EdgesLayer.getName(context));
            jo.put(IGpLayer.LAYERENABLED_TAG, true);
            cardinalLayersDefinitions.add(jo);

            jo = new JSONObject();
            jo.put(IGpLayer.LAYERTYPE_TAG, CardinalLineLayer.class.getCanonicalName());
            jo.put(IGpLayer.LAYERNAME_TAG, CardinalLineLayer.getName(context));
            jo.put(IGpLayer.LAYERENABLED_TAG, true);
            cardinalLayersDefinitions.add(jo);

            jo = new JSONObject();
            jo.put(IGpLayer.LAYERTYPE_TAG, CardinalPolygonLayer.class.getCanonicalName());
            jo.put(IGpLayer.LAYERNAME_TAG, CardinalPolygonLayer.getName(context));
            jo.put(IGpLayer.LAYERENABLED_TAG, true);
            cardinalLayersDefinitions.add(jo);

       }

    public void createGroups(CardinalGPMapView mapView) {
        Layers layers = mapView.map().layers();
        //Add Group Cardinal Layers
        layers.addGroup(CardinalLayerGroups.GROUP_CARDINALLAYERS.getGroupId());
    }

    public List<JSONObject> getUserLayersDefinitions() {
        return userLayersDefinitions;
    }

    public List<JSONObject> getSystemLayersDefinitions() {
        return systemLayersDefinitions;
    }

    public List<JSONObject> getCardinalLayersDefinitions() {
        return cardinalLayersDefinitions;
    }

    /**
     * Load all the layers in the map.
     *
     * @param mapView           the map view.
     * @param activitySupporter
     * @throws JSONException
     */
    public void loadInMap(GPMapView mapView, IActivitySupporter activitySupporter) throws Exception {
        //--  Remove all the layers From Map:
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            mapView.map().layers().removeIf(layer -> layer instanceof IGpLayer || layer instanceof BuildingLayer || layer instanceof LabelLayer);
        } else {
            // TODO remove when minsdk gets 24
            Layers layers = mapView.map().layers();
            Iterator<Layer> iterator = layers.iterator();
            while (iterator.hasNext()) {
                Layer layer = iterator.next();
                if (layer instanceof IGpLayer || layer instanceof BuildingLayer || layer instanceof LabelLayer) {
                    iterator.remove();
                }
            }
        }

        if (ProfilesHandler.INSTANCE.ProfileChanged) {
            ProfilesHandler.INSTANCE.ProfileChanged = false;  // reset it

            // Remove all the layers From LayerManager List:
            userLayersDefinitions.clear();

            //-- Add all the layers from the profile --
            Profile activeProfile = ProfilesHandler.INSTANCE.getActiveProfile();
            if (activeProfile != null) {
                GPApplication gpApplication = GPApplication.getInstance();
                ResourcesManager resourcesManager = ResourcesManager.getInstance(gpApplication);
                File sdcardDir = resourcesManager.getMainStorageDir();
                for (ProfileBasemaps currentBasemap : activeProfile.basemapsList) {
                    String filePath = currentBasemap.getRelativePath();
                    File basemapFile = new File(sdcardDir, filePath);
                    ELayerTypes layerType = ELayerTypes.fromFileExt(basemapFile.getName());
                    if (layerType == ELayerTypes.MAPURL) {
                        int index = addMapurlBitmapTileService(basemapFile, null);
                    } else {
                        int index = addMapFile(basemapFile, null);  // adds to local list (and map?)
                    }
                }
                List<ProfileSpatialitemaps> spatialiteList = activeProfile.spatialiteList;
                for (ProfileSpatialitemaps currentSpatialite : spatialiteList) {
                    String filePath = currentSpatialite.getRelativePath();
                    File spatialite = new File(sdcardDir, filePath);
                    if (currentSpatialite.visibleLayerNames != null) {
                        for (String tableName : currentSpatialite.visibleLayerNames) {
                            int index = addSpatialiteTable(spatialite, tableName, null);
                        }
                    }
                }
            }
        }

        if (userLayersDefinitions.size() == 0) {
            EOnlineTileSources source = EOnlineTileSources.Open_Street_Map_Standard;
            CardinalLayerManager.INSTANCE.addBitmapTileService(source.getName(), source.getUrl(), source.getTilePath(), source.getMaxZoom(), 1f, null);
        }
        //
        loadMapLayers(mapView, userLayersDefinitions);
        //-- Add system layers --
        if (systemLayersDefinitions.size() > 0) {
            for (JSONObject layerDefinition : systemLayersDefinitions) {
                String layerClass = layerDefinition.getString(IGpLayer.LAYERTYPE_TAG);
                boolean isEnabled = true;
                boolean hasEnabled = layerDefinition.has(IGpLayer.LAYERENABLED_TAG);
                if (hasEnabled)
                    isEnabled = layerDefinition.getBoolean(IGpLayer.LAYERENABLED_TAG);

                if (layerClass.equals(GpsLogsLayer.class.getCanonicalName())) {
                    GpsLogsLayer sysLayer = new GpsLogsLayer(mapView);
                    sysLayer.load();
                    sysLayer.setEnabled(isEnabled);
                } else if (layerClass.equals(CurrentGpsLogLayer.class.getCanonicalName())) {
                    CurrentGpsLogLayer sysLayer = new CurrentGpsLogLayer(mapView);
                    sysLayer.load();
                    sysLayer.setEnabled(isEnabled);
                } else if (layerClass.equals(BookmarkLayer.class.getCanonicalName())) {
                    BookmarkLayer sysLayer = new BookmarkLayer(mapView);
                    sysLayer.load();
                    sysLayer.setEnabled(isEnabled);
                } /*else if (layerClass.equals(ImagesLayer.class.getCanonicalName())) {
                    ImagesLayer sysLayer = new ImagesLayer(mapView);
                    sysLayer.load();
                    sysLayer.setEnabled(isEnabled);
                } else if (layerClass.equals(NotesLayer.class.getCanonicalName())) {
                    NotesLayer sysLayer = new NotesLayer(mapView, activitySupporter);
                    sysLayer.load();
                    sysLayer.setEnabled(isEnabled);
                }*/ else if (layerClass.equals(GpsPositionLayer.class.getCanonicalName())) {
                    GpsPositionLayer sysLayer = new GpsPositionLayer(mapView);
                    sysLayer.load();
                    sysLayer.setEnabled(isEnabled);
                } else if (layerClass.equals(GpsPositionTextLayer.class.getCanonicalName())) {
                    GpsPositionTextLayer sysLayer = new GpsPositionTextLayer(mapView);
                    sysLayer.load();
                    sysLayer.setEnabled(isEnabled);
                } else if (layerClass.equals(GPMapScaleBarLayer.class.getCanonicalName())) {
                    GPMapScaleBarLayer sysLayer = new GPMapScaleBarLayer(mapView);
                    sysLayer.load();
                    sysLayer.setEnabled(isEnabled);
                }
            }
        } else {
            loadSystemLayers(mapView, activitySupporter, systemLayersDefinitions);
        }


        //Cardinal Load Layers
        if (cardinalLayersDefinitions.size() > 0) {
            for (JSONObject layerDefinition : cardinalLayersDefinitions) {
                String layerClass = layerDefinition.getString(IGpLayer.LAYERTYPE_TAG);

                boolean isEnabled = true;
                boolean hasEnabled = layerDefinition.has(IGpLayer.LAYERENABLED_TAG);
                if (hasEnabled)
                    isEnabled = layerDefinition.getBoolean(IGpLayer.LAYERENABLED_TAG);

                if (layerClass.equals(CardinalPointLayer.class.getCanonicalName())) {

                    Long Id = layerDefinition.getLong(ICardinalLayer.LAYERID_TAG);
                    cu.phibrain.plugins.cardinal.io.database.entity.model.Layer layer = LayerOperations.getInstance().load(Id);
                    CardinalPointLayer sysLayer = new CardinalPointLayer(mapView, activitySupporter, Id);
                    sysLayer.load();
                    sysLayer.setEnabled(layer.getIsActive());
                }
                else{
                    if (layerClass.equals(EdgesLayer.class.getCanonicalName())) {
                        EdgesLayer sysLayer = new EdgesLayer(mapView, activitySupporter);
                        sysLayer.load();
                        sysLayer.setEnabled(isEnabled);
                    }
                    else if(layerClass.equals(CardinalLineLayer.class.getCanonicalName())){
                        CardinalLineLayer sysLayer = new CardinalLineLayer(mapView, activitySupporter);
                        sysLayer.load();
                        sysLayer.setEnabled(isEnabled);
                    }
                    else if(layerClass.equals(CardinalPolygonLayer.class.getCanonicalName())){
                        CardinalPolygonLayer sysLayer = new CardinalPolygonLayer(mapView, activitySupporter);
                        sysLayer.load();
                        sysLayer.setEnabled(isEnabled);
                    }

                }
            }
        }
        else{
            loadCardinalLayers(mapView, activitySupporter, cardinalLayersDefinitions);
        }
    }

    private void loadCardinalLayers(GPMapView mapView, IActivitySupporter activitySupporter, List<JSONObject> cardinalLayersDefinitions) throws JSONException, IOException {
        List<cu.phibrain.plugins.cardinal.io.database.entity.model.Layer> cardinalLayers = LayerOperations.getInstance().getAll();
        for (cu.phibrain.plugins.cardinal.io.database.entity.model.Layer layerIndex: cardinalLayers) {
                JSONObject jo = new JSONObject();
                jo.put(IGpLayer.LAYERTYPE_TAG, CardinalPointLayer.class.getCanonicalName());
                jo.put(IGpLayer.LAYERNAME_TAG, layerIndex.getName());

                jo.put(IGpLayer.LAYERENABLED_TAG, layerIndex.getEnabled());

                jo.put(ICardinalLayer.LAYERID_TAG, layerIndex.getId());
                cardinalLayersDefinitions.add(jo);
                //Load layer
                CardinalPointLayer cardinalPointLayer = new CardinalPointLayer(mapView, activitySupporter, layerIndex.getId());
                cardinalPointLayer.setID(layerIndex.getId());
                cardinalPointLayer.load();
        }

        EdgesLayer edgeLayer = new EdgesLayer(mapView, activitySupporter);
        GPApplication context = GPApplication.getInstance();
        JSONObject jo = new JSONObject();
        jo.put(IGpLayer.LAYERTYPE_TAG, EdgesLayer.class.getCanonicalName());
        jo.put(IGpLayer.LAYERNAME_TAG, EdgesLayer.getName(context));
        cardinalLayersDefinitions.add(edgeLayer.toJson());
        edgeLayer.load();

        CardinalLineLayer lineLayer = new CardinalLineLayer(mapView, activitySupporter);
        jo = new JSONObject();
        jo.put(IGpLayer.LAYERTYPE_TAG, CardinalLineLayer.class.getCanonicalName());
        jo.put(IGpLayer.LAYERNAME_TAG, lineLayer.getName(context));
        cardinalLayersDefinitions.add(lineLayer.toJson());
        lineLayer.load();

        CardinalPolygonLayer polygonLayer = new CardinalPolygonLayer(mapView, activitySupporter);
        jo = new JSONObject();
        jo.put(IGpLayer.LAYERTYPE_TAG, CardinalPolygonLayer.class.getCanonicalName());
        jo.put(IGpLayer.LAYERNAME_TAG, polygonLayer.getName(context));
        cardinalLayersDefinitions.add(polygonLayer.toJson());
        polygonLayer.load();
    }

    public void loadMapLayers(GPMapView mapView, List<JSONObject> mapLayersDefinitions) throws Exception {
        EditManager.INSTANCE.setEditLayer(null);
        for (JSONObject layerDefinition : mapLayersDefinitions) {
            try {
                String layerClass = layerDefinition.getString(IGpLayer.LAYERTYPE_TAG);
                String name = layerDefinition.getString(IGpLayer.LAYERNAME_TAG);
                boolean isEnabled = true;
                boolean hasEnabled = layerDefinition.has(IGpLayer.LAYERENABLED_TAG);
                if (hasEnabled) {
                    isEnabled = layerDefinition.getBoolean(IGpLayer.LAYERENABLED_TAG);
                    if (!isEnabled) {
                        continue;
                    }
                }

                ELayerTypes layerType = ELayerTypes.fromType(layerClass);
                if (layerType != null) {
                    switch (layerType) {
                        case MAPSFORGE: {
                            String mapPath = layerDefinition.getString(IGpLayer.LAYERPATH_TAG);
                            String[] mapPaths = mapPath.split(IGpLayer.PATHS_DELIMITER);
                            boolean doLabels = true;
                            if (layerDefinition.has(IGpLayer.LAYERDOLABELS_TAG))
                                doLabels = layerDefinition.getBoolean(IGpLayer.LAYERDOLABELS_TAG);
                            boolean do3d = true;
                            if (layerDefinition.has(IGpLayer.LAYERDO3D_TAG))
                                do3d = layerDefinition.getBoolean(IGpLayer.LAYERDO3D_TAG);

                            MapsforgeLayer mapsforgeLayer = new MapsforgeLayer(mapView, name, do3d, doLabels, mapPaths);
                            mapsforgeLayer.load();
                            mapsforgeLayer.setEnabled(isEnabled);
                            break;
                        }
                        case MBTILES: {
                            String path = layerDefinition.getString(IGpLayer.LAYERPATH_TAG);
                            float alpha = 1f;
                            if (layerDefinition.has(IGpLayer.LAYERALPHA_TAG))
                                alpha = (float) layerDefinition.getDouble(IGpLayer.LAYERALPHA_TAG);
                            MBTilesLayer mbtilesLayer = new MBTilesLayer(mapView, path, alpha, null);
                            mbtilesLayer.load();
                            mbtilesLayer.setEnabled(isEnabled);
                            break;
                        }
                        case VECTORTILESSERVICE: {
                            String tilePath = layerDefinition.getString(IGpLayer.LAYERPATH_TAG);
                            String url = layerDefinition.getString(IGpLayer.LAYERURL_TAG);
                            VectorTilesServiceLayer vtsLayer = new VectorTilesServiceLayer(mapView, name, url, tilePath);
                            vtsLayer.load();
                            vtsLayer.setEnabled(isEnabled);
                            break;
                        }
                        case BITMAPTILESERVICE: {
                            String tilePath = layerDefinition.getString(IGpLayer.LAYERPATH_TAG);
                            String url = layerDefinition.getString(IGpLayer.LAYERURL_TAG);
                            int maxZoom = 19;
                            if (layerDefinition.has(IGpLayer.LAYERMAXZOOM_TAG))
                                maxZoom = layerDefinition.getInt(IGpLayer.LAYERMAXZOOM_TAG);
                            float alpha = 1f;
                            if (layerDefinition.has(IGpLayer.LAYERALPHA_TAG))
                                alpha = (float) layerDefinition.getDouble(IGpLayer.LAYERALPHA_TAG);
                            BitmapTileServiceLayer bitmapLayer = new BitmapTileServiceLayer(mapView, name, url, tilePath, maxZoom, alpha);
                            bitmapLayer.load();
                            bitmapLayer.setEnabled(isEnabled);
                            break;
                        }
                        case SPATIALITE: {
                            String dbPath = layerDefinition.getString(IGpLayer.LAYERPATH_TAG);

                            boolean isEditing = false;
                            if (layerDefinition.has(IGpLayer.LAYEREDITING_TAG))
                                isEditing = layerDefinition.getBoolean(IGpLayer.LAYEREDITING_TAG);

                            SpatialiteTableLayer spatialiteLayer = new SpatialiteTableLayer(mapView, dbPath, name, isEditing);
                            spatialiteLayer.load();
                            spatialiteLayer.setEnabled(isEnabled);
                            if (isEditing) {
                                EditManager.INSTANCE.setEditLayer(spatialiteLayer);
                            }
                            break;
                        }
                        case GEOPACKAGE: {
                            String dbPath = layerDefinition.getString(IGpLayer.LAYERPATH_TAG);

                            if (layerClass.equals(ELayerTypes.GEOPACKAGE.getVectorType())) {
                                boolean isEditing = false;
                                if (layerDefinition.has(IGpLayer.LAYEREDITING_TAG))
                                    isEditing = layerDefinition.getBoolean(IGpLayer.LAYEREDITING_TAG);

                                GeopackageTableLayer spatialiteLayer = new GeopackageTableLayer(mapView, dbPath, name, isEditing);
                                spatialiteLayer.load();
                                spatialiteLayer.setEnabled(isEnabled);
                                if (isEditing) {
                                    EditManager.INSTANCE.setEditLayer(spatialiteLayer);
                                }
                            } else {
                                float alpha = 1f;
                                if (layerDefinition.has(IGpLayer.LAYERALPHA_TAG))
                                    alpha = (float) layerDefinition.getDouble(IGpLayer.LAYERALPHA_TAG);
                                GeopackageTilesLayer geopackageTilesLayer = new GeopackageTilesLayer(mapView, dbPath, name, alpha, null);
                                geopackageTilesLayer.load();
                                geopackageTilesLayer.setEnabled(isEnabled);
                            }
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                GPLog.error(this, "Unable to load layer: " + layerDefinition.toString(2), e);
            }
        }

    }

    public void loadSystemLayers(GPMapView mapView, IActivitySupporter activitySupporter, List<JSONObject> systemLayersDefinitions) throws Exception {
        GpsLogsLayer gpsLogsLayer = new GpsLogsLayer(mapView);
        gpsLogsLayer.load();
        systemLayersDefinitions.add(gpsLogsLayer.toJson());

        CurrentGpsLogLayer currentGpsLogLayer = new CurrentGpsLogLayer(mapView);
        currentGpsLogLayer.load();
        systemLayersDefinitions.add(currentGpsLogLayer.toJson());

        BookmarkLayer bookmarkLayer = new BookmarkLayer(mapView);
        bookmarkLayer.load();
        systemLayersDefinitions.add(bookmarkLayer.toJson());
//
//        ImagesLayer imagesLayer = new ImagesLayer(mapView);
//        imagesLayer.load();
//        systemLayersDefinitions.add(imagesLayer.toJson());
//
//        NotesLayer notesLayer = new NotesLayer(mapView, activitySupporter);
//        notesLayer.load();
//        systemLayersDefinitions.add(notesLayer.toJson());

        GpsPositionLayer gpsPositionLayer = new GpsPositionLayer(mapView);
        gpsPositionLayer.load();
        systemLayersDefinitions.add(gpsPositionLayer.toJson());

        GpsPositionTextLayer gpsPositionTextLayer = new GpsPositionTextLayer(mapView);
        gpsPositionTextLayer.load();
        systemLayersDefinitions.add(gpsPositionTextLayer.toJson());
    }


//    public void updateFromMap(GPMapView mapView) throws JSONException {
//        userLayersDefinitions.clear();
//        Layers layers = mapView.map().layers();
//
//
//        int size = layers.size();
//        for (int i = 0; i < size; i++) {
//            Layer layer = layers.get(i);
//            if (layer instanceof IGpLayer && !(layer instanceof ISystemLayer)) {
//                IGpLayer iPersistenceLayer = (IGpLayer) layer;
//                JSONObject jsonObject = iPersistenceLayer.toJson();
//                userLayersDefinitions.add(jsonObject);
//            }
//        }
//
//    }

    /**
     * Dispose all the layers and save the state to preferences.
     */
    public void dispose(GPMapView mapView) throws JSONException {
        if (mapView != null) {
            JSONArray usersLayersArray = new JSONArray();
            JSONObject usersRoot = new JSONObject();
            usersRoot.put(LayerManager.LAYERS, usersLayersArray);

            JSONArray systemLayersArray = new JSONArray();
            JSONObject systemRoot = new JSONObject();
            systemRoot.put(LayerManager.LAYERS, systemLayersArray);

            JSONArray cardinalLayersArray = new JSONArray();
            JSONObject cardinalRoot = new JSONObject();
            cardinalRoot.put(LayerManager.LAYERS, cardinalLayersArray);
            GPApplication context = GPApplication.getInstance();
            for (Layer layer : mapView.map().layers()) {
                if (layer instanceof IGpLayer) {
                    if (layer instanceof ISystemLayer) {

                        if(layer instanceof  ICardinalLayer){
                            IGpLayer gpLayer = (IGpLayer) layer;
                            cu.phibrain.plugins.cardinal.io.database.entity.model.Layer layerIndex = LayerOperations.getInstance().load(((CardinalPointLayer)layer).getID());
                            //Danil creo que aquihay un problema, no deberias salvar el estado hacia la bd porque si recargas se pierde el estado de la capa
                            //Si hay  que salvarlo porque el fitro en menu juega con es estado en que se encuentra dentro del adapter de capa, si el item de la capa no esta marcado esos mto no se ven en menu,
                            //lo mismo cuando aumentas el zoom y se deban de pintar su topologia
                            //El estado no se pierde porque lo mando a cargar siempre de la BD, quedar'a como lo dejamos
                            layerIndex.setEnabled(layer.isEnabled());
                            LayerOperations.getInstance().save(layerIndex);
//                            JSONObject jo = new JSONObject();
//                            jo.put(IGpLayer.LAYERTYPE_TAG, CardinalLayer.class.getCanonicalName());
//                            jo.put(IGpLayer.LAYERNAME_TAG, layerIndex.getName());
//                            jo.put("ID", layerIndex.getId());
//                            jo.put(IGpLayer.LAYERENABLED_TAG, layer.isEnabled());
//                            cardinalLayersArray.put(jo);
                            gpLayer.dispose();
                        }

                        else if(layer instanceof  ICardinalEdge){

                            IGpLayer gpLayer = (IGpLayer) layer;
                            JSONObject jo = new JSONObject();
                            jo.put(IGpLayer.LAYERTYPE_TAG, EdgesLayer.class.getCanonicalName());
                            jo.put(IGpLayer.LAYERNAME_TAG, EdgesLayer.getName(context));
                            jo.put(IGpLayer.LAYERENABLED_TAG, layer.isEnabled());
                            cardinalLayersArray.put(jo);
                            gpLayer.dispose();
                        }else if(layer instanceof  ICardinalLine){
                            IGpLayer gpLayer = (IGpLayer) layer;
                            JSONObject jo = new JSONObject();
                            jo.put(IGpLayer.LAYERTYPE_TAG, CardinalLineLayer.class.getCanonicalName());
                            jo.put(IGpLayer.LAYERNAME_TAG, CardinalLineLayer.getName(context));
                            jo.put(IGpLayer.LAYERENABLED_TAG, layer.isEnabled());
                            cardinalLayersArray.put(jo);
                            gpLayer.dispose();
                        }else if(layer instanceof  ICardinalPolygon){
                            IGpLayer gpLayer = (IGpLayer) layer;
                            JSONObject jo = new JSONObject();
                            jo.put(IGpLayer.LAYERTYPE_TAG, CardinalPolygonLayer.class.getCanonicalName());
                            jo.put(IGpLayer.LAYERNAME_TAG, CardinalPolygonLayer.getName(context));
                            jo.put(IGpLayer.LAYERENABLED_TAG, layer.isEnabled());
                            cardinalLayersArray.put(jo);
                            gpLayer.dispose();
                        }
                        else{
                            IGpLayer gpLayer = (IGpLayer) layer;
                            JSONObject jsonObject = gpLayer.toJson();
                            systemLayersArray.put(jsonObject);
                            gpLayer.dispose();
                        }
                    } else {
                        IGpLayer gpLayer = (IGpLayer) layer;
                        JSONObject jsonObject = gpLayer.toJson();
                        usersLayersArray.put(jsonObject);
                        gpLayer.dispose();
                    }
                }
            }

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(GPApplication.getInstance());

            SharedPreferences.Editor editor = preferences.edit();

            String jsonString = usersRoot.toString();
            editor.putString(LayerManager.GP_LOADED_USERMAPS_KEY, jsonString);

            jsonString = systemRoot.toString();
            editor.putString(LayerManager.GP_LOADED_SYSTEMMAPS_KEY, jsonString);

            jsonString = cardinalRoot.toString();
            editor.putString(GP_LOADED_CARDINAL_KEY, jsonString);
            editor.apply();
        }
    }


    /**
     * Remove the current layer from the map view.
     *
     * @return the position the layer had or -1 if the layer could not be found.
     */
    public int removeFromMap(GPMapView mapView, String id) {
        Layers layers = mapView.map().layers();
        int index = 0;
        for (Layer layer : layers) {
            if (layer instanceof IGpLayer) {
                IGpLayer gpLayer = (IGpLayer) layer;
                if (gpLayer.getId().equals(id)) {
                    layers.remove(index);
                    return index;
                }
            }
            index++;
        }
        return -1;
    }

//    public void swapLayers(GPMapView mapView, int oldIndex, int newIndex) {
//        Layers layers = mapView.map().layers();
//        Layer removed = layers.remove(oldIndex);
//        layers.add(newIndex, removed);
//    }


    public void onResume(GPMapView mapView, IActivitySupporter activitySupporter) {
        if (mapView != null) {
            Layers layers = mapView.map().layers();
            // right now we dispose and reload
            try {
                loadInMap(mapView, activitySupporter);
            } catch (Exception e) {
                GPLog.error(this, null, e);
            }
            for (Layer layer : layers) {
                if (layer instanceof IGpLayer) {
                    IGpLayer gpLayer = (IGpLayer) layer;
                    gpLayer.onResume();
                }
            }
            //ToDo Debug
            int count = 0;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                count = (int) layers.stream().filter(l -> l instanceof IVectorTileOfflineLayer || l instanceof IVectorTileOnlineLayer).count();
            } else {
                // TODO remove when minsdk is 24
                for (Layer l : layers) {
                    if (l instanceof IVectorTileOfflineLayer || l instanceof IVectorTileOnlineLayer) {
                        count++;
                    }
                }
            }
            if (count > 0) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(GPApplication.getInstance());
                String themeLabel = preferences.getString(MapUtilities.PREFERENCES_KEY_THEME, GPMapThemes.DEFAULT.getThemeLabel());
                mapView.setTheme(GPMapThemes.fromLabel(themeLabel));
            }
        }
    }

    public void onPause(GPMapView mapView) {
        if (mapView != null) {
//            for (Layer layer : mapView.map().layers()) {
//                if (layer instanceof IGpLayer) {
//                    IGpLayer gpLayer = (IGpLayer) layer;
//                    gpLayer.onPause();
//                }
//            }
            for (Layer layer : mapView.map().layers()) {
                if (layer instanceof IGpLayer) {
                    IGpLayer gpLayer = (IGpLayer) layer;
                    gpLayer.dispose();
                }
            }
        }
    }

    /**
     * Ad a mapsforge and mbtiles files to the list.
     *
     * <p>If a mapfile is already existing, then add to it.</p>
     *
     * @param finalFile the file to add.
     * @param index     optional index of the layer to add.
     * @return the index in which the layer was added or -1 if for some reason a new layer has not been added.
     * @throws Exception
     */
    public int addMapFile(File finalFile, Integer index) throws Exception {
        int i = finalFile.getName().lastIndexOf('.');
        String name = FileUtilities.getNameWithoutExtention(finalFile);
        checkSameNameLayerExists(name);

        JSONObject jo = null;

        ELayerTypes layerType = ELayerTypes.fromFileExt(finalFile.getName());
        if (layerType == null) {
//            throw new RuntimeException();
        } else {

            switch (layerType) {
                case MAPSFORGE: {
                    JSONObject existingObj = null;
                    for (JSONObject def : userLayersDefinitions) {
                        String type = def.getString(IGpLayer.LAYERTYPE_TAG);
                        ELayerTypes layerTypes = ELayerTypes.fromType(type);
                        if (layerTypes == ELayerTypes.MAPSFORGE) {
                            existingObj = def;
                            break;
                        }
                    }

                    if (existingObj != null) {
                        String existingName = existingObj.getString(IGpLayer.LAYERNAME_TAG);
                        String existingPath = existingObj.getString(IGpLayer.LAYERPATH_TAG);
                        existingName += IGpLayer.PATHS_DELIMITER + name;
                        existingPath += IGpLayer.PATHS_DELIMITER + finalFile.getAbsolutePath();
                        existingObj.put(IGpLayer.LAYERNAME_TAG, existingName);
                        existingObj.put(IGpLayer.LAYERPATH_TAG, existingPath);
                        return -1;
                    } else {
                        jo = new JSONObject();
                        jo.put(IGpLayer.LAYERTYPE_TAG, layerType.getTilesType());
                        jo.put(IGpLayer.LAYERNAME_TAG, name);
                        jo.put(IGpLayer.LAYERPATH_TAG, finalFile.getAbsolutePath());
                        if (!userLayersDefinitions.contains(jo))
                            if (index != null) {
                                userLayersDefinitions.add(index, jo);
                            } else {
                                userLayersDefinitions.add(jo);
                            }
                    }
                    break;
                }
                case MBTILES: {
                    jo = new JSONObject();
                    jo.put(IGpLayer.LAYERTYPE_TAG, layerType.getTilesType());
                    jo.put(IGpLayer.LAYERNAME_TAG, name);
                    jo.put(IGpLayer.LAYERPATH_TAG, finalFile.getAbsolutePath());
                    if (!userLayersDefinitions.contains(jo))
                        if (index != null) {
                            userLayersDefinitions.add(index, jo);
                        } else {
                            userLayersDefinitions.add(jo);
                        }
                }
            }
        }
        if (jo != null) {
            return userLayersDefinitions.indexOf(jo);
        } else {
            return -1;
        }
    }

    private void checkSameNameLayerExists(String name) throws Exception {
        for (JSONObject definition : userLayersDefinitions) {
            String existingName = definition.getString(IGpLayer.LAYERNAME_TAG);
            if (existingName.equals(name)) {
                throw new Exception(LayerManager.SAME_NAME_EXISTS);
            }
        }
        for (JSONObject definition : systemLayersDefinitions) {
            String existingName = definition.getString(IGpLayer.LAYERNAME_TAG);
            if (existingName.equals(name)) {
                throw new Exception(LayerManager.SAME_NAME_EXISTS);
            }
        }

    }

    public int addSpatialiteTable(File dbFile, String tableName, Integer index) throws Exception {
        checkSameNameLayerExists(tableName);

        JSONObject jo = new JSONObject();
        jo.put(IGpLayer.LAYERTYPE_TAG, ELayerTypes.SPATIALITE.getVectorType());
        jo.put(IGpLayer.LAYERNAME_TAG, tableName);
        jo.put(IGpLayer.LAYERPATH_TAG, dbFile);
        jo.put(IGpLayer.LAYERMAXZOOM_TAG, 25);
        if (!userLayersDefinitions.contains(jo))
            if (index != null) {
                userLayersDefinitions.add(index, jo);
            } else {
                userLayersDefinitions.add(jo);
            }
        return userLayersDefinitions.indexOf(jo);
    }

    public int addGeopackageTable(File dbFile, String tableName, Integer index, String layerTypeClass) throws Exception {
        checkSameNameLayerExists(tableName);

        JSONObject jo = new JSONObject();
        jo.put(IGpLayer.LAYERTYPE_TAG, layerTypeClass);
        jo.put(IGpLayer.LAYERNAME_TAG, tableName);
        jo.put(IGpLayer.LAYERPATH_TAG, dbFile);
        jo.put(IGpLayer.LAYERMAXZOOM_TAG, 25);
        if (!userLayersDefinitions.contains(jo))
            if (index != null) {
                userLayersDefinitions.add(index, jo);
            } else {
                userLayersDefinitions.add(jo);
            }
        return userLayersDefinitions.indexOf(jo);
    }

    public int addMapurlBitmapTileService(File mapurlFile, Integer index) throws Exception {
//        url=http://tile.openstreetmap.org/ZZZ/XXX/YYY.png
//        minzoom=0
//        maxzoom=19
//        center=11.42 46.8
//        type=google
//        format=png
//        defaultzoom=13
//        mbtiles=defaulttiles/_mapnik.mbtiles
//        description=Mapnik - Openstreetmap Slippy Map Tileserver - Data, imagery and map information provided by MapQuest, OpenStreetMap and contributors, ODbL.

        String name = FileUtilities.getNameWithoutExtention(mapurlFile);
        checkSameNameLayerExists(name);
        LinkedHashMap<String, String> paramsMap = FileUtilities.readFileToHashMap(mapurlFile, null, false);

        String url = paramsMap.get("url");
        if (url != null) {
            int firstSlash = url.indexOf('/', 9);
            if (firstSlash != -1) {
                String newUrl = url.substring(0, firstSlash);
                String path = url.substring(firstSlash);
                path = path.replace("ZZZ", "{Z}");
                path = path.replace("YYY", "{Y}");
                path = path.replace("XXX", "{X}");

                String maxZoomStr = paramsMap.get("maxzoom");
                int maxZoom = 19;
                try {
                    if (maxZoomStr != null) {
                        maxZoom = (int) Double.parseDouble(maxZoomStr);
                    }
                } catch (Exception e) {
                    // ignore
                }

                return addBitmapTileService(name, newUrl, path, maxZoom, 1f, null);
            }
        }

        throw new Exception("Mapurl file format not recognized.");
    }

    public int addBitmapTileService(String name, String url, String tilePath, int maxZoom, float alpha, Integer index) throws Exception {
        checkSameNameLayerExists(name);
        JSONObject jo = new JSONObject();
        jo.put(IGpLayer.LAYERTYPE_TAG, ELayerTypes.BITMAPTILESERVICE.getTilesType());
        jo.put(IGpLayer.LAYERNAME_TAG, name);
        jo.put(IGpLayer.LAYERURL_TAG, url);
        jo.put(IGpLayer.LAYERPATH_TAG, tilePath);
        jo.put(IGpLayer.LAYERMAXZOOM_TAG, maxZoom);
        jo.put(IGpLayer.LAYERALPHA_TAG, alpha);
        if (!userLayersDefinitions.contains(jo))
            if (index != null) {
                userLayersDefinitions.add(index, jo);
            } else {
                userLayersDefinitions.add(jo);
            }
        return userLayersDefinitions.indexOf(jo);
    }

    public void setEnabled(int position, boolean enabled) {
        try {
            List<JSONObject> list = cardinalLayersDefinitions;
            JSONObject layerObj = list.get(position);
            layerObj.put(IGpLayer.LAYERENABLED_TAG, enabled);
            if(!layerObj.getString(IGpLayer.LAYERTYPE_TAG).equals(EdgesLayer.class.getCanonicalName())
                    && !layerObj.getString(IGpLayer.LAYERTYPE_TAG).equals(CardinalLineLayer.class.getCanonicalName())
                    && !layerObj.getString(IGpLayer.LAYERTYPE_TAG).equals(CardinalPolygonLayer.class.getCanonicalName())) {
                Long layerId = layerObj.getLong("ID");
                cu.phibrain.plugins.cardinal.io.database.entity.model.Layer layer = LayerOperations.getInstance().load(layerId);
                layer.setIsActive(enabled);
                LayerOperations.getInstance().update(layer);
            }

        } catch (Exception e) {
            GPLog.error(this, null, e);
        }
    }

    public void setEnabled(boolean isSystem, int position, boolean enabled) {
        try {
            List<JSONObject> list = isSystem ? systemLayersDefinitions : userLayersDefinitions;
            JSONObject layerObj = list.get(position);
            layerObj.put(IGpLayer.LAYERENABLED_TAG, enabled);
        } catch (Exception e) {
            GPLog.error(this, null, e);
        }
    }

    public void changeLayerPosition(int layer, int fromRow, int toRow) {
        List<JSONObject> list = null;
        switch (layer){
            case 0:
                list = userLayersDefinitions;
                break;
            case 1:
                list = systemLayersDefinitions;
                break;
            case 2:
                list = cardinalLayersDefinitions;
                break;
            default:
                list = systemLayersDefinitions;
                break;
        }
        if (list.size() > fromRow && list.size() > toRow) {
            JSONObject item = list.remove(fromRow);
            list.add(toRow, item);
        }
    }

    public List<IVectorDbLayer> getEnabledVectorLayers(GPMapView mapView) {
        List<IGpLayer> layers = mapView.getLayers();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return layers.stream().filter(l -> l instanceof IVectorDbLayer && l.isEnabled()).map(l -> (IVectorDbLayer) l).collect(Collectors.toList());
        } else {
            List<IVectorDbLayer> vectorLayers = new ArrayList<>();
            for (IGpLayer l : layers) {
                if (l instanceof IVectorDbLayer && l.isEnabled()) {
                    vectorLayers.add((IVectorDbLayer) l);
                }
            }
            return vectorLayers;
        }
    }

    /**
     * Get all editable and enabled layers.
     *
     * @param mapView the mapview.
     * @return the list of editable and enabled layers.
     */
    public List<IEditableLayer> getEditableLayers(GPMapView mapView) {
        List<IGpLayer> layers = mapView.getLayers();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return layers.stream().filter(l -> l instanceof IEditableLayer && l.isEnabled() && ((IEditableLayer) l).isEditable()).map(l -> (IEditableLayer) l).collect(Collectors.toList());
        } else {
            List<IEditableLayer> editableLayers = new ArrayList<>();
            for (IGpLayer l : layers) {
                if (l instanceof IEditableLayer && l.isEnabled() && ((IEditableLayer) l).isEditable()) {
                    editableLayers.add((IEditableLayer) l);
                }
            }
            return editableLayers;
        }
    }
}
