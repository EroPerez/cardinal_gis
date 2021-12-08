package cu.phibrain.cardinal.app.ui.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;

import cu.phibrain.cardinal.app.ui.service.synchronize.CloudSyncAdapter;
import eu.geopaparazzi.library.network.NetworkUtilities;

public class AutoSyncProjectService extends Service {
    private static final String TAG = AutoSyncProjectService.class.getSimpleName();

    public AutoSyncProjectService() {
    }

    // Storage for an instance of the sync adapter
    private static CloudSyncAdapter sSyncAdapter = null;
    // Object to use as a thread-safe lock
    private static final Object sSyncAdapterLock = new Object();

    /*
     * Instantiate the sync adapter object.
     */
    @Override
    public void onCreate() {
        /*
         * Create the sync adapter as a singleton.
         * Set the sync adapter as syncable
         * Disallow parallel syncs
         */
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new CloudSyncAdapter(getApplicationContext(), true, true);
            }
        }
    }

    /**
     * Return an object that allows the system to invoke
     * the sync adapter.
     */
    @Override
    public IBinder onBind(Intent intent) {
        /*
         * Get the object that allows external processes
         * to call onPerformSync(). The object is created
         * in the base class code when the SyncAdapter
         * constructors call super()
         */
        return sSyncAdapter.getSyncAdapterBinder();
    }

    public static Intent getStartIntent(Context context) {
        return new Intent(context, AutoSyncProjectService.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, final int startId) {
        Log.d(TAG,"Starting sync...");

        if (!NetworkUtilities.isNetworkAvailable(this)) {
            Log.d(TAG,"Sync canceled, connection not available");

            stopSelf(startId);
            return START_NOT_STICKY;
        }


        return START_STICKY;
    }


    public static class SyncOnConnectionAvailable extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)
                    && NetworkUtilities.isNetworkAvailable(context)) {
                Log.d(TAG,"Connection is now available, triggering sync...");
                context.startService(getStartIntent(context));
            }
        }
    }
}
