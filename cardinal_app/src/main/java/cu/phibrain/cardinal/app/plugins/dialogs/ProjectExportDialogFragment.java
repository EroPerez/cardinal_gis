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

package cu.phibrain.cardinal.app.plugins.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.fragment.app.DialogFragment;

import cu.phibrain.plugins.cardinal.io.WebDataProjectManager;
import eu.geopaparazzi.core.R;
import eu.geopaparazzi.library.database.GPLog;


/**
 * Dialog for export to cloud (PROJECT).
 *
 * @author Erodis Pérez Michel
 */
public class ProjectExportDialogFragment extends DialogFragment {
    public static final String NODATA = "NODATA";//NON-NLS

    public static final String KEY_URL = "KEY_URL";//NON-NLS
    public static final String KEY_USER = "KEY_USER";//NON-NLS
    public static final String KEY_PWD = "KEY_PWD";//NON-NLS
    public static final String KEY_PRO = "KEY_PRO";//NON-NLS

    private ProgressBar progressBar;

    private AlertDialog alertDialog;
    private Button positiveButton;

    private String serverUrl;
    private String user;
    private String pwd;
    private long projectId;

    /**
     * Create a dialog instance.
     *
     * @param serverUrl the server url.
     * @param user      the username for the server.
     * @param pwd       the password.
     * @return the instance.
     */
    public static ProjectExportDialogFragment newInstance(final String serverUrl, final String user, final String pwd, long pro) {
        ProjectExportDialogFragment f = new ProjectExportDialogFragment();
        Bundle args = new Bundle();
        args.putString(KEY_URL, serverUrl);
        args.putString(KEY_USER, user);
        args.putString(KEY_PWD, pwd);
        args.putLong(KEY_PRO, pro);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        serverUrl = getArguments().getString(KEY_URL);
        user = getArguments().getString(KEY_USER);
        pwd = getArguments().getString(KEY_PWD);
        projectId = getArguments().getLong(KEY_PRO);

    }

    @Override
    public Dialog onCreateDialog(Bundle bundle) {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
        View gpsinfoDialogView = getActivity().getLayoutInflater().inflate(
                R.layout.fragment_dialog_progressbar, null);
        builder.setView(gpsinfoDialogView);
        builder.setMessage(R.string.exporting_data_to_the_cloud);

        progressBar = gpsinfoDialogView.findViewById(
                R.id.progressBar);

        builder.setPositiveButton(android.R.string.ok,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }
        );

        progressBar.setIndeterminate(true);

        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        return alertDialog;
    }

    private void startExport() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean uploadImages = preferences.getBoolean("REFS_KEY_UPLOAD_CLOUD_IMAGES", false);

        new AsyncTask<String, Void, String>() {
            protected String doInBackground(String... params) {
                try {
                    String message = WebDataProjectManager.INSTANCE.uploadProject(getActivity(), serverUrl, user, pwd, projectId, uploadImages);
                    return message;
                } catch (Exception e) {
                    GPLog.error(this, e.getLocalizedMessage(), e);
                    return "ERROR" + e.getLocalizedMessage();//NON-NLS
                }
            }

            protected void onPostExecute(String response) {
                alertDialog.setMessage(response);
                positiveButton.setEnabled(true);
            }
        }.execute((String) null);
    }

    public void onStart() {
        super.onStart();
        AlertDialog d = (AlertDialog) getDialog();
        if (d != null) {
            positiveButton = d.getButton(Dialog.BUTTON_POSITIVE);
            positiveButton.setEnabled(false);
        }
        startExport();
    }

    @Override
    public void onAttach(Activity activity) {

        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
