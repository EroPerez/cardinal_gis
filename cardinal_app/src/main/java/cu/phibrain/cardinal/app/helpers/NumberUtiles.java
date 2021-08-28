package cu.phibrain.cardinal.app.helpers;

import org.oscim.core.MapPosition;
import org.oscim.core.MercatorProjection;

import eu.geopaparazzi.map.GPMapView;

public class NumberUtiles {

    protected static final double UNSCALE_COORD = 4;

    public static Double parseStringToDouble(String value, double defaultValue) {
        return value == null || value.trim().isEmpty() ? defaultValue : Double.parseDouble(value);
    }

    public static Long parseStringToLong(String value, Long defaultValue) {
        try {
            if (value == null || value.trim().isEmpty())
                return defaultValue;
            else
                return Long.parseLong(value);
        } catch (Exception ex) {
            ex.printStackTrace();

        }
        return null;
    }

    public static int roundUp(double n) {

        return Integer.parseInt(Long.toString(Math.round(n + 0.1f)));
    }


    public static float metersToPixels(float meters, GPMapView mapView) {
        MapPosition mapPosition = mapView.map().getMapPosition();
        double zoom = mapPosition.getZoomLevel();
        double scale = (Math.pow(2, zoom) / UNSCALE_COORD) * (1 / Math.pow(2, zoom));
        double groundResolution = MercatorProjection.groundResolution(mapPosition);
        return (float) ((meters * (1 / groundResolution)) / scale);
    }

}
