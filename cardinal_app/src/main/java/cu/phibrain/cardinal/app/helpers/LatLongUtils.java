package cu.phibrain.cardinal.app.helpers;

import android.content.Context;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.oscim.core.GeoPoint;
import org.oscim.utils.geom.GeomBuilder;

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

import static java.lang.Math.abs;

public class LatLongUtils {
    private static final double MAX_DISTANCE = 50.0f;
    private static final double LINE_AND_POLYGON_VIEW_ZOOM = 15;
    private static final double RADIUS_JOIN_MO = 100.0f;
    public static final double EPSILON = 9E-7;
    public static final double SELECTION_FUZZINESS = 2.5f;


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
            for (ProjectConfig cfg :
                    cfgs) {
                if (cfg.getConfigType() == ProjectConfig.ConfigType.MAP_OBJECT_OFFSET) {
                    return NumberUtiles.parseStringToDouble(cfg.getValue(), MAX_DISTANCE);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MAX_DISTANCE;
    }

    public static double getLineAndPolygonViewZoom() {
        AppContainer appContainer = ((CardinalApplication) CardinalApplication.getInstance()).getContainer();
        try {
            List<ProjectConfig> cfgs = appContainer.getProjectActive().getConfigurations();
            for (ProjectConfig cfg :
                    cfgs) {
                if (cfg.getConfigType() == ProjectConfig.ConfigType.LINE_AND_POLYGON_VIEW_ZOOM) {
                    return NumberUtiles.parseStringToDouble(cfg.getValue(), LINE_AND_POLYGON_VIEW_ZOOM);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return LINE_AND_POLYGON_VIEW_ZOOM;
    }

    public static double getRadiusJoinMo() {
        AppContainer appContainer = ((CardinalApplication) CardinalApplication.getInstance()).getContainer();
        try {
            List<ProjectConfig> cfgs = appContainer.getProjectActive().getConfigurations();
            for (ProjectConfig cfg :
                    cfgs) {
                if (cfg.getConfigType() == ProjectConfig.ConfigType.MAP_OBJECT_JOINT_OFFSET) {
                    return NumberUtiles.parseStringToDouble(cfg.getValue(), RADIUS_JOIN_MO);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return RADIUS_JOIN_MO;
    }

    /**
     * Given three colinear points a, b, c, the function checks if point c lies on line segment 'ab'
     * Method:
     * Check if the cross product of (b-a) and (c-a) is 0, as tells Darius Bacon, tells you if the points a, b and c are aligned.
     * <p>
     * But, as you want to know if c is between a and b, you also have to check that the dot product of (b-a) and (c-a) is positive and is less than the square of the distance between a and b.
     * <p>
     * In non-optimized pseudocode:
     * <p>
     * def isOnLine(a, b, c):
     * crossproduct = (c.y - a.y) * (b.x - a.x) - (c.x - a.x) * (b.y - a.y)
     * <p>
     * # compare versus epsilon for floating point values, or != 0 if using integers
     * if abs(crossproduct) > epsilon:
     * return False
     * <p>
     * dotproduct = (c.x - a.x) * (b.x - a.x) + (c.y - a.y)*(b.y - a.y)
     * if dotproduct < 0:
     * return False
     * <p>
     * squaredlengthba = (b.x - a.x)*(b.x - a.x) + (b.y - a.y)*(b.y - a.y)
     * if dotproduct > squaredlengthba:
     * return False
     * <p>
     * return True
     *
     * @param a
     * @param b
     * @param c
     * @return boolean
     */

    public static boolean IsOnSegment(@NotNull Point a, @NotNull Point b, @NotNull Point c) {
        double crossproduct = (c.getY() - a.getY()) * (b.getX() - a.getX())
                - (c.getX() - a.getX()) * (b.getY() - a.getY());

        // compare versus epsilon for floating point values, or != 0 if using integers
        if (abs(crossproduct) > EPSILON)
            return false;

        double dotproduct = (c.getX() - a.getX()) * (b.getX() - a.getX()) + (c.getY() - a.getY()) * (b.getY() - a.getY());

        if (dotproduct < 0)
            return false;

        double squaredlengthba = (b.getX() - a.getX()) * (b.getX() - a.getX()) + (b.getY() - a.getY()) * (b.getY() - a.getY());
        if (dotproduct > squaredlengthba)
            return false;


        return onSegment(a, c, b);
    }

    // Given three colinear points p, q, r,
    // the function checks if point q lies
    // on line segment 'pr'
    private static boolean onSegment(@NotNull Point p, @NotNull Point q, @NotNull Point r) {
        if (q.getX() <= Math.max(p.getX(), r.getX()) &&
                q.getX() >= Math.min(p.getX(), r.getX()) &&
                q.getY() <= Math.max(p.getY(), r.getY()) &&
                q.getY() >= Math.min(p.getY(), r.getY())) {
            return true;
        }
        return false;
    }

    /**
     * Tests whether a point lies on the line defined by a list of
     * coordinates.
     *
     * The best way to determine if a point R = (rx, ry) lies on the line connecting points P = (px, py)
     * and Q = (qx, qy) is to check whether the determinant of the matrix
     * <p>
     * {{qx - px, qy - py}, {rx - px, ry - py}},
     * <p>
     * namely (qx - px) * (ry - py) - (qy - py) * (rx - px) is close to 0. This solution has several
     * related advantages over the others posted: first, it requires no special case for vertical lines,
     * second, it doesn't divide (usually a slow operation), third, it doesn't trigger bad
     * floating-point behavior when the line is almost, but not quite vertical.
     *
     * @param r    the point to test
     * @param p the line coordinate
     * @param q the other line coordinate
     * @return true if the point is a vertex of the line or lies in the interior
     * of a line segment in the line
     */
    public static boolean CheckIsPointOnLineSegment(@NotNull Point r, @NotNull Point p, @NotNull Point q) {

        double det = Math.abs((q.getX() - p.getX()) * (r.getY() - p.getY()) - (q.getY() - p.getY()) * (r.getX() - p.getX()));

        return det <= EPSILON && onSegment(p, r, q);

    }
}

