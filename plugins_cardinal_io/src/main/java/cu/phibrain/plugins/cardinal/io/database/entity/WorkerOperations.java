package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.Worker;
import cu.phibrain.plugins.cardinal.io.model.WorkerDao;


public class WorkerOperations extends BaseRepo<Worker, WorkerDao> {

    private static WorkerOperations mInstance = null;
    private WorkerDao dao;

    private WorkerOperations() {
        super();
        initEntityDao();
    }

    public static WorkerOperations getInstance() {
        if (mInstance == null) {
            mInstance = new WorkerOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getWorkerDao();
    }
}
