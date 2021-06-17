// MainActivityFragment.java
// Contains the Flag Quiz logic
package cu.phibrain.cardinal.app.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.system.Os;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.cardinal.app.MapviewActivity;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.plugins.cardinal.io.R;
import eu.geopaparazzi.core.GeopaparazziApplication;
import eu.geopaparazzi.core.database.DaoMetadata;
import eu.geopaparazzi.core.database.objects.Metadata;
import eu.geopaparazzi.core.ui.activities.AboutActivity;
import eu.geopaparazzi.core.ui.activities.ExportActivity;
import eu.geopaparazzi.core.ui.activities.ImportActivity;
import eu.geopaparazzi.core.ui.activities.PanicActivity;
import eu.geopaparazzi.core.ui.activities.ProjectMetadataActivity;
import eu.geopaparazzi.core.ui.activities.SettingsActivity;
import eu.geopaparazzi.core.ui.dialogs.GpsInfoDialogFragment;
import eu.geopaparazzi.core.ui.dialogs.NewProjectDialogFragment;
import eu.geopaparazzi.core.ui.fragments.GeopaparazziActivityFragment;
import eu.geopaparazzi.core.utilities.Constants;
import eu.geopaparazzi.core.utilities.IApplicationChangeListener;
import eu.geopaparazzi.library.GPApplication;
import eu.geopaparazzi.library.core.ResourcesManager;
import eu.geopaparazzi.library.database.DefaultHelperClasses;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.database.GPLogPreferencesHandler;
import eu.geopaparazzi.library.database.TableDescriptions;
import eu.geopaparazzi.library.gps.GpsLoggingStatus;
import eu.geopaparazzi.library.gps.GpsServiceStatus;
import eu.geopaparazzi.library.gps.GpsServiceUtilities;
import eu.geopaparazzi.library.sensors.OrientationSensor;
import eu.geopaparazzi.library.style.ColorUtilities;
import eu.geopaparazzi.library.util.AppsUtilities;
import eu.geopaparazzi.library.util.CompressionUtilities;
import eu.geopaparazzi.library.util.FileTypes;
import eu.geopaparazzi.library.util.FileUtilities;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.LibraryConstants;
import eu.geopaparazzi.library.util.TextAndBooleanRunnable;
import eu.geopaparazzi.library.util.TimeUtilities;

import static eu.geopaparazzi.library.util.LibraryConstants.MAPSFORGE_EXTRACTED_DB_NAME;

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
    private ImageButton mGpslogButton;
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
        // this fragment adds to the menu
        setHasOptionsMenu(true);
        FragmentActivity activity = getActivity();
        if (activity != null)
            for (PackageInfo pack : activity.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS)) {
                ProviderInfo[] providers = pack.providers;
                if (providers != null) {
                    for (ProviderInfo provider : providers) {
                        String authority = provider.authority;
                        if (authority != null && authority.equals("eu.geopaparazzi.provider.profiles")) {//NON-NLS
                            hasProfilesProvider = true;
                        }
                    }
                }
            }

        try {
            initializeResourcesManager();

            // start gps service
            GpsServiceUtilities.startGpsService(getActivity());
        } catch (Exception e) {
            e.printStackTrace();

            GPLog.error(this, null, e);
        }
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

        mGpslogButton = view.findViewById(eu.geopaparazzi.core.R.id.dashboardButtonGpslog);
        mGpslogButton.setOnClickListener(this);
        mGpslogButton.setOnLongClickListener(this);


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

                if (appContainer.ProjectActive == null /*|| appContainer.WorkSessionActive == null*/) {
                    GPDialogs.infoDialog(getContext(), getString(R.string.not_project_active), null);
                } else {
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
        } else if (v == mGpslogButton) {
            handleGpsLogAction();
        }

    }


    private void enablePanic(boolean enable) {
        if (enable) {
            mPanicFAB.show();
        } else {
            mPanicFAB.hide();
        }
    }

    private void initializeResourcesManager() throws Exception {
        mResourcesManager = ResourcesManager.getInstance(getContext());

        final FragmentActivity activity = getActivity();
        if (activity != null)
            if (mResourcesManager == null) {
                GPDialogs.yesNoMessageDialog(activity, getString(eu.geopaparazzi.core.R.string.no_sdcard_use_internal_memory),
                        new Runnable() {
                            public void run() {
                                ResourcesManager.setUseInternalMemory(true);
                                try {
                                    mResourcesManager = ResourcesManager.getInstance(getContext());
                                    initIfOk();
                                } catch (Exception e) {
                                    GPLog.error(this, null, e); //$NON-NLS-1$
                                }
                            }
                        }, activity::finish
                );
            } else {
                // create the default mapsforge data extraction db
                File applicationSupporterDir = mResourcesManager.getApplicationSupporterDir();
                File newDbFile = new File(applicationSupporterDir, MAPSFORGE_EXTRACTED_DB_NAME);
                if (!newDbFile.exists()) {
                    AssetManager assetManager = activity.getAssets();
                    InputStream inputStream = assetManager.open(MAPSFORGE_EXTRACTED_DB_NAME);
                    FileUtilities.copyFile(inputStream, new FileOutputStream(newDbFile));
                }
                // initialize rest of resources
                initIfOk();
            }
    }

    private void initIfOk() {
        final FragmentActivity activity = getActivity();
        if (activity == null)
            return;
        if (mResourcesManager == null) {
            GPDialogs.warningDialog(activity, getString(eu.geopaparazzi.core.R.string.sdcard_notexist), activity::finish);
            return;
        }


        try {
            File applicationDir = ResourcesManager.getInstance(activity).getApplicationSupporterDir();
            File projFolder = new File(applicationDir, "proj");//NON-NLS
            File projFolderWithDate = new File(applicationDir, "proj20190708"); // to keep versions//NON-NLS
            File projDbFile = new File(projFolderWithDate, "proj.db");//NON-NLS
            if (!projDbFile.exists()) {
                GPLog.addLogEntry("Proj 6 folder doesn't exist: " + projFolderWithDate.getAbsolutePath());//NON-NLS
                File zipFile = new File(applicationDir, "proj.zip");//NON-NLS
                AssetManager assetManager = activity.getAssets();
                InputStream inputStream = assetManager.open("proj.zip");//NON-NLS
                FileUtilities.copyFile(inputStream, new FileOutputStream(zipFile));
                GPLog.addLogEntry("Copied proj defs from asset to: " + zipFile.getAbsolutePath());//NON-NLS
                try {
                    CompressionUtilities.unzipFolder(zipFile.getAbsolutePath(), applicationDir.getAbsolutePath(), false);
                    projFolder.renameTo(projFolderWithDate);
                    GPLog.addLogEntry("Uncompressed and renamed to: " + projFolderWithDate.getAbsolutePath());//NON-NLS
                } finally {
                    zipFile.delete();
                }

            }
            Os.setenv("PROJ_LIB", projFolderWithDate.getAbsolutePath(), true);//NON-NLS
        } catch (Exception e) {
            e.printStackTrace();
        }


        /*
         * check the logging system
         */
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        GPLogPreferencesHandler.checkLog(preferences);
        GPLogPreferencesHandler.checkLogHeavy(preferences);
        GPLogPreferencesHandler.checkLogAbsurd(preferences);

        checkLogButton();

        // check for screen on
        boolean keepScreenOn = preferences.getBoolean(Constants.PREFS_KEY_SCREEN_ON, false);
        if (keepScreenOn) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }

        try {
            GeopaparazziApplication.getInstance().getDatabase();

            // Set the project name in the metadata, if not already available
            List<Metadata> projectMetadata = DaoMetadata.getProjectMetadata();
            for (Metadata metadata : projectMetadata) {
                if (metadata.key.equals(TableDescriptions.MetadataTableDefaultValues.KEY_NAME.getFieldName())) {
                    String projectName = metadata.value;
                    if (projectName.length() == 0) {
                        File dbFile = mResourcesManager.getDatabaseFile();
                        String dbName = FileUtilities.getNameWithoutExtention(dbFile);
                        DaoMetadata.setValue(metadata.key, dbName);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), e.getLocalizedMessage(), e);
            GPDialogs.toast(activity, eu.geopaparazzi.core.R.string.databaseError, Toast.LENGTH_LONG);
        }
    }

    private void checkLogButton() {
        if (mGpslogButton != null) {
            Context context = getContext();
            if (context != null)
                if (mLastGpsLoggingStatus == GpsLoggingStatus.GPS_DATABASELOGGING_ON) {
                    mGpslogButton.setBackgroundColor(ColorUtilities.getAccentColor(context));
                } else {
                    mGpslogButton.setBackgroundColor(ColorUtilities.getPrimaryColor(context));
                }
        }
    }

    private void handleGpsLogAction() {
        final GPApplication appContext = GeopaparazziApplication.getInstance();
        final FragmentActivity activity = getActivity();
        if (activity == null)
            return;
        if (mLastGpsLoggingStatus == GpsLoggingStatus.GPS_DATABASELOGGING_ON) {
            GPDialogs.yesNoMessageDialog(getActivity(), getString(eu.geopaparazzi.core.R.string.do_you_want_to_stop_logging),
                    () -> activity.runOnUiThread(() -> {
                        // stop logging
                        GpsServiceUtilities.stopDatabaseLogging(appContext);
                        mGpslogButton.setBackgroundColor(ColorUtilities.getPrimaryColor(activity));
                        GpsServiceUtilities.triggerBroadcast(getActivity());
                    }), null
            );

        } else {
            // start logging
            if (mLastGpsServiceStatus == GpsServiceStatus.GPS_FIX) {
                final String defaultLogName = "log_" + TimeUtilities.INSTANCE.TIMESTAMPFORMATTER_LOCAL.format(new Date()); //$NON-NLS-1$

                GPDialogs.inputMessageAndCheckboxDialog(getActivity(), getString(eu.geopaparazzi.core.R.string.gps_log_name),
                        defaultLogName, getString(eu.geopaparazzi.core.R.string.continue_last_log), false, new TextAndBooleanRunnable() {
                            public void run() {
                                activity.runOnUiThread(() -> {
                                    String newName = theTextToRunOn;
                                    if (newName == null || newName.length() < 1) {
                                        newName = defaultLogName;
                                    }

                                    mGpslogButton.setBackgroundColor(ColorUtilities.getAccentColor(activity));
                                    GpsServiceUtilities.startDatabaseLogging(appContext, newName, theBooleanToRunOn,
                                            DefaultHelperClasses.GPSLOG_HELPER_CLASS);
                                    GpsServiceUtilities.triggerBroadcast(getActivity());
                                });
                            }
                        }
                );

            } else {
                GPDialogs.warningDialog(getActivity(), getString(eu.geopaparazzi.core.R.string.gpslogging_only), null);
            }
        }
    }
}
