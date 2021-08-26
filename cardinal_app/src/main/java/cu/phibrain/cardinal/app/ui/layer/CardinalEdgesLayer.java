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
import org.oscim.core.MapPosition;
import org.oscim.event.Event;
import org.oscim.event.Gesture;
import org.oscim.event.MotionEvent;
import org.oscim.layers.vector.VectorLayer;
import org.oscim.layers.vector.geometries.Drawable;
import org.oscim.layers.vector.geometries.Style;
import org.oscim.map.Layers;
import org.oscim.utils.geom.GeomBuilder;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.cardinal.app.MapviewActivity;
import cu.phibrain.cardinal.app.R;
import cu.phibrain.cardinal.app.helpers.LatLongUtils;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.cardinal.app.injections.UserMode;
import cu.phibrain.plugins.cardinal.io.database.entity.model.RouteSegment;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.RouteSegmentOperations;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.IActivitySupporter;
import eu.geopaparazzi.map.GPMapPosition;
import eu.geopaparazzi.map.GPMapView;
import eu.geopaparazzi.map.features.Feature;
import eu.geopaparazzi.map.layers.interfaces.IEditableLayer;
import eu.geopaparazzi.map.layers.interfaces.ISystemLayer;
import eu.geopaparazzi.map.layers.layerobjects.GPLineDrawable;

public class CardinalEdgesLayer extends VectorLayer implements ISystemLayer, IEditableLayer, ICardinalEdge {

    public static String NAME = null;
    private final SharedPreferences peferences;
    private GPMapView mapView;
    private Style lineStyle = null;
    private eu.geopaparazzi.library.style.Style gpStyle;
    private IActivitySupporter activitySupporter;

    public CardinalEdgesLayer(GPMapView mapView, IActivitySupporter activitySupporter) {
        super(mapView.map());
        this.activitySupporter = activitySupporter;
        peferences = PreferenceManager.getDefaultSharedPreferences(mapView.getContext());
        this.mapView = mapView;
        getName(mapView.getContext());

        try {
            reloadData();
        } catch (IOException e) {
            GPLog.error(this, null, e);
        }
    }

    public static String getName(Context context) {
        if (NAME == null) {
            NAME = context.getString(R.string.layername_edges);
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
                        .strokeColor(Color.BLACK)
                        .strokeWidth((float) LatLongUtils.SELECTION_FUZZINESS)
                        .stipple(25)
                        .stippleColor(Color.RED)
                        .cap(Paint.Cap.ROUND)
                        .build();
            }
            List<RouteSegment> routeSegments = RouteSegmentOperations.getInstance().getAll();
            for (RouteSegment route : routeSegments) {
                List<GeoPoint> list_GeoPoints = new ArrayList<>();
                if (!route.getDeleted() && (route.getOriginObj() != null && route.getDestinyObj() != null)) {
                    list_GeoPoints.add(LatLongUtils.centerPoint(route.getOriginObj().getCoord(), route.getOriginObj().getObjectType().getGeomType()));
                    list_GeoPoints.add(LatLongUtils.centerPoint(route.getDestinyObj().getCoord(), route.getDestinyObj().getObjectType().getGeomType()));
                    GPLineDrawable drawable = new GPLineDrawable(list_GeoPoints, lineStyle, route.getId());
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
        tmpDrawables.clear();
        mDrawables.clear();
        update();
    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onMapEvent(Event e, MapPosition pos) {
        super.onMapEvent(e, pos);

    }

    //pendiente de la recta
    private double m(Point a, Point b) {
        return (b.getY() - a.getY()) / (b.getX() - a.getX());
    }

    //funcion de la recta
    private double y(Point a, Point b, Point c) {
        double _m = m(a, b);
        return _m * c.getX() - _m * a.getX() + a.getY();
    }

    //Restringir a la distancia de la recta
    private boolean restriction(Point a, Point b, Point c) {
        double max_x = 0;
        double maxy = 0;

        double minx = 0;
        double miny = 0;
        //max X
        if (a.getX() > b.getX()) {
            max_x = a.getX();
            minx = b.getX();
        } else {
            max_x = b.getX();
            minx = a.getX();
        }
        //MaxY
        if (a.getY() > b.getY()) {
            maxy = a.getY();
            miny = b.getY();
        } else {
            maxy = b.getY();
            miny = a.getY();
        }
        if ((c.getX() < max_x && c.getX() > minx) && (c.getY() < maxy && c.getY() > miny))
            return true;

        return false;

    }

    private GPLineDrawable selectEdge(float cord_x, float cord_y) {
        GeoPoint geoPoint = mMap.viewport().fromScreenPoint(cord_x, cord_y);
        Point pointC = new GeomBuilder().point(geoPoint.getLongitude(), geoPoint.getLatitude()).toPoint();
        for (Drawable drawable : tmpDrawables) {
            GPLineDrawable edge = (GPLineDrawable) drawable;
            List lines = LineStringExtracter.getLines(edge.getGeometry());
            for (Object geoLine : lines) {
                Coordinate coordinateA = ((Geometry) geoLine).getCoordinates()[0];
                Coordinate coordinateB = ((Geometry) geoLine).getCoordinates()[1];
                Point pointA = new GeomBuilder().point(coordinateA.x, coordinateA.y).toPoint();
                Point pointB = new GeomBuilder().point(coordinateB.x, coordinateB.y).toPoint();
                DecimalFormat twoDForm = new DecimalFormat("#.####");
//                double _y = Double.valueOf(twoDForm.format(pointC.getY()));
//                double _yLine = Double.valueOf(twoDForm.format(y(pointA, pointB, pointC)));
//                double miny = _yLine - _y;
//                if (((miny <= 0.0004 && miny >= 0) || (miny <= -0.0004 && miny <= 0)) && restriction(pointA, pointB, pointC)) {
//                    return edge;
//                }

                //MI variante zet
                if (LatLongUtils.CheckIsPointOnLineSegment(pointC, pointA, pointB)) {
                    return edge;
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
        if (g instanceof Gesture.LongPress) {
            GPLineDrawable edge = selectEdge(e.getX(), e.getY());
            if (edge != null) {

                GPDialogs.yesNoMessageDialog((MapviewActivity) this.activitySupporter,
                        String.format(activity.getString(cu.phibrain.cardinal.app.R.string.delete_edge)),
                        () -> activity.runOnUiThread(() -> {
                            // yes
                            RouteSegmentOperations.getInstance().delete(edge.getId());
                            appContainer.setRouteSegmentActive(null);
                            remove(edge);
                            update();

                        }), () -> activity.runOnUiThread(() -> {
                            // no


                        })
                );
                return true;

            }
        } else if (g instanceof Gesture.Press) {
            GPLineDrawable edge = selectEdge(e.getX(), e.getY());
            if (edge != null) {
                Intent intent = new Intent(MapviewActivity.ACTION_UPDATE_UI);
                intent.putExtra("create_map_object_by_select_edge", edge.getId());
                ((MapviewActivity) this.activitySupporter).sendBroadcast(intent);
                return true;
            }
        }
        return false;
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

