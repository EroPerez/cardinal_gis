package cu.phibrain.cardinal.app.ui.layer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import org.hortonmachine.dbs.datatypes.EGeometryType;
import org.json.JSONException;
import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.util.LineStringExtracter;
import org.oscim.backend.canvas.Paint;
import org.oscim.core.GeoPoint;
import org.oscim.event.Gesture;
import org.oscim.event.MotionEvent;
import org.oscim.layers.vector.VectorLayer;
import org.oscim.layers.vector.geometries.Drawable;
import org.oscim.layers.vector.geometries.Style;
import org.oscim.map.Layers;
import org.oscim.utils.geom.GeomBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.cardinal.app.MapviewActivity;
import cu.phibrain.cardinal.app.R;
import cu.phibrain.cardinal.app.helpers.LatLongUtils;
import cu.phibrain.cardinal.app.helpers.NumberUtiles;
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
import eu.geopaparazzi.library.util.TextRunnable;
import eu.geopaparazzi.map.GPMapPosition;
import eu.geopaparazzi.map.GPMapView;
import eu.geopaparazzi.map.features.Feature;
import eu.geopaparazzi.map.layers.interfaces.IEditableLayer;
import eu.geopaparazzi.map.layers.interfaces.ISystemLayer;
import eu.geopaparazzi.map.layers.layerobjects.GPLineDrawable;

public class CardinalLineLayer extends VectorLayer implements ISystemLayer, IEditableLayer, ICardinalLine {

    public static String NAME = null;
    private final SharedPreferences peferences;
    private final GPMapView mapView;
    private Style lineStyle = null;
    private eu.geopaparazzi.library.style.Style gpStyle;
    private final IActivitySupporter activitySupporter;

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
        if ((double) zoom >= LatLongUtils.getLineAndPolygonViewZoom()) {
            if (lineStyle == null) {
                lineStyle = Style.builder()
                        .strokeColor(Color.YELLOW)
                        .strokeWidth((float) LatLongUtils.SELECTION_FUZZINESS)
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
                                points.addAll(mo.getCoord());
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
        if (!isEnabled()) {
            return false;
        }
        if (g instanceof Gesture.LongPress) {
            if (tmpDrawables.size() > 0) {
                GPLineDrawable selectedLine = null;
                GeoPoint geoPoint = mMap.viewport().fromScreenPoint(e.getX(), e.getY());
                Point targetPoint = new GeomBuilder().point(geoPoint.getLongitude(), geoPoint.getLatitude()).toPoint();
                for (int index = 0; index < tmpDrawables.size(); index++) {
                    Drawable drawable = tmpDrawables.get(index);
                    selectedLine = (GPLineDrawable) drawable;

                    List lines = LineStringExtracter.getLines(selectedLine.getGeometry());
                    for (Object geoLine : lines) {
                        Coordinate coordinateA = ((Geometry) geoLine).getCoordinates()[0];
                        Coordinate coordinateB = ((Geometry) geoLine).getCoordinates()[1];
                        Point startLinePoint = new GeomBuilder().point(coordinateA.x, coordinateA.y).toPoint();
                        Point endLinePoint = new GeomBuilder().point(coordinateB.x, coordinateB.y).toPoint();
                        if (LatLongUtils.CheckIsPointOnLineSegment(targetPoint, startLinePoint,  endLinePoint)) {
                            return onItemLongPress(index, selectedLine);
                        }

                    }

                }

            }
        }
        return false;
    }

    public boolean onItemLongPress(int index, GPLineDrawable item) {
        AppContainer appContainer = ((CardinalApplication) CardinalApplication.getInstance()).getContainer();
        appContainer.setRouteSegmentActive(null);

        if (item != null) {
            MapObject objectSelected = MapObjectOperations.getInstance().load(item.getId());
            appContainer.setCurrentMapObject(objectSelected);
            appContainer.setMapObjecTypeActive(objectSelected.getObjectType());
            try {
                mapView.reloadLayer(CardinalSelectPointLayer.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Update ui
            Intent intent = new Intent(MapviewActivity.ACTION_UPDATE_UI);
            intent.putExtra("update_map_object_active", true);
            intent.putExtra("update_map_object_type_active", true);
            ((MapviewActivity) this.activitySupporter).sendBroadcast(intent);


        }
        return true;

    }

    @Override
    public boolean isEditable() {
        return true;
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
                        mapView.reloadLayer(CardinalEdgesLayer.class);
                        //Reload current point layers
                        ((CardinalGPMapView) mapView).reloadLayer(editLayer.getId());
                        mapView.reloadLayer(CardinalSelectPointLayer.class);
                        mapView.reloadLayer(CardinalJoinsLayer.class);

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
        AppCompatActivity activity = (MapviewActivity) this.activitySupporter;

        GPDialogs.inputMessageDialog(activity, activity.getString(R.string.inspector_object_grade), "2", new TextRunnable() {
            @Override
            public void run() {
                long grade = NumberUtiles.parseStringToLong(theTextToRunOn, 0L);
                BarcodeReaderDialogFragment.newInstance(
                        mapView, LatLongUtils.toGpGeoPoints(geometry), grade
                ).show(
                        activity.getSupportFragmentManager(),
                        "dialog"
                );
            }
        });

    }

    @Override
    public void updateFeatureGeometry(Feature feature, Geometry geometry, int geometrySrid) throws Exception {

        AppContainer appContainer = ((CardinalApplication) CardinalApplication.getInstance()).getContainer();
        MapObject currentMO = appContainer.getCurrentMapObject();
        MapObjecType oldSelectedObjectType = null;

        if (appContainer.getMode() == UserMode.OBJECT_COORD_EDITION) {

            currentMO.setCoord(LatLongUtils.toGpGeoPoints(geometry));
            MapObjectOperations.getInstance().save(currentMO);

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
        mapView.reloadLayer(CardinalEdgesLayer.class);
        //Reload current point layers
        Layer editLayer = currentMO.getLayer();
        ((CardinalGPMapView) mapView).reloadLayer(editLayer.getId());

        if (oldSelectedObjectType != null) {
            Layer layer = oldSelectedObjectType.getLayerObj();
            ((CardinalGPMapView) mapView).reloadLayer(layer.getId());
        }
        mapView.reloadLayer(CardinalSelectPointLayer.class);
        mapView.reloadLayer(CardinalPolygonLayer.class);
        mapView.reloadLayer(CardinalJoinsLayer.class);

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

