package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.Worker;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkerDao;


public class WorkerOperations extends BaseOperations<Worker, WorkerDao> {

    private static WorkerOperations mInstance = null;

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
        setDao(daoSession.getWorkerDao());
    }

    public Worker findOneBy(String username){
        return this.queryBuilder().where(WorkerDao.Properties.Username.eq(username)).unique();
    }
}
