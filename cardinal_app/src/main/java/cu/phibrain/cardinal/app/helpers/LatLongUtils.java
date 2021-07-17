package cu.phibrain.cardinal.app.helpers;

import android.content.Context;
import android.widget.Toast;

import cu.phibrain.cardinal.app.R;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.map.GPGeoPoint;

public class LatLongUtils {
    public static final double MAX_DISTANCE = 50.0f;

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
}

