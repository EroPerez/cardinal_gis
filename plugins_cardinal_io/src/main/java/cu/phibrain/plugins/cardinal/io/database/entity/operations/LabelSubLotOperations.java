package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.entity.model.Label;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelSubLot;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelSubLotDao;

public class LabelSubLotOperations extends BaseOperations<LabelSubLot, LabelSubLotDao> {

    private static LabelSubLotOperations mInstance = null;

    private LabelSubLotOperations() {
        super();
        initEntityDao();
    }

    public static LabelSubLotOperations getInstance() {
        if (mInstance == null) {
            mInstance = new LabelSubLotOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        setDao(daoSession.getLabelSubLotDao());
    }


    public LabelSubLot load(long workSessionId, String labelCode) {
        QueryBuilder<LabelSubLot> queryBuilder = queryBuilder().where(
                LabelSubLotDao.Properties.WorkerSessionId.eq(workSessionId)
        );

       queryBuilder.join(LabelSubLotDao.Properties.LabelId, Label.class).where(
               LabelDao.Properties.Code.eq(labelCode)
        );

       return queryBuilder.unique();
    }

    public List<LabelSubLot> loadAll(long workerSessionId) {
        Query<LabelSubLot> workSession_LabelsQuery = null;
        synchronized (this) {
            if (workSession_LabelsQuery == null) {
                QueryBuilder<LabelSubLot> queryBuilder = queryBuilder();
                queryBuilder.where(LabelSubLotDao.Properties.WorkerSessionId.eq(null));
                workSession_LabelsQuery = queryBuilder.build();
            }
        }
        Query<LabelSubLot> query = workSession_LabelsQuery.forCurrentThread();
        query.setParameter(0, workerSessionId);
        return query.list();
    }

}
