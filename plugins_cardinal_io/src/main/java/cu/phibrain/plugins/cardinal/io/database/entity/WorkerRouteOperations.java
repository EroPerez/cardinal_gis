package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.WorkerRoute;
import cu.phibrain.plugins.cardinal.io.model.WorkerRouteDao;

public class WorkerRouteOperations extends BaseRepo<WorkerRoute, WorkerRouteDao> {

    private static WorkerRouteOperations mInstance = null;

    private WorkerRouteOperations() {
        super();
        initEntityDao();
    }

    public static WorkerRouteOperations getInstance() {
        if (mInstance == null) {
            mInstance = new WorkerRouteOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getWorkerRouteDao();
    }

}
