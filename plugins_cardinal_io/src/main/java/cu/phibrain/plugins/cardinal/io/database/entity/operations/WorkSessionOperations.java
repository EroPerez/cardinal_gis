package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkSession;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkSessionDao;

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

}
