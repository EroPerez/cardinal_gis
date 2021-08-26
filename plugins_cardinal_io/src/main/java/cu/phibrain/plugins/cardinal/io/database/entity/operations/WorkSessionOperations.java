package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Iterator;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.entity.model.Contract;
import cu.phibrain.plugins.cardinal.io.database.entity.model.ContractDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.IExportable;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectDao;
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

    public List<MapObject> getMapObjects(long workSessionId) {
        MapObjectDao targetDao = daoSession.getMapObjectDao();
        List<MapObject> entities = targetDao._queryWorkSession_MapObjects(workSessionId);

        for (Iterator<MapObject> it = entities.iterator(); it.hasNext(); ) {
            MapObject entity = it.next();
            if (entity instanceof IExportable && ((IExportable) entity).getDeleted()) {
                it.remove();
            }
        }

        return entities;
    }

}
