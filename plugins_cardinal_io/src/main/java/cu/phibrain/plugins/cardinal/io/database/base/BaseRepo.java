package cu.phibrain.plugins.cardinal.io.database.base;

import cu.phibrain.plugins.cardinal.io.model.DaoMaster;
import cu.phibrain.plugins.cardinal.io.model.DaoSession;
import cu.phibrain.plugins.cardinal.io.utils.Observer;
import cu.phibrain.plugins.cardinal.io.utils.Subject;

public class BaseRepo implements Observer {

    private static BaseRepo instance = null;
    protected DaoSession daoSession;

    public BaseRepo() {
        daoSession = DaoSessionManager.getInstance().getDaoSession();
    }

    public static BaseRepo getInstance() {
        if (instance == null) {
            instance = new BaseRepo();
        }
        return instance;
    }

    public void truncate(Class classToDelete) {
        daoSession.deleteAll(classToDelete);
    }


    public void truncateAllTables() {
        daoSession.getDatabase().beginTransaction();
        try {
            DaoMaster.dropAllTables(daoSession.getDatabase(), true);
            DaoMaster.createAllTables(daoSession.getDatabase(), true);
            daoSession.getDatabase().setTransactionSuccessful();
        } finally {
            daoSession.getDatabase().endTransaction();
        }
    }

    @Override
    public void update() {
        daoSession = DaoSessionManager.getInstance().getDaoSession();
        initEntityDao();
    }

    @Override
    public void attachTo(Subject sub) {
        sub.register(this);
    }

    protected void initEntityDao() {

    }
}
