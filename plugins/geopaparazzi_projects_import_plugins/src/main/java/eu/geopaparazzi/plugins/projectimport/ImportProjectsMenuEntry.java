/*
 * Geopaparazzi - Digital field mapping on Android based devices
 * Copyright (C) 2016  HydroloGIS (www.hydrologis.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.geopaparazzi.plugins.projectimport;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import eu.geopaparazzi.core.utilities.Constants;
import eu.geopaparazzi.library.network.NetworkUtilities;
import eu.geopaparazzi.library.plugin.types.MenuEntry;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.IActivitySupporter;
import eu.geopaparazzi.library.util.LibraryConstants;
import eu.geopaparazzi.library.webproject.WebProjectsListActivity;

/**
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class ImportProjectsMenuEntry extends MenuEntry {

    private final Context serviceContext;
    private IActivitySupporter clickActivityStarter;

    public ImportProjectsMenuEntry(Context context) {
        this.serviceContext = context;
    }

    @Override
    public String getLabel() {
        return serviceContext.getString(eu.geopaparazzi.core.R.string.geopap_browser);
    }

    @Override
    public int getOrder() {
        return 10000;
    }

    @Override
    public void onClick(IActivitySupporter clickActivityStarter) {
        this.clickActivityStarter = clickActivityStarter;
        Context context = clickActivityStarter.getContext();


        if (!NetworkUtilities.isNetworkAvailable(context)) {
            GPDialogs.infoDialog(context, context.getString(eu.geopaparazzi.core.R.string.available_only_with_network), null);
            return;
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String user = preferences.getString(Constants.PREF_KEY_USER, "geopaparazziuser"); //$NON-NLS-1$
        final String passwd = preferences.getString(Constants.PREF_KEY_PWD, "geopaparazzipwd"); //$NON-NLS-1$
        final String server = preferences.getString(Constants.PREF_KEY_SERVER, ""); //$NON-NLS-1$

        if (server.length() == 0) {
            GPDialogs.infoDialog(context, context.getString(eu.geopaparazzi.core.R.string.error_set_cloud_settings), null);
            return;
        }

        Intent webImportIntent = new Intent(context, WebProjectsListActivity.class);
        webImportIntent.putExtra(LibraryConstants.PREFS_KEY_URL, server);
        webImportIntent.putExtra(LibraryConstants.PREFS_KEY_USER, user);
        webImportIntent.putExtra(LibraryConstants.PREFS_KEY_PWD, passwd);
        clickActivityStarter.startActivity(webImportIntent);
    }

}
