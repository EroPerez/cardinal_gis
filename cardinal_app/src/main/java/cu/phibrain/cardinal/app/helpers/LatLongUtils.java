package cu.phibrain.cardinal.app.helpers;

import android.content.Context;
import android.widget.Toast;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.oscim.core.GeoPoint;
import org.oscim.utils.geom.GeomBuilder;

import java.util.ArrayList;
import java.util.List;

import cu.phibrain.cardinal.app.R;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.map.GPGeoPoint;

public class LatLongUtils {
    public static final double MAX_DISTANCE = 50.0f;
    public static final double  LINE_AND_POLYGON_VIEW_ZOOM = 15;
    public static double distance(MapObject mo1, MapObject mo2) {
        try {
            GPGeoPoint p1 = mo1.getCoord().get(mo1.getCoord().size() - 1);
            GPGeoPoint p2 = mo2.getCoord().get(0);

            return p1.vincentyDistance(p2);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0.0f;
    }

    public static boolean soFar(MapObject mo1, double distance, MapObject mo2) {

        return distance(mo1, mo2) > distance;

    }

    public static void showTip(Context context, double distance) {
        GPDialogs.toast(context,
                String.format(context.getString(R.string.distance_tip_message), distance),
                Toast.LENGTH_LONG);
    }

    public static List<GPGeoPoint> toGpGeoPoints(Geometry geometry){
        List<GPGeoPoint> coords = new ArrayList<>();
        for (Coordinate cord:geometry.getCoordinates()) {
            coords.add(new GPGeoPoint(cord.x,cord.y));
        }
        return coords;
    }

    public static  List<Coordinate> toCoordinates (List<GPGeoPoint> toGpGeoPoints){
        List<Coordinate> coords = new ArrayList<>();
        for (GPGeoPoint cord:toGpGeoPoints) {
            coords.add(new Coordinate(cord.getLatitude(),cord.getLongitude()));
        }
        return coords;
    }

    public static  GPGeoPoint labelPoint(List<GPGeoPoint> points, MapObjecType.GeomType geomType){
        GeomBuilder builder = new GeomBuilder();
        for (GeoPoint point : points) {
            builder.point(point.getLongitude(),
                    point.getLatitude());
        }
        Point centroid = null;
        if(geomType == MapObjecType.GeomType.POLYGON){
            centroid = builder.toPolygon().getCentroid();
        }else if(geomType == MapObjecType.GeomType.POLYLINE){
            centroid = builder.toLineString().getCentroid();
        }
        return centroid==null ? null: new GPGeoPoint(centroid.getX(), centroid.getY());

    }
}

