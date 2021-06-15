// MainActivityFragment.java
// Contains the Flag Quiz logic
package cu.phibrain.cardinal.app.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.cardinal.app.MapviewActivity;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.plugins.cardinal.io.R;

import eu.geopaparazzi.core.ui.activities.AboutActivity;
import eu.geopaparazzi.core.ui.activities.ExportActivity;
import eu.geopaparazzi.core.ui.activities.ImportActivity;
import eu.geopaparazzi.core.ui.activities.PanicActivity;
import eu.geopaparazzi.core.ui.activities.ProjectMetadataActivity;
import eu.geopaparazzi.core.ui.activities.SettingsActivity;
import eu.geopaparazzi.core.ui.dialogs.GpsInfoDialogFragment;
import eu.geopaparazzi.core.ui.dialogs.NewProjectDialogFragment;
import eu.geopaparazzi.core.ui.fragments.GeopaparazziActivityFragment;
import eu.geopaparazzi.core.utilities.IApplicationChangeListener;
import eu.geopaparazzi.library.core.ResourcesManager;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.gps.GpsLoggingStatus;
import eu.geopaparazzi.library.gps.GpsServiceStatus;
import eu.geopaparazzi.library.sensors.OrientationSensor;
import eu.geopaparazzi.library.util.AppsUtilities;
import eu.geopaparazzi.library.util.FileTypes;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.LibraryConstants;

/**
 * The fragment of the main geopap view.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class CardinalActivityFragment extends GeopaparazziActivityFragment {

    private final int RETURNCODE_BROWSE_FOR_NEW_PREOJECT = 665;
    private final int RETURNCODE_PROFILES = 666;

    private ImageButton mMetadataButton;
    private ImageButton mMapviewButton;
    private ImageButton mExportButton;

    private ImageButton mImportButton;

    private OrientationSensor mOrientationSensor;
    private IApplicationChangeListener appChangeListener;

    private BroadcastReceiver mGpsServiceBroadcastReceiver;
    private static boolean sCheckedGps = false;
    private GpsServiceStatus mLastGpsServiceStatus;
    private GpsLoggingStatus mLastGpsLoggingStatus = GpsLoggingStatus.GPS_DATABASELOGGING_OFF;
    private double[] mLastGpsPosition;
    private FloatingActionButton mPanicFAB;
    private ResourcesManager mResourcesManager;
    private boolean hasProfilesProvider = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_cardinal, container, false);
        return v; // return the fragment's view for display
    }


    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        mMetadataButton = view.findViewById(R.id.dashboardButtonMetadata);
        mMetadataButton.setOnClickListener(this);
        mMetadataButton.setOnLongClickListener(this);

        mMapviewButton = view.findViewById(R.id.dashboardButtonMapview);
        mMapviewButton.setOnClickListener(this);
        mMapviewButton.setOnLongClickListener(this);




        mImportButton = view.findViewById(R.id.dashboardButtonImport);
        mImportButton.setOnClickListener(this);
        mImportButton.setOnLongClickListener(this);

        mExportButton = view.findViewById(R.id.dashboardButtonExport);
        mExportButton.setOnClickListener(this);
        mExportButton.setOnLongClickListener(this);

        mPanicFAB = view.findViewById(R.id.panicActionButton);
        mPanicFAB.setOnClickListener(this);
        enablePanic(false);

    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_new) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                NewProjectDialogFragment newProjectDialogFragment = new NewProjectDialogFragment();
                newProjectDialogFragment.show(fragmentManager, "new project dialog");//NON-NLS
            }
            return true;
        } else if (i == R.id.action_load) {
            try {
                String title = getString(R.string.select_gpap_file);
                AppsUtilities.pickFile(this, RETURNCODE_BROWSE_FOR_NEW_PREOJECT, title, new String[]{FileTypes.GPAP.getExtension()}, null);
            } catch (Exception e) {
                GPLog.error(this, null, e);
            }
            return true;
        } else if (i == R.id.action_gps) {
            FragmentManager fragmentManager = getFragmentManager();
            if (fragmentManager != null) {
                GpsInfoDialogFragment gpsInfoDialogFragment = new GpsInfoDialogFragment();
                gpsInfoDialogFragment.show(fragmentManager, "gpsinfo dialog");//NON-NLS
            }
            return true;
        } else if (i == R.id.action_gpsstatus) {
            AppsUtilities.checkAndOpenGpsStatus(getActivity());
            return true;
        } else if (i == R.id.action_settings) {
            Intent preferencesIntent = new Intent(this.getActivity(), SettingsActivity.class);
            startActivity(preferencesIntent);
            return true;
        }  else if (i == R.id.action_about) {
            Intent intent = new Intent(getActivity(), AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (i == R.id.action_exit) {
            appChangeListener.onAppIsShuttingDown();
            FragmentActivity activity = getActivity();
            if (activity != null) {
                activity.finish();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        if (v == mMetadataButton) {
            try {
                Intent projectMetadataIntent = new Intent(getActivity(), ProjectMetadataActivity.class);
                startActivity(projectMetadataIntent);
            } catch (Exception e) {
                GPLog.error(this, null, e); //$NON-NLS-1$
            }
        } else if (v == mMapviewButton) {

            try {
                AppContainer appContainer = ((CardinalApplication)CardinalApplication.getInstance()).appContainer;
                appContainer.refreshProject();

                if(appContainer.ProjectActive == null){
                    GPDialogs.infoDialog(getContext(), getString(R.string.not_project_active), null);
                }
                else
                {
                    Intent importIntent = new Intent(getActivity(), MapviewActivity.class);
                    startActivity(importIntent);
                }
            } catch (IOException e) {
            e.printStackTrace();
        }

        } else if (v == mImportButton) {
            Intent importIntent = new Intent(getActivity(), ImportActivity.class);
            startActivity(importIntent);
        }  else if (v == mExportButton) {
            Intent exportIntent = new Intent(getActivity(), ExportActivity.class);
            startActivity(exportIntent);
        } else if (v == mPanicFAB) {
            if (mLastGpsPosition == null) {
                return;
            }

            Intent panicIntent = new Intent(getActivity(), PanicActivity.class);
            double lon = mLastGpsPosition[0];
            double lat = mLastGpsPosition[1];
            panicIntent.putExtra(LibraryConstants.LATITUDE, lat);
            panicIntent.putExtra(LibraryConstants.LONGITUDE, lon);
            startActivity(panicIntent);
        }

    }



    private void enablePanic(boolean enable) {
        if (enable) {
            mPanicFAB.show();
        } else {
            mPanicFAB.hide();
        }
    }

}
