package cu.phibrain.cardinal.app.ui.service.synchronize;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SyncResult;
import android.database.CursorWindow;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import java.lang.reflect.Field;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.plugins.cardinal.io.WebDataProjectManager;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Project;
import eu.geopaparazzi.core.utilities.Constants;
import eu.geopaparazzi.library.database.GPLog;

public class CloudSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String TAG = CloudSyncAdapter.class.getSimpleName();
    private final AccountManager mAccountManager;

    public CloudSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        CloudSyncManager.init(context);
        mAccountManager = AccountManager.get(context);
    }

    public CloudSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        CloudSyncManager.init(context);
        mAccountManager = AccountManager.get(context);
    }


    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        try {
            //Beginning sync operation
            Log.d(TAG, "Beginning sync operation");

            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 160 * 1024 * 1024); //the 160MB is the new size


            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.getContext());
            final String pwd = preferences.getString(Constants.PREF_KEY_PWD, ""); //$NON-NLS-1$
            final String serverUrl = preferences.getString(Constants.PREF_KEY_SERVER, ""); //$NON-NLS-1$
            boolean uploadImages = preferences.getBoolean("REFS_KEY_UPLOAD_CLOUD_IMAGES", true);
            AppContainer appContainer = ((CardinalApplication) CardinalApplication.getInstance()).getContainer();
            Project project = appContainer.getProjectActive();
            String result = WebDataProjectManager.INSTANCE.uploadProject(this.getContext(), serverUrl, account.name, pwd, project.getId(), uploadImages);

            Log.d(TAG, "Sync result --> " + result);



        } catch (Exception e) {
            GPLog.error(this, null, e);
            e.printStackTrace();
        }
    }
}
