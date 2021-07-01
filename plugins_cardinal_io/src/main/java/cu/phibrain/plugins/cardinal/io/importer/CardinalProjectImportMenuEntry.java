package cu.phibrain.plugins.cardinal.io.importer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import cu.phibrain.plugins.cardinal.io.R;
import eu.geopaparazzi.core.utilities.Constants;
import eu.geopaparazzi.core.utilities.IApplicationChangeListener;
import eu.geopaparazzi.library.network.NetworkUtilities;
import eu.geopaparazzi.library.plugin.types.MenuEntry;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.IActivitySupporter;
import eu.geopaparazzi.library.util.LibraryConstants;

/**
 * @author Erodis Pérez Michel  (eperezm1986@gmail.com)
 */
public class CardinalProjectImportMenuEntry extends MenuEntry {
    private final Context serviceContext;
    private IActivitySupporter clickActivityStarter;
    public CardinalProjectImportMenuEntry(Context context) {
        this.serviceContext = context;
    }

    @Override
    public String getLabel() {
        return this.serviceContext.getResources().getString(R.string.cardinal_online);

    }
    @Override
    public int getOrder() {
        return 10000;
    }
    @Override
    public void onClick(IActivitySupporter clickActivityStarter) {
        this.clickActivityStarter = clickActivityStarter;
        Context context = clickActivityStarter.getContext();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String user = preferences.getString(Constants.PREF_KEY_USER, "geopaparazziuser"); //$NON-NLS-1$
        final String passwd = preferences.getString(Constants.PREF_KEY_PWD, "geopaparazzipwd"); //$NON-NLS-1$
        final String server = preferences.getString(Constants.PREF_KEY_SERVER, ""); //$NON-NLS-1$

        if (processOnClick(user, passwd, server)) {
            Intent webImportIntent = new Intent(clickActivityStarter.getContext(), CardinalProjectImporterActivity.class);

            webImportIntent.putExtra(LibraryConstants.PREFS_KEY_URL, server);
            webImportIntent.putExtra(LibraryConstants.PREFS_KEY_USER, user);
            webImportIntent.putExtra(LibraryConstants.PREFS_KEY_PWD, passwd);
            clickActivityStarter.startActivityForResult(webImportIntent, requestCode);
        }
    }

    protected boolean processOnClick(String user, String passwd, String server) {
        Context context = clickActivityStarter.getContext();
        if (!NetworkUtilities.isNetworkAvailable(context)) {
            GPDialogs.infoDialog(context, context.getString(R.string.available_only_with_network), null);
            return false;
        }



        if (server.length() == 0 || user.length() == 0 || passwd.length() == 0) {
            GPDialogs.infoDialog(context, context.getString(R.string.error_set_cloud_settings_cardinal), null);
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResultExecute(int requestCode, int resultCode, Intent data) {
        Context context = clickActivityStarter.getContext();
        Log.e("GEOPAPARAZZIAPPLICATION", "Was not able to restart application, mStartActivity null");
        if (context instanceof IApplicationChangeListener) {
            ((IApplicationChangeListener) context).onApplicationNeedsRestart();
            Log.e("GEOPAPARAZZIAPPLICATION", "Was not able to restart application, mStartActivity null");
        }
    }
}
