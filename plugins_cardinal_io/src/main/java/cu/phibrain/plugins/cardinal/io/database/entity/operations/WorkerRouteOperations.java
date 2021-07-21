package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import org.greenrobot.greendao.query.QueryBuilder;

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

    public WorkerRoute load(long workSessionId, long gpsLogsId) {
        QueryBuilder<WorkerRoute> queryBuilder = queryBuilder().where(
                WorkerRouteDao.Properties.WorkerSessionId.eq(workSessionId),
                WorkerRouteDao.Properties.GpsLogsTableId.eq(gpsLogsId)
        );

        return queryBuilder.unique();
    }

}
