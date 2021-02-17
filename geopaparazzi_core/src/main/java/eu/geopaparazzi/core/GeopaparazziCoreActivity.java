// MapLayerListActivity.java
// Hosts the GeopaparazziActivityFragment on a phone and both the
// GeopaparazziActivityFragment and SettingsActivityFragment on a tablet
package eu.geopaparazzi.core;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.KeyEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;

import eu.geopaparazzi.core.database.DaoBookmarks;
import eu.geopaparazzi.core.mapview.MapviewActivity;
import eu.geopaparazzi.core.ui.fragments.GeopaparazziActivityFragment;
import eu.geopaparazzi.core.utilities.IApplicationChangeListener;
import eu.geopaparazzi.library.core.ResourcesManager;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.forms.TagsManager;
import eu.geopaparazzi.library.gps.GpsServiceUtilities;
import eu.geopaparazzi.library.permissions.AChainedPermissionHelper;
import eu.geopaparazzi.library.permissions.PermissionFineLocation;
import eu.geopaparazzi.library.permissions.PermissionForegroundService;
import eu.geopaparazzi.library.permissions.PermissionWriteStorage;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.LibraryConstants;
import eu.geopaparazzi.library.util.PositionUtilities;
import eu.geopaparazzi.library.util.SimplePosition;
import eu.geopaparazzi.library.util.UrlUtilities;

import static eu.geopaparazzi.library.util.LibraryConstants.PREFS_KEY_DATABASE_TO_LOAD;

/**
 * Main activity.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class GeopaparazziCoreActivity extends AppCompatActivity implements IApplicationChangeListener {
    private AChainedPermissionHelper permissionHelper = new PermissionWriteStorage();
    private GeopaparazziActivityFragment geopaparazziActivityFragment;

    // configure the GeopaparazziCoreActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissionHelper = new PermissionWriteStorage();
        permissionHelper.add(new PermissionFineLocation()).add(new PermissionForegroundService());

        // PERMISSIONS START
        if (permissionHelper.hasPermission(this) && permissionHelper.getNextWithoutPermission(this) == null) {
            completeInit();
        } else {
            if (permissionHelper.hasPermission(this)) {
                permissionHelper = permissionHelper.getNextWithoutPermission(this);
            }
            permissionHelper.requestPermission(this);
        }
        // PERMISSIONS STOP

        setContentView(R.layout.activity_geopaparazzi);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    private void checkAvailableProfiles() {
////        try {
////            ProfilesHandler.INSTANCE.checkActiveProfile(getContentResolver());
//            BaseMapSourcesManager.INSTANCE.forceBasemapsreRead();
//            SpatialiteSourcesManager.INSTANCE.forceSpatialitemapsreRead();
////        } catch (JSONException e) {
////            e.printStackTrace();
////        }
    }

    private void init() {

        // set default values in the app's SharedPreferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        geopaparazziActivityFragment = new GeopaparazziActivityFragment();
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragmentContainer, geopaparazziActivityFragment);
        transaction.commitAllowingStateLoss();
    }

    private void checkIncomingProject() {
        Uri data = getIntent().getData();
        if (data != null) {
            String path = data.getEncodedPath();
            if (path.endsWith(LibraryConstants.GEOPAPARAZZI_DB_EXTENSION)) {
                final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(PREFS_KEY_DATABASE_TO_LOAD, path);
                editor.apply();
            }
        }
    }

    private void checkIncomingUrl() {
        Uri data = getIntent().getData();
        if (data != null) {
            final String path = data.toString();
            // try osm
            final SimplePosition simplePosition = UrlUtilities.getLatLonTextFromOsmUrl(path);
            if (simplePosition.latitude != null) {
                GPDialogs.yesNoMessageDialog(this, getString(R.string.import_bookmark_prompt), new Runnable() {
                    @Override
                    public void run() {
                        GeopaparazziCoreActivity activity = GeopaparazziCoreActivity.this;
                        try {
                            DaoBookmarks.addBookmark(simplePosition.longitude, simplePosition.latitude, simplePosition.text, simplePosition.zoomLevel);
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
                            PositionUtilities.putMapCenterInPreferences(preferences, simplePosition.longitude, simplePosition.latitude, 16);
                            Intent mapIntent = new Intent(activity, MapviewActivity.class);
                            startActivity(mapIntent);
                        } catch (IOException e) {
                            GPLog.error(this, "Error parsing URI: " + path, e); //$NON-NLS-1$
                            GPDialogs
                                    .warningDialog(
                                            activity,
                                            getString(R.string.unable_parse_url) + path,
                                            null);
                        }
                    }
                }, null);

            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        if (permissionHelper.hasGainedPermission(requestCode, grantResults)) {
            AChainedPermissionHelper nextWithoutPermission = permissionHelper.getNextWithoutPermission(this);
            permissionHelper = nextWithoutPermission;
            if (permissionHelper == null) {
                completeInit();
            } else {
                permissionHelper.requestPermission(this);
            }

        } else {
            GPDialogs.infoDialog(this, getString(R.string.premissions_cant_start) + permissionHelper.getDescription(), new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }
    }

    private void completeInit() {
        try {
            ResourcesManager.getInstance(this);
        } catch (Exception e) {
            GPLog.error(this, "Error", e); //NON-NLS
        }
        checkIncomingProject();
        init();
        checkIncomingUrl();
        checkAvailableProfiles();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // force to exit through the exit button
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onApplicationNeedsRestart() {

        if (geopaparazziActivityFragment != null && geopaparazziActivityFragment.getGpsServiceBroadcastReceiver() != null) {
            GpsServiceUtilities.stopDatabaseLogging(this);
            GpsServiceUtilities.stopGpsService(this);
            GpsServiceUtilities.unregisterFromBroadcasts(this, geopaparazziActivityFragment.getGpsServiceBroadcastReceiver());
            GeopaparazziApplication.getInstance().closeDatabase();
            ResourcesManager.resetManager();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recreate();
            }
        }, 10);
    }

    @Override
    public void onAppIsShuttingDown() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putFloat(MapviewActivity.MAPSCALE_X, 1);
        edit.putFloat(MapviewActivity.MAPSCALE_Y, 1);
        edit.apply();


        GpsServiceUtilities.stopDatabaseLogging(this);
        GpsServiceUtilities.stopGpsService(this);

        TagsManager.reset();
        GeopaparazziApplication.reset();
    }

}
