package cu.phibrain.plugins.cardinal.io.ui.layer;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.oscim.backend.canvas.Paint;
import org.oscim.core.Box;
import org.oscim.event.Gesture;
import org.oscim.event.MotionEvent;
import org.oscim.layers.marker.MarkerItem;
import org.oscim.layers.vector.VectorLayer;
import org.oscim.layers.vector.geometries.Drawable;
import org.oscim.layers.vector.geometries.LineDrawable;
import org.oscim.layers.vector.geometries.PointDrawable;
import org.oscim.layers.vector.geometries.Style;
import org.oscim.map.Layers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import eu.geopaparazzi.library.GPApplication;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.database.TableDescriptions;
import org.oscim.core.GeoPoint;
import org.oscim.utils.geom.GeomBuilder;

import eu.geopaparazzi.library.style.ColorUtilities;
import eu.geopaparazzi.map.GPMapView;
import cu.phibrain.plugins.cardinal.io.R;
import eu.geopaparazzi.map.features.Feature;
import eu.geopaparazzi.map.layers.LayerGroups;
import eu.geopaparazzi.map.layers.interfaces.ISystemLayer;
import eu.geopaparazzi.map.layers.layerobjects.GPLineDrawable;
import eu.geopaparazzi.map.layers.utils.GeopackageConnectionsHandler;
import eu.geopaparazzi.map.layers.utils.GpsLog;
import eu.geopaparazzi.map.utils.MapUtilities;

public class EdgesLayer extends VectorLayer implements ISystemLayer {

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

        SQLiteDatabase sqliteDatabase = GPApplication.getInstance().getDatabase();

        String query = "SELECT " +//NON-NLS
                TableDescriptions.NotesTableFields.COLUMN_ID.getFieldName() +
                ", " +//
                TableDescriptions.NotesTableFields.COLUMN_LON.getFieldName() +
                ", " +//
                TableDescriptions.NotesTableFields.COLUMN_LAT.getFieldName() +
                ", " +//
                TableDescriptions.NotesTableFields.COLUMN_ALTIM.getFieldName() +
                ", " +//
                TableDescriptions.NotesTableFields.COLUMN_TEXT.getFieldName() +
                ", " +//
                TableDescriptions.NotesTableFields.COLUMN_TS.getFieldName() +
                ", " +//
                TableDescriptions.NotesTableFields.COLUMN_FORM.getFieldName() +//
                " FROM " + TableDescriptions.TABLE_NOTES;//NON-NLS
        query = query + " WHERE " + TableDescriptions.NotesTableFields.COLUMN_ISDIRTY.getFieldName() + " = 1";//NON-NLS
        tmpDrawables.clear();
        mDrawables.clear();
        if (lineStyle == null) {
            lineStyle = Style.builder()
                    .strokeColor(Color.BLACK)
                    .strokeWidth(2)
                    .cap(Paint.Cap.ROUND)
                    .build();
        }
        GeoPoint last_point = null;
        List<GeoPoint> list_GeoPoin = null;
        try (Cursor c = sqliteDatabase.rawQuery(query, null)) {
            c.moveToFirst();
            while (!c.isAfterLast()) {
                int i = 0;
                long id = c.getLong(i++);
                double lon = c.getDouble(i++);
                double lat = c.getDouble(i++);
                double elev = c.getDouble(i++);
                GeoPoint current_point = new GeoPoint(lat, lon);
                if(last_point == null){
                    last_point = current_point;
                }
                else {
                    list_GeoPoin = new ArrayList<>();
                    list_GeoPoin.add(last_point);
                    list_GeoPoin.add(current_point);
                    GPLineDrawable drawable = new GPLineDrawable(list_GeoPoin, lineStyle, id);
                    add(drawable);
                    last_point = current_point;
                }
                c.moveToNext();
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
        layers.add(this, LayerGroups.GROUP_PROJECTLAYERS.getGroupId());
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

                Toast.makeText(mapView.getContext(), Long.toString(indexLine.getId()), Toast.LENGTH_SHORT).show();
                tmpDrawables.clear();
            }
        }
        return false;
    }

}

