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

    /**
     * Pad number with left zero
     *
     * @param in   The integer value
     * @param fill The number of digits to fill
     * @return The given value left padded with the given number of digits
     */
    public static String lPadZero(int in, int fill) {

        boolean negative = false;
        int value, len = 0;

        if (in >= 0) {
            value = in;
        } else {
            negative = true;
            value = -in;
            in = -in;
            len++;
        }

        if (value == 0) {
            len = 1;
        } else {
            while (value != 0) {
                value /= 10;
                len++;
            }
        }

        StringBuilder sb = new StringBuilder();

        if (negative) {
            sb.append('-');
        }

        for (int i = fill; i > len; i--) {
            sb.append('0');
        }

        sb.append(in);

        return sb.toString();
    }

}
