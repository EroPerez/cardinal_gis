package cu.phibrain.cardinal.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.CursorWindow;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

import java.io.IOException;
import java.lang.reflect.Field;

import cu.phibrain.cardinal.app.ui.fragment.CardinalActivityFragment;
import cu.phibrain.cardinal.app.ui.permissions.PermissionCamera;
import eu.geopaparazzi.core.GeopaparazziCoreActivity;
import eu.geopaparazzi.core.database.DaoBookmarks;
import eu.geopaparazzi.library.core.ResourcesManager;
import eu.geopaparazzi.library.database.GPLog;
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

public class CardinalActivity extends GeopaparazziCoreActivity {
    private AChainedPermissionHelper permissionHelper = new PermissionWriteStorage();
    private CardinalActivityFragment cardinalActivityFragment;
    public static final int DOWNLOADDATA_RETURN_CODE = 667;

    // configure the GeopaparazziCoreActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        permissionHelper = new PermissionWriteStorage();
        permissionHelper.add(new PermissionFineLocation()).add(new PermissionForegroundService()).add(new PermissionCamera());

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

        setContentView(R.layout.activity_cardinal);
        Toolbar toolbar = findViewById(cu.phibrain.cardinal.app.R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024); //the 100MB is the new size
        } catch (Exception e) {
            e.printStackTrace();
            GPLog.error(this, null, e);
        }


    }


    private void init() {

        // set default values in the app's SharedPreferences
        PreferenceManager.setDefaultValues(this, eu.geopaparazzi.core.R.xml.preferences, false);
        cardinalActivityFragment = new CardinalActivityFragment();
        FragmentTransaction transaction =
                getSupportFragmentManager().beginTransaction();
        transaction.replace(cu.phibrain.cardinal.app.R.id.cardinalFragmentContainer, cardinalActivityFragment);
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
                GPDialogs.yesNoMessageDialog(this, getString(eu.geopaparazzi.core.R.string.import_bookmark_prompt), new Runnable() {
                    @Override
                    public void run() {
                        CardinalActivity activity = CardinalActivity.this;
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
                                            getString(eu.geopaparazzi.core.R.string.unable_parse_url) + path,
                                            null);
                        }
                    }
                }, null);

            }

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
            GPDialogs.infoDialog(this, getString(eu.geopaparazzi.core.R.string.premissions_cant_start) + permissionHelper.getDescription(), new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
        }
    }

    @Override
    public void onApplicationNeedsRestart() {

        if (cardinalActivityFragment != null && cardinalActivityFragment.getGpsServiceBroadcastReceiver() != null) {
            GpsServiceUtilities.stopDatabaseLogging(this);
            GpsServiceUtilities.stopGpsService(this);
            try {
                GpsServiceUtilities.unregisterFromBroadcasts(this, cardinalActivityFragment.getGpsServiceBroadcastReceiver());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            CardinalApplication.getInstance().closeDatabase();
            ResourcesManager.resetManager();
            try {
                ((CardinalApplication) CardinalApplication.getInstance()).reconstructContainer();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                recreate();
            }
        }, 10);
    }


}
