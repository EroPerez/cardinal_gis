package cu.phibrain.cardinal.app.ui.service.synchronize;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.plugins.cardinal.io.WebDataProjectManager;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Project;
import eu.geopaparazzi.core.utilities.Constants;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.util.GPDialogs;

public class CloudSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = CloudSyncAdapter.class.getSimpleName();

    public CloudSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        CloudSyncManager.init(this.getContext());
    }

    public CloudSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        CloudSyncManager.init(this.getContext());
    }


    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        try {
            //Beginning sync operation
            Log.d(TAG, "Beginning sync operation");

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
            final String pwd = preferences.getString(Constants.PREF_KEY_PWD, ""); //$NON-NLS-1$
            final String serverUrl = preferences.getString(Constants.PREF_KEY_SERVER, ""); //$NON-NLS-1$
            boolean uploadImages = preferences.getBoolean("REFS_KEY_UPLOAD_CLOUD_IMAGES", true);
            AppContainer appContainer = ((CardinalApplication) CardinalApplication.getInstance()).getContainer();
            Project project = appContainer.getProjectActive();
            String result = WebDataProjectManager.INSTANCE.uploadProject(this.getContext(), serverUrl, account.name, pwd, project.getId(), uploadImages);

            Log.d(TAG, "Sync result --> " + result);
            GPDialogs.toast(this.getContext(), result, 1);


        } catch (Exception e) {
            GPLog.error(this, null, e);
            e.printStackTrace();
        }
    }
}
