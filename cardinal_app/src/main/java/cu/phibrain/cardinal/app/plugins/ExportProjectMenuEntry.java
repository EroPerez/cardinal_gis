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


package cu.phibrain.cardinal.app.plugins;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.cardinal.app.R;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.cardinal.app.plugins.dialogs.ProjectExportDialogFragment;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Project;
import eu.geopaparazzi.core.utilities.Constants;
import eu.geopaparazzi.library.network.NetworkUtilities;
import eu.geopaparazzi.library.plugin.types.MenuEntry;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.IActivitySupporter;

/**
 * @author Erodis Pérez Michel
 */
public class ExportProjectMenuEntry extends MenuEntry {

    private final Context serviceContext;

    private IActivitySupporter clickActivityStarter;

    public ExportProjectMenuEntry(Context context) {
        this.serviceContext = context;
    }

    @Override
    public String getLabel() {
        return serviceContext.getString(cu.phibrain.plugins.cardinal.io.R.string.cardinal_online);
    }

    @Override
    public int getOrder() {
        return 10000;
    }

    @Override
    public void onClick(final IActivitySupporter clickActivityStarter) {
        this.clickActivityStarter = clickActivityStarter;
        Context context = clickActivityStarter.getContext();


        if (!NetworkUtilities.isNetworkAvailable(context)) {
            GPDialogs.infoDialog(context, context.getString(eu.geopaparazzi.core.R.string.available_only_with_network), null);
            return;
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        final String user = preferences.getString(Constants.PREF_KEY_USER, ""); //$NON-NLS-1$
        final String pwd = preferences.getString(Constants.PREF_KEY_PWD, ""); //$NON-NLS-1$
        final String serverUrl = preferences.getString(Constants.PREF_KEY_SERVER, ""); //$NON-NLS-1$
        final boolean autoSync = preferences .getBoolean("REFS_KEY_AUTO_SYNC_SESSION", false);

        if(autoSync){
            GPDialogs.infoDialog(context, context.getString(R.string.auto_sync_activated), null);
            return;
        }

        long projectId = -1L;
        AppContainer appContainer = ((CardinalApplication) CardinalApplication.getInstance()).getContainer();

        try {
            Project currentProject = appContainer.getProjectActive();
            if (currentProject == null) {
                GPDialogs.infoDialog(context, context.getString(R.string.not_project_active), null);
                return;
            }

            projectId = currentProject.getId();

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (serverUrl.length() == 0 || user.length() == 0 || pwd.length() == 0 || projectId == -1L) {
            GPDialogs.infoDialog(context, context.getString(eu.geopaparazzi.core.R.string.error_set_cloud_settings), null);
            return;
        }

        final long finalProjectId = projectId;
        GPDialogs.yesNoMessageDialog(context, context.getString(eu.geopaparazzi.core.R.string.upload_to_cloud_prompt), new Runnable() {
            @Override
            public void run() {
                ProjectExportDialogFragment stageExportDialogFragment = ProjectExportDialogFragment.newInstance(serverUrl, user, pwd, finalProjectId);
                stageExportDialogFragment.show(clickActivityStarter.getSupportFragmentManager(), "cloud export");
            }
        }, null);
    }

}

