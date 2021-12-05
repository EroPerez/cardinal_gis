package cu.phibrain.cardinal.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import net.danlew.android.joda.JodaTimeAndroid;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.config.ACRAConfiguration;
import org.acra.config.ACRAConfigurationException;
import org.acra.config.ConfigurationBuilder;
import org.json.JSONException;

import java.io.IOException;

import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.cardinal.app.ui.layer.CardinalLayerManager;
import cu.phibrain.plugins.cardinal.io.database.base.DaoSessionManager;
import cu.phibrain.plugins.cardinal.io.database.entity.model.DaoSession;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.ContractOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.DevicesOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.LabelBatchesOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.LabelMaterialOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.LabelOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.LabelSubLotOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.LayerOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjecTypeDefectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjecTypeOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjecTypeStateOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectHasDefectHasImagesOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectHasDefectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectHasStateOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectImagesOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectMetadataOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectTypeAttributeOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MaterialOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.NetworksOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.ProjectConfigOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.ProjectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.RouteSegmentOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.SignalEventsOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.StockOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.SupplierOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.TopologicalRuleOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.WorkSessionOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.WorkerOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.WorkerRouteOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.ZoneOperations;
import eu.geopaparazzi.core.GeopaparazziApplication;
import eu.geopaparazzi.library.core.ResourcesManager;
import eu.geopaparazzi.library.profiles.ProfilesHandler;


public class CardinalApplication extends GeopaparazziApplication {
    private DaoSession daoSession;
    private static Context context;
    protected AppContainer appContainer;

    public CardinalApplication() {
    }

    public static Context getContext() {
        return context;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            GeopaparazziApplication.mailTo = "eperezm1986@gmail.com";
            ACRAConfiguration config = new ConfigurationBuilder(this) //
                    .setMailTo(mailTo)//
                    .setCustomReportContent(//
                            ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, //
                            ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, //
                            ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT,
                            ReportField.AVAILABLE_MEM_SIZE, ReportField.SHARED_PREFERENCES) //
                    .setResToastText(eu.geopaparazzi.core.R.string.crash_toast_text)//
                    .setLogcatArguments("-t", "400", "-v", "time", "GPLOG:I", "*:S") //
                    .setReportingInteractionMode(ReportingInteractionMode.TOAST)//
                    .build();


            ACRA.init(this, config);
        } catch (ACRAConfigurationException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        try {
            reconstructContainer();
            ResourcesManager.resetManager();
            ProfilesHandler.INSTANCE.checkActiveProfile(getContentResolver());
            if (appContainer.getProjectActive() != null)
                CardinalLayerManager.INSTANCE.init();

            Log.i("GEOPAPARAZZIAPPLICATION", "ACRA Initialized.");

            // It's important to initialize the ResourceZoneInfoProvider; otherwise
            // joda-time-android will not work.
            JodaTimeAndroid.init(this);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void doRestart(Context c) {
        try {
            //check if the context is given
            if (c != null) {
                //fetch the packagemanager so we can get the default launch activity
                // (you can replace this intent with any other activity if you want
                PackageManager pm = c.getPackageManager();
                //check if we got the PackageManager
                if (pm != null) {
                    //create the intent with the default start activity for your application
                    Intent mStartActivity = pm.getLaunchIntentForPackage(
                            c.getPackageName()
                    );
                    if (mStartActivity != null) {
                        mStartActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //create a pending intent so the application is restarted after System.exit(0) was called.
                        // We use an AlarmManager to call this intent in 100ms
                        int mPendingIntentId = 223344;
                        PendingIntent mPendingIntent = PendingIntent
                                .getActivity(c, mPendingIntentId, mStartActivity,
                                        PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager) c.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        //kill the application
                        System.exit(0);
                    } else {
                        Log.e("GEOPAPARAZZIAPPLICATION", "Was not able to restart application, mStartActivity null");
                    }
                } else {
                    Log.e("GEOPAPARAZZIAPPLICATION", "Was not able to restart application, PM null");
                }
            } else {
                Log.e("GEOPAPARAZZIAPPLICATION", "Was not able to restart application, Context null");
            }
        } catch (Exception ex) {
            Log.e("GEOPAPARAZZIAPPLICATION", "Was not able to restart application");

        }
    }

    public AppContainer getContainer() {
        return this.appContainer;
    }

    public void reconstructContainer() throws IOException {
        DaoSessionManager.getInstance().setContext(context);
        DaoSessionManager.getInstance().setDatabaseName(this.getDatabase().getPath());
        daoSession = DaoSessionManager.getInstance().getDaoSession();

        //Attach observer to subject
        ProjectOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        WorkerOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        ContractOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        StockOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        ZoneOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        NetworksOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        LayerOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        MapObjecTypeOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        MapObjectTypeAttributeOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        MapObjecTypeDefectOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        MapObjecTypeStateOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        LabelBatchesOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        LabelMaterialOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        LabelSubLotOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        MapObjectHasDefectHasImagesOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        MapObjectHasDefectOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        MapObjectHasStateOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        MapObjectImagesOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        MapObjectMetadataOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        MapObjectOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        MaterialOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        RouteSegmentOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        SignalEventsOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        SupplierOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        TopologicalRuleOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        WorkerRouteOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        WorkSessionOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        LabelOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        ProjectConfigOperations.getInstance().attachTo(DaoSessionManager.getInstance());
        DevicesOperations.getInstance().attachTo(DaoSessionManager.getInstance());

        this.appContainer = new AppContainer();
    }
}
