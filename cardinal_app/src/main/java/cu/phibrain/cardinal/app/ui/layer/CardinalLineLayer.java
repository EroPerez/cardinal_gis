package cu.phibrain.cardinal.app.ui.layer;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;

import org.hortonmachine.dbs.datatypes.EGeometryType;
import org.json.JSONException;
import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;
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
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.plugins.cardinal.io.R;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Layer;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.RouteSegment;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.LayerOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.RouteSegmentOperations;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.map.GPGeoPoint;
import eu.geopaparazzi.map.GPMapPosition;
import eu.geopaparazzi.map.GPMapView;
import eu.geopaparazzi.map.features.Feature;
import eu.geopaparazzi.map.features.editing.EditManager;
import eu.geopaparazzi.map.layers.interfaces.IEditableLayer;
import eu.geopaparazzi.map.layers.interfaces.ISystemLayer;
import eu.geopaparazzi.map.layers.layerobjects.GPLineDrawable;

public class CardinalLineLayer extends VectorLayer implements ISystemLayer, IEditableLayer, ICardinalLine {

    public static String NAME = null;
    private final SharedPreferences peferences;
    private GPMapView mapView;
    private Style lineStyle = null;
    private eu.geopaparazzi.library.style.Style gpStyle;
    public CardinalLineLayer(GPMapView mapView) {
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
            NAME = context.getString(R.string.cardinal_line);
        }
        return NAME;
    }

    public void reloadData() throws IOException {
        GPMapPosition mapPosition = mapView.getMapPosition();
        int zoom = mapPosition.getZoomLevel();

        tmpDrawables.clear();
        mDrawables.clear();
        if(zoom > 18 ) {
            if (lineStyle == null) {
                lineStyle = Style.builder()
                        .strokeColor(Color.YELLOW)
                        .strokeWidth(2f)
                        .cap(Paint.Cap.ROUND)
                        .build();
            }
            List<Layer> layers = LayerOperations.getInstance().getAll();
            for (Layer layer:layers) {
                if(layer.getEnabled()){
                    for (MapObjecType mto:layer.getMapobjectypes()) {
                        if(mto.getGeomType() == MapObjecType.GeomType.POLYLINE) {
                            for (MapObject mo : mto.getMapObjects()) {
                                List<GeoPoint> points = new ArrayList<>();
                                for (GPGeoPoint point :mo.getCoord()) {
                                    points.add(((GeoPoint)point));
                                }
                                if(points.size()>1) {
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
        AppContainer appContainer = ((CardinalApplication) CardinalApplication.getInstance()).appContainer;
        MapObject mo = appContainer.getMapObjectActive();
        List<GPGeoPoint> coords = new ArrayList<>();
        for (Coordinate cord:geometry.getCoordinates()) {
            coords.add(new GPGeoPoint(cord.x,cord.y));
        }
        mo.setCoord(coords);
        mo.update();
        this.reloadData();
        EditManager.INSTANCE.setEditLayer(null);
    }

    @Override
    public void updateFeatureGeometry(Feature feature, Geometry geometry, int geometrySrid) throws Exception {

    }

    @Override
    public EGeometryType getGeometryType() {
        return EGeometryType.LINESTRING;
    }
}

