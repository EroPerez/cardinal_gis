package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkerRoute;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkerRouteDao;

public class WorkerRouteOperations extends BaseOperations<WorkerRoute, WorkerRouteDao> {

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
        setDao(daoSession.getWorkerRouteDao());
    }

}
