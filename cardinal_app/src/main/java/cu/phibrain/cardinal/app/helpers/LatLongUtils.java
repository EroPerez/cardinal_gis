package cu.phibrain.cardinal.app.helpers;

import android.content.Context;
import android.widget.Toast;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.oscim.core.GeoPoint;
import org.oscim.utils.geom.GeomBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.cardinal.app.R;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.ProjectConfig;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.map.GPGeoPoint;

public class LatLongUtils {
    private static final double MAX_DISTANCE = 50.0f;
    private static final double LINE_AND_POLYGON_VIEW_ZOOM = 15;
    private static final double RADIUS_JOIN_MO = 100.0f;

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

    /**
     * For coordinates captured using a GPS, or by any means, longitude is the X value
     * and latitude is the Y value. These are for a geographic coordinate system and have
     * units of degrees.
     */
    public static List<GPGeoPoint> toGpGeoPoints(Geometry geometry) {
        List<GPGeoPoint> coords = new ArrayList<>();
        for (Coordinate cord : geometry.getCoordinates()) {
            coords.add(new GPGeoPoint(cord.y, cord.x));
        }
        return coords;
    }


    public static GPGeoPoint centerPoint(List<GPGeoPoint> points, MapObjecType.GeomType geomType) {
        GeomBuilder builder = new GeomBuilder();
        for (GeoPoint point : points) {
            builder.point(point.getLongitude(),
                    point.getLatitude());
        }
        Point centroid = null;
        if (geomType == MapObjecType.GeomType.POLYGON) {
            centroid = builder.toPolygon().getCentroid();
        } else if (geomType == MapObjecType.GeomType.POLYLINE) {
            centroid = builder.toLineString().getCentroid();
        } else if (geomType == MapObjecType.GeomType.POINT) {
            centroid = builder.toPoint().getCentroid();
        }
        return centroid == null ? null : new GPGeoPoint(centroid.getY() + 0.000001, centroid.getX() + 0.000001);

    }

    public static double getMaxDistance() {
        AppContainer appContainer = ((CardinalApplication) CardinalApplication.getInstance()).getContainer();
        try {
            List<ProjectConfig> cfgs = appContainer.getProjectActive().getConfigurations();
            for (ProjectConfig cfg:
                 cfgs) {
                if(cfg.getConfigType() == ProjectConfig.ConfigType.MAP_OBJECT_OFFSET ) {
                    return NumberUtiles.parseStringToDouble(cfg.getValue(), MAX_DISTANCE);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return MAX_DISTANCE;
    }

    public static double getLineAndPolygonViewZoom() {
        AppContainer appContainer = ((CardinalApplication) CardinalApplication.getInstance()).getContainer();
        try {
            List<ProjectConfig> cfgs = appContainer.getProjectActive().getConfigurations();
            for (ProjectConfig cfg:
                    cfgs) {
                if(cfg.getConfigType() == ProjectConfig.ConfigType.LINE_AND_POLYGON_VIEW_ZOOM ) {
                    return NumberUtiles.parseStringToDouble(cfg.getValue(), LINE_AND_POLYGON_VIEW_ZOOM);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return LINE_AND_POLYGON_VIEW_ZOOM;
    }

    public static double getRadiusJoinMo() {
        AppContainer appContainer = ((CardinalApplication) CardinalApplication.getInstance()).getContainer();
        try {
            List<ProjectConfig> cfgs = appContainer.getProjectActive().getConfigurations();
            for (ProjectConfig cfg:
                    cfgs) {
                if(cfg.getConfigType() == ProjectConfig.ConfigType.MAP_OBJECT_JOINT_OFFSET ) {
                    return NumberUtiles.parseStringToDouble(cfg.getValue(), RADIUS_JOIN_MO);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return RADIUS_JOIN_MO;
    }
}

