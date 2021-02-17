package cu.phibrain.plugins.cardinal.io.importer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import cu.phibrain.plugins.cardinal.io.R;

import eu.geopaparazzi.core.utilities.Constants;
import eu.geopaparazzi.library.network.NetworkUtilities;
import eu.geopaparazzi.library.plugin.PluginService;
import eu.geopaparazzi.library.plugin.types.MenuEntry;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.IActivitySupporter;

/**
 * @author Erodis Pérez Michel  (eperezm1986@gmail.com)
 */
public class CardinalProjectImportMenuEntry extends MenuEntry {
    PluginService service = null;
    public CardinalProjectImportMenuEntry(PluginService service) {
        this.service = service;
    }

    @Override
    public String getLabel() {
        return this.service.getResources().getString(R.string.cardinal_online);

    }

    @Override
    public void onClick(IActivitySupporter clickActivityStarter) {
        if (processOnClick(clickActivityStarter)) {
            Intent intent = new Intent(clickActivityStarter.getContext(), CardinalProjectImporterActivity.class);
            clickActivityStarter.startActivity(intent);
        }
    }

    protected boolean processOnClick(IActivitySupporter starter) {
        Context context = starter.getContext();
        if (!NetworkUtilities.isNetworkAvailable(context)) {
            GPDialogs.infoDialog(context, context.getString(R.string.available_only_with_network), null);
            return false;
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String user = preferences.getString(Constants.PREF_KEY_USER, "geopaparazziuser"); //$NON-NLS-1$
        final String pwd = preferences.getString(Constants.PREF_KEY_PWD, "geopaparazzipwd"); //$NON-NLS-1$
        final String url = preferences.getString(Constants.PREF_KEY_SERVER, ""); //$NON-NLS-1$

        if (url.length() == 0 || user.length() == 0 || pwd.length() == 0) {
            GPDialogs.infoDialog(context, context.getString(R.string.error_set_cloud_settings_cardinal), null);
            return false;
        }
        return true;
    }
}
