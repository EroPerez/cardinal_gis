package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import org.greenrobot.greendao.query.QueryBuilder;

import cu.phibrain.plugins.cardinal.io.database.entity.model.Contract;
import cu.phibrain.plugins.cardinal.io.database.entity.model.ContractDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkSession;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkSessionDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Worker;

public class WorkSessionOperations extends BaseOperations<WorkSession, WorkSessionDao> {

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
        setDao(daoSession.getWorkSessionDao());
    }


    public long countByWorker(Worker worker) {
        QueryBuilder<WorkSession> queryBuilder = this.queryBuilder();

        queryBuilder.join(WorkSessionDao.Properties.ContractId, Contract.class).where(
                ContractDao.Properties.WorkerId.eq(worker.getId())
        );

        return queryBuilder.count();
    }

}
