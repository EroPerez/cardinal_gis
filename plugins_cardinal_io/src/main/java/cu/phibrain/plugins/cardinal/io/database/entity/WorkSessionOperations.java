package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.WorkSession;
import cu.phibrain.plugins.cardinal.io.model.WorkSessionDao;

public class WorkSessionOperations extends BaseRepo<WorkSession, WorkSessionDao> {

    private static WorkSessionOperations mInstance = null;

    private WorkSessionOperations() {
        super();
        initEntityDao();
    }

    public static WorkSessionOperations getInstance() {
        if (mInstance == null) {
            mInstance = new WorkSessionOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getWorkSessionDao();
    }

}
