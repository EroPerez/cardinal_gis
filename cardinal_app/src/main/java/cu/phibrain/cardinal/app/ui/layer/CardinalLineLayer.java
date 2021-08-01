package cu.phibrain.cardinal.app.ui.layer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.hortonmachine.dbs.datatypes.EGeometryType;
import org.json.JSONException;
import org.json.JSONObject;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.oscim.backend.canvas.Paint;
import org.oscim.core.GeoPoint;
import org.oscim.event.Gesture;
import org.oscim.event.MotionEvent;
import org.oscim.layers.vector.VectorLayer;
import org.oscim.layers.vector.geometries.Style;
import org.oscim.map.Layers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.cardinal.app.MapviewActivity;
import cu.phibrain.cardinal.app.R;
import cu.phibrain.cardinal.app.helpers.LatLongUtils;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.cardinal.app.injections.UserMode;
import cu.phibrain.cardinal.app.ui.fragment.BarcodeReaderDialogFragment;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Layer;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.LayerOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectOperations;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.IActivitySupporter;
import eu.geopaparazzi.map.GPGeoPoint;
import eu.geopaparazzi.map.GPMapPosition;
import eu.geopaparazzi.map.GPMapView;
import eu.geopaparazzi.map.features.Feature;
import eu.geopaparazzi.map.layers.interfaces.IEditableLayer;
import eu.geopaparazzi.map.layers.interfaces.ISystemLayer;
import eu.geopaparazzi.map.layers.layerobjects.GPLineDrawable;

public class CardinalLineLayer extends VectorLayer implements ISystemLayer, IEditableLayer, ICardinalLine {

    public static String NAME = null;
    private final SharedPreferences peferences;
    private GPMapView mapView;
    private Style lineStyle = null;
    private eu.geopaparazzi.library.style.Style gpStyle;
    private IActivitySupporter activitySupporter;

    public CardinalLineLayer(GPMapView mapView, IActivitySupporter activitySupporter) {
        super(mapView.map());

        peferences = PreferenceManager.getDefaultSharedPreferences(mapView.getContext());
        this.mapView = mapView;
        this.activitySupporter = activitySupporter;
        getName(mapView.getContext());

        try {
            reloadData();
        } catch (IOException e) {
            GPLog.error(this, null, e);
        }
    }

    public static String getName(Context context) {
        if (NAME == null) {
            NAME = context.getString(R.string.cardinal_line);
        }
        return NAME;
    }

    public void reloadData() throws IOException {
        GPMapPosition mapPosition = mapView.getMapPosition();
        int zoom = mapPosition.getZoomLevel();

        tmpDrawables.clear();
        mDrawables.clear();
        if ((double) zoom >= LatLongUtils.LINE_AND_POLYGON_VIEW_ZOOM) {
            if (lineStyle == null) {
                lineStyle = Style.builder()
                        .strokeColor(Color.YELLOW)
                        .strokeWidth(2f)
                        .cap(Paint.Cap.ROUND)
                        .build();
            }
            List<Layer> layers = LayerOperations.getInstance().getAll();
            for (Layer layer : layers) {
                if (layer.getEnabled()) {
                    for (MapObjecType mto : layer.getMapobjectypes()) {
                        if (mto.getGeomType() == MapObjecType.GeomType.POLYLINE) {
                            mto.resetMapObjects();
                            for (MapObject mo : mto.getMapObjects()) {
                                List<GeoPoint> points = new ArrayList<>();
                                for (GPGeoPoint point : mo.getCoord()) {
                                    points.add(((GeoPoint) point));
                                }
                                if (points.size() > 1) {
                                    GPLineDrawable drawable = new GPLineDrawable(points, lineStyle, mo.getId());
                                    add(drawable);
                                }
                            }
                        }
                    }
                }
            }
        }
        update();
    }

    public void disable() {
        setEnabled(false);
    }


    public void enable() {
        setEnabled(true);
    }

    @Override
    public String getId() {
        return getName();
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public GPMapView getMapView() {
        return mapView;
    }

    @Override
    public void load() {
        Layers layers = map().layers();
        layers.add(this, CardinalLayerGroups.GROUP_CARDINALLAYERS.getGroupId());
    }

    @Override
    public JSONObject toJson() throws JSONException {
        return toDefaultJson();
    }

    @Override
    public void dispose() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }


    @Override
    public boolean onGesture(Gesture g, MotionEvent e) {

        if (g instanceof Gesture.Tap) {
            if (tmpDrawables.size() > 0) {
                GPLineDrawable indexLine = (GPLineDrawable) tmpDrawables.get(tmpDrawables.size() - 1);

                GPDialogs.toast(mapView.getContext(), Long.toString(indexLine.getId()), Toast.LENGTH_SHORT);
                tmpDrawables.clear();
            }
        }
        return false;
    }

    @Override
    public boolean isEditable() {
        return false;
    }

    @Override
    public boolean isInEditingMode() {
        return false;
    }

    @Override
    public List<Feature> getFeatures(Envelope env) throws Exception {
        return null;
    }

    @Override
    public void deleteFeatures(List<Feature> features) throws Exception {
        AppContainer appContainer = ((CardinalApplication) CardinalApplication.getInstance()).getContainer();
        MapObject currentMO = appContainer.getCurrentMapObject();
        Layer editLayer = currentMO.getLayer();

        GPDialogs.yesNoMessageDialog(this.activitySupporter.getContext(),
                ((MapviewActivity) this.activitySupporter).getString(cu.phibrain.cardinal.app.R.string.do_you_want_to_delete_this_map_object),
                () -> ((MapviewActivity) this.activitySupporter).runOnUiThread(() -> {
                    // stop logging
                    MapObjectOperations.getInstance().delete(currentMO);
                    appContainer.setCurrentMapObject(null);
                    try {
                        this.reloadData();
                        mapView.reloadLayer(EdgesLayer.class);
                        //Reload current point layers
                        ((CardinalGPMapView) mapView).reloadLayer(editLayer.getId());

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(MapviewActivity.ACTION_UPDATE_UI);
                    intent.putExtra("update_map_object_active", true);
                    intent.putExtra("update_map_object_type_active", true);

                    ((MapviewActivity) this.activitySupporter).sendBroadcast(intent);

                }), null
        );
        appContainer.setMode(UserMode.NONE);
    }

    @Override
    public void addNewFeatureByGeometry(Geometry geometry, int srid) throws Exception {

        BarcodeReaderDialogFragment.newInstance(
                this.mapView, LatLongUtils.toGpGeoPoints(geometry)
        ).show(
                ((MapviewActivity) this.activitySupporter).getSupportFragmentManager(),
                "dialog"
        );

    }

    @Override
    public void updateFeatureGeometry(Feature feature, Geometry geometry, int geometrySrid) throws Exception {

        AppContainer appContainer = ((CardinalApplication) CardinalApplication.getInstance()).getContainer();
        MapObject currentMO = appContainer.getCurrentMapObject();
        MapObjecType oldSelectedObjectType = null;

        if (appContainer.getMode() == UserMode.OBJECT_COORD_EDITION) {

            currentMO.setCoord(LatLongUtils.toGpGeoPoints(geometry));
            currentMO.update();


        } else if (appContainer.getMode() == UserMode.OBJECT_EDITION) {
            //Do the clone process here
            currentMO.setCoord(LatLongUtils.toGpGeoPoints(geometry));
            oldSelectedObjectType = currentMO.getObjectType();
            MapObjecType newSelectedObjectType = appContainer.getMapObjecTypeActive();
            currentMO.setMapObjectTypeId(newSelectedObjectType.getId());
            MapObjectOperations.getInstance().clone(currentMO);

            appContainer.setCurrentMapObject(currentMO);

        }

        appContainer.setMode(UserMode.NONE);
        //Reload layers associated
        this.reloadData();
        mapView.reloadLayer(EdgesLayer.class);
        //Reload current point layers
        Layer editLayer = currentMO.getLayer();
        ((CardinalGPMapView) mapView).reloadLayer(editLayer.getId());

        if(oldSelectedObjectType != null)
        {
            Layer layer = oldSelectedObjectType.getLayerObj();
            ((CardinalGPMapView) mapView).reloadLayer(layer.getId());
        }
        GPDialogs.quickInfo(mapView, ((MapviewActivity) activitySupporter).getString(cu.phibrain.cardinal.app.R.string.map_object_saved_message));

        Intent intent = new Intent(MapviewActivity.ACTION_UPDATE_UI);
        intent.putExtra("update_map_object_active", true);
        intent.putExtra("update_map_object_type_active", true);

        ((MapviewActivity) this.activitySupporter).sendBroadcast(intent);
    }

    @Override
    public EGeometryType getGeometryType() {
        return EGeometryType.LINESTRING;
    }
}

