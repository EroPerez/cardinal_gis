package cu.phibrain.cardinal.app;

import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

import java.io.IOException;

import cu.phibrain.plugins.cardinal.io.model.DaoMaster;
import cu.phibrain.plugins.cardinal.io.model.DaoSession;
import eu.geopaparazzi.core.GeopaparazziApplication;


public class CardinalApplication extends GeopaparazziApplication {
    private DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        // regular SQLite database
        try {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, this.getDatabase().getPath());
            Database db = helper.getWritableDb();

            // encrypted SQLCipher database
            // note: you need to add SQLCipher to your dependencies, check the build.gradle file
            // DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db-encrypted");
            // Database db = helper.getEncryptedWritableDb("encryption-key");

            daoSession = new DaoMaster(db).newSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }
}
