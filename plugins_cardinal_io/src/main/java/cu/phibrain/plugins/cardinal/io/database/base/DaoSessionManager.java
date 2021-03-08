package cu.phibrain.plugins.cardinal.io.database.base;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.model.DaoMaster;
import cu.phibrain.plugins.cardinal.io.model.DaoSession;
import cu.phibrain.plugins.cardinal.io.utils.Observer;
import cu.phibrain.plugins.cardinal.io.utils.Subject;


/**
 * Usage:
 * Example: Get the operation class Song of this class SongDao
 * DaoSessionManager.getInstance().setDatabaseName("android.db");
 * DaoSessionManager.getInstance().setContext(getApplicationContext());
 * DaoSession daoSession = DaoSessionManager.getInstance()
 * .getDaoSession();
 * SongDao songDao = daoSession.getSongDao();
 */

public class DaoSessionManager implements Subject {

    public static volatile DaoSessionManager mInstance = null;
    private final Object MUTEX = new Object();
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private String databaseName;
    private Context mContext;
    private List<Observer> observers;
    private boolean changed;

    private DaoSessionManager() {
        databaseName = null;
        mContext = null;
        this.observers = new ArrayList<>();
    }

    public static DaoSessionManager getInstance() {
        if (mInstance == null) {
            synchronized (DaoSessionManager.class) {
                if (mInstance == null) {
                    mInstance = new DaoSessionManager();
                }
            }
        }

        return mInstance;
    }

    public DaoMaster getDaoMaster() {

        MySQLiteOpenHelper mHelper = new MySQLiteOpenHelper(mContext, databaseName, null);
        daoMaster = new DaoMaster(mHelper.getWritableDb());
        return daoMaster;
    }

    public DaoSession getDaoSession() {

        if (daoSession == null) {

            if (daoMaster == null) {
                getDaoMaster();
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public void resetDaoSession() {
        this.daoMaster = null;
        this.daoSession = null;
        this.changed = true;
        this.notifyObservers();
    }

    @Override
    public void register(Observer obj) {
        if (obj == null) throw new NullPointerException("Null Observer");
        synchronized (MUTEX) {
            if (!observers.contains(obj)) observers.add(obj);
        }
    }

    @Override
    public void unregister(Observer obj) {
        synchronized (MUTEX) {
            observers.remove(obj);
        }
    }

    @Override
    public void notifyObservers() {
        //List<Observer> observersLocal = null;
        //synchronization is used to make sure any observer registered after message is received is not notified
        synchronized (MUTEX) {
            if (!changed)
                return;
            //  observersLocal = new ArrayList<>(this.observers);
            this.changed = false;
        }
        for (Observer obj : this.observers) {
            obj.update();
        }

    }
}
