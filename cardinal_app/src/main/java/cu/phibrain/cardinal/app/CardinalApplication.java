package cu.phibrain.cardinal.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.acra.ACRA;
import org.acra.ReportField;
import org.acra.ReportingInteractionMode;
import org.acra.config.ACRAConfiguration;
import org.acra.config.ACRAConfigurationException;
import org.acra.config.ConfigurationBuilder;

import java.io.File;
import java.io.IOException;

import cu.phibrain.plugins.cardinal.io.database.base.DaoSessionManager;
import cu.phibrain.plugins.cardinal.io.database.entity.ContractOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.GroupOfLayerOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.LayerOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.MapObjecTypeDefectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.MapObjecTypeOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.MapObjecTypeStateOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.MapObjectTypeAttributeOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.ProjectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.StockOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.WorkerOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.ZoneOperations;
import cu.phibrain.plugins.cardinal.io.model.DaoSession;
import cu.phibrain.plugins.cardinal.io.ui.layer.CardinalLayerManager;
import eu.geopaparazzi.core.GeopaparazziApplication;
import eu.geopaparazzi.core.database.DatabaseManager;
import eu.geopaparazzi.library.GPApplication;
import eu.geopaparazzi.library.core.ResourcesManager;
import eu.geopaparazzi.library.profiles.ProfilesHandler;

@SuppressWarnings("ALL")
public class CardinalApplication extends GPApplication {
    private DaoSession daoSession;
    private static Context context;
    private static SQLiteDatabase database;
    public static String mailTo = "feedback@molanco.com";
    private DatabaseManager databaseManager;
    private File databaseFile;
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

            ACRAConfiguration config = new ConfigurationBuilder(this) //
                    .setMailTo(mailTo)//
                    .setCustomReportContent(//
                            ReportField.APP_VERSION_CODE, ReportField.APP_VERSION_NAME, //
                            ReportField.ANDROID_VERSION, ReportField.PHONE_MODEL, //
                            ReportField.CUSTOM_DATA, ReportField.STACK_TRACE, ReportField.LOGCAT) //
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
        ResourcesManager.resetManager();
        context = this;
        try {
            ProfilesHandler.INSTANCE.checkActiveProfile(getContentResolver());
            CardinalLayerManager.INSTANCE.init();

            DaoSessionManager.getInstance().setContext(context);
            DaoSessionManager.getInstance().setDatabaseName(this.getDatabase().getPath());
            daoSession = DaoSessionManager.getInstance().getDaoSession();

            //Attach observer to subject
            ProjectOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            WorkerOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            ContractOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            StockOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            ZoneOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            GroupOfLayerOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            LayerOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            MapObjecTypeOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            MapObjectTypeAttributeOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            MapObjecTypeDefectOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            MapObjecTypeStateOperations.getInstance().attachTo(DaoSessionManager.getInstance());

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("CRDINALAPPLICATION", "ACRA Initialized.");
    }
    @Override
    public synchronized SQLiteDatabase getDatabase() throws IOException {
        if (database == null) {
            databaseManager = new DatabaseManager();
            try {
                databaseFile = ResourcesManager.getInstance(this).getDatabaseFile();
                database = databaseManager.getDatabase(getInstance(), databaseFile);
            } catch (Exception e) {
                throw new IOException(e.getLocalizedMessage());
            }
        }
        return database;
    }

    @Override
    public void closeDatabase() {
        if (database != null && database.isOpen()) {
            database.close();
        }
        database = null;
        databaseFile = null;
    }

    public static void reset() {
        if (database != null) {
            try {
                database.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        database = null;
    }
}
