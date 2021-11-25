package cu.phibrain.cardinal.app.ui.layer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

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
import java.util.Iterator;
import java.util.List;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.cardinal.app.MapviewActivity;
import cu.phibrain.cardinal.app.R;
import cu.phibrain.cardinal.app.helpers.LatLongUtils;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.cardinal.app.injections.UserMode;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectOperations;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.IActivitySupporter;
import eu.geopaparazzi.map.GPMapPosition;
import eu.geopaparazzi.map.GPMapView;
import eu.geopaparazzi.map.features.Feature;
import eu.geopaparazzi.map.layers.interfaces.IEditableLayer;
import eu.geopaparazzi.map.layers.interfaces.ISystemLayer;
import eu.geopaparazzi.map.layers.layerobjects.GPLineDrawable;

public class CardinalJoinsLayer extends VectorLayer implements ISystemLayer, IEditableLayer, ICardinalJoint {

    public static String NAME = null;

    private GPMapView mapView;
    private Style lineStyle = null;
    private IActivitySupporter activitySupporter;
    private final SharedPreferences preferences;

    /*
     * Join visibility
     */
    public static String PREFS_KEY_MAP_OBJECT_JOIN_VISIBLE = "PREFS_KEY_MAP_OBJECT_JOIN_VISIBLE";


    public CardinalJoinsLayer(GPMapView mapView, IActivitySupporter activitySupporter) {
        super(mapView.map());
        this.mapView = mapView;
        this.activitySupporter = activitySupporter;
        getName(mapView.getContext());

        preferences = PreferenceManager.getDefaultSharedPreferences(mapView.getContext());
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putBoolean(PREFS_KEY_MAP_OBJECT_JOIN_VISIBLE, isEnabled());
        prefEditor.commit();

        try {
            reloadData();
        } catch (IOException e) {
            GPLog.error(this, null, e);
        }
    }

    public static String getName(Context context) {
        if (NAME == null) {
            NAME = context.getString(R.string.layername_jointo);
        }
        return NAME;
    }

    public void reloadData() throws IOException {
        GPMapPosition mapPosition = mapView.getMapPosition();
        int zoom = mapPosition.getZoomLevel();

        if (!isEnabled()) {
            return;
        }

        tmpDrawables.clear();
        mDrawables.clear();
        if ((double) zoom >= LatLongUtils.getLineAndPolygonViewZoom()) {
            if (lineStyle == null) {
                lineStyle = Style.builder()
                        .strokeColor(Color.GREEN)
                        .strokeWidth((float) LatLongUtils.SELECTION_FUZZINESS)
                        .stipple(20)
                        .stippleColor(Color.RED)
                        .cap(Paint.Cap.ROUND)
                        .build();
            }
            List<MapObject> mapObjectList = MapObjectOperations.getInstance().getAll();
            for (MapObject jointTo : mapObjectList) {
                List<GeoPoint> list_GeoPoints = new ArrayList<>();
                for (MapObject joinFrom : jointTo.getJoinedList()) {
                    list_GeoPoints.add(joinFrom.getCentroid());
                    list_GeoPoints.add(jointTo.getCentroid());
                    GPLineDrawable drawable = new GPLineDrawable(list_GeoPoints, lineStyle, joinFrom.getId());
                    add(drawable);
                }
            }
        }
        update();

    }

    @Override
    public void setEnabled(boolean enabled) {
        if (enabled == isEnabled())
            return;

        super.setEnabled(enabled);

        // update preferences to show/hide blue circle
        SharedPreferences.Editor prefEditor = preferences.edit();
        prefEditor.putBoolean(PREFS_KEY_MAP_OBJECT_JOIN_VISIBLE, isEnabled());
        prefEditor.commit();

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

        for (Iterator<Drawable> it = tmpDrawables.iterator(); it.hasNext(); ) {
            Drawable drawable = it.next();
            if (drawable != null) {
                it.remove();
            }
        }

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    private GPLineDrawable selectJoin(float cord_x, float cord_y, Integer index) {
        GeoPoint geoPoint = mMap.viewport().fromScreenPoint(cord_x, cord_y);
        Point pointC = new GeomBuilder().point(geoPoint.getLongitude(), geoPoint.getLatitude()).toPoint();
        for (; index < tmpDrawables.size(); index++) {
            Drawable drawable = tmpDrawables.get(index);
            GPLineDrawable join = (GPLineDrawable) drawable;
            List lines = LineStringExtracter.getLines(join.getGeometry());
            for (Object geoLine : lines) {
                Coordinate coordinateA = ((Geometry) geoLine).getCoordinates()[0];
                Coordinate coordinateB = ((Geometry) geoLine).getCoordinates()[1];
                Point pointA = new GeomBuilder().point(coordinateA.x, coordinateA.y).toPoint();
                Point pointB = new GeomBuilder().point(coordinateB.x, coordinateB.y).toPoint();
                if (LatLongUtils.CheckIsPointOnLineSegment(pointC, pointA, pointB)) {
                    return join;
                }

            }
        }
        return null;
    }

    @Override
    public boolean onGesture(Gesture g, MotionEvent e) {
        AppContainer appContainer = ((CardinalApplication) CardinalApplication.getInstance()).getContainer();
        MapviewActivity activity = (MapviewActivity) this.activitySupporter;
        if (!isEnabled() || appContainer.getMode() != UserMode.NONE) {
            return false;
        }
        Integer index = 0;
        if (g instanceof Gesture.DoubleTap) {
            GPLineDrawable selectedJoinObj = selectJoin(e.getX(), e.getY(), index);
            if (selectedJoinObj != null) {

                MapObject joinObj = MapObjectOperations.getInstance().load(selectedJoinObj.getId());
                if (joinObj != null) {
                    GPDialogs.yesNoMessageDialog((MapviewActivity) this.activitySupporter,
                            String.format(activity.getString(cu.phibrain.cardinal.app.R.string.do_you_want_to_undock_this_map_object)),
                            () -> activity.runOnUiThread(() -> {
                                // yes
                                joinObj.setJoinObj(null);
                                MapObjectOperations.getInstance().save(joinObj);
                                joinObj.resetJoinedList();
                                remove(selectedJoinObj);
                                update();

                            }), null
                    );

                }
            }
            return true;
        } else if (g instanceof Gesture.LongPress) {
            GPLineDrawable selectedJoinObj = selectJoin(e.getX(), e.getY(), index);
            if (selectedJoinObj != null) {
                return onItemLongPress(index, selectedJoinObj);
            }


        }
        return false;
    }


    public boolean onItemLongPress(int index, GPLineDrawable item) {
        AppContainer appContainer = ((CardinalApplication) CardinalApplication.getInstance()).getContainer();
        appContainer.setRouteSegmentActive(null);

        if (item != null) {
            MapObject objectSelected = MapObjectOperations.getInstance().load(item.getId());
            MapObject JoinObj = objectSelected.getJoinObj();
            appContainer.setCurrentMapObject(JoinObj);
            appContainer.setMapObjecTypeActive(JoinObj.getObjectType());

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

    }

    @Override
    public void addNewFeatureByGeometry(Geometry geometry, int srid) throws Exception {

    }

    @Override
    public void updateFeatureGeometry(Feature feature, Geometry geometry, int geometrySrid) throws Exception {

    }

    @Override
    public EGeometryType getGeometryType() {
        return EGeometryType.LINESTRING;
    }
}
