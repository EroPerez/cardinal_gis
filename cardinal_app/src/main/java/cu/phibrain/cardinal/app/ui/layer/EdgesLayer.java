package cu.phibrain.cardinal.app.ui.layer;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.hortonmachine.dbs.datatypes.EGeometryType;
import org.json.JSONException;
import org.json.JSONObject;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.oscim.backend.canvas.Paint;
import org.oscim.event.Gesture;
import org.oscim.event.MotionEvent;
import org.oscim.layers.vector.VectorLayer;
import org.oscim.layers.vector.geometries.Style;
import org.oscim.map.Layers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.entity.RouteSegmentOperations;
import cu.phibrain.plugins.cardinal.io.model.RouteSegment;
import eu.geopaparazzi.library.GPApplication;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.database.TableDescriptions;
import org.oscim.core.GeoPoint;

import eu.geopaparazzi.map.GPMapView;
import cu.phibrain.plugins.cardinal.io.R;
import eu.geopaparazzi.map.features.Feature;
import eu.geopaparazzi.map.layers.LayerGroups;
import eu.geopaparazzi.map.layers.interfaces.IEditableLayer;
import eu.geopaparazzi.map.layers.interfaces.ISystemLayer;
import eu.geopaparazzi.map.layers.layerobjects.GPLineDrawable;

public class EdgesLayer extends VectorLayer implements ISystemLayer, IEditableLayer, ICardinalEdge {

    public static String NAME = null;
    private final SharedPreferences peferences;
    private GPMapView mapView;
    private Style lineStyle = null;
    private eu.geopaparazzi.library.style.Style gpStyle;
    public EdgesLayer(GPMapView mapView) {
        super(mapView.map());

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
        tmpDrawables.clear();
        mDrawables.clear();
        if (lineStyle == null) {
            lineStyle = Style.builder()
                    .strokeColor(Color.BLACK)
                    .strokeWidth(1f)
                    .cap(Paint.Cap.ROUND)
                    .build();
        }
        List<RouteSegment> routeSegments = RouteSegmentOperations.getInstance().getAll();
        for (RouteSegment route:routeSegments) {
            List<GeoPoint> list_GeoPoin = new ArrayList<>();
            list_GeoPoin.add(route.getOriginObj().getCoord().get(0));
            list_GeoPoin.add(route.getDestinyObj().getCoord().get(0));
            GPLineDrawable drawable = new GPLineDrawable(list_GeoPoin, lineStyle, route.getId());
            add(drawable);
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

        if (g instanceof Gesture.Tap){
            if(tmpDrawables.size()>0) {
                GPLineDrawable indexLine = (GPLineDrawable) tmpDrawables.get(tmpDrawables.size()-1);

              //  Toast.makeText(mapView.getContext(), Long.toString(indexLine.getId()), Toast.LENGTH_SHORT).show();
                tmpDrawables.clear();
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

