package cu.phibrain.cardinal.app.ui.layer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

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
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectOperations;
import eu.geopaparazzi.library.database.GPLog;
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


    public CardinalJoinsLayer(GPMapView mapView, IActivitySupporter activitySupporter) {
        super(mapView.map());
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
                        .strokeWidth(2.5f)
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
                    GPLineDrawable drawable = new GPLineDrawable(list_GeoPoints, lineStyle, jointTo.getId());
                    add(drawable);
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


    @Override
    public boolean onGesture(Gesture g, MotionEvent e) {
        if (!isEnabled()) {
            return false;
        }
        if (g instanceof Gesture.LongPress) {
            if (tmpDrawables.size() > 0) {
                GPLineDrawable selectedJoinObj = null;
                GeoPoint geoPoint = mMap.viewport().fromScreenPoint(e.getX(), e.getY());
                Point pointC = new GeomBuilder().point(geoPoint.getLongitude(), geoPoint.getLatitude()).toPoint();
                for (int index = 0; index < tmpDrawables.size(); index++) {
                    Drawable drawable = tmpDrawables.get(index);
                    selectedJoinObj = (GPLineDrawable) drawable;

                    List lines = LineStringExtracter.getLines(selectedJoinObj.getGeometry());
                    for (Object geoLine : lines) {
                        Coordinate coordinateA = ((Geometry) geoLine).getCoordinates()[0];
                        Coordinate coordinateB = ((Geometry) geoLine).getCoordinates()[1];
                        Point pointA = new GeomBuilder().point(coordinateA.x, coordinateA.y).toPoint();
                        Point pointB = new GeomBuilder().point(coordinateB.x, coordinateB.y).toPoint();
                        if (LatLongUtils.IsOnSegment(pointA, pointB, pointC)) {
                            return onItemLongPress(index, selectedJoinObj);
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

