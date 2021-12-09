package cu.phibrain.cardinal.app.helpers;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import cu.phibrain.cardinal.app.ui.service.AutoSyncProjectService;

public class SyncServiceUtilities {

    /**
     * Start the service.
     *
     * @param activity the activity to use.
     */
    public static void startAutoSyncService(Activity activity) {
        Intent intent = new Intent(activity, AutoSyncProjectService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            activity.startForegroundService(intent);
        } else {
            activity.startService(intent);
        }

    }

    /**
     * Stop the service.
     *
     * @param activity the activity to use.
     */
    public static void stopAutoSyncService(Activity activity) {
        Intent intent = new Intent(activity, AutoSyncProjectService.class);
        activity.stopService(intent);
    }
}
