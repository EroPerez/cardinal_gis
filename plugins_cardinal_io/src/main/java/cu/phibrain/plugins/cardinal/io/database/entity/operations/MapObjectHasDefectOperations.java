package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import cu.phibrain.plugins.cardinal.io.database.entity.events.MapObjectHasDefectEventListener;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefect;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefectDao;

public class MapObjectHasDefectOperations extends BaseOperations<MapObjectHasDefect, MapObjectHasDefectDao> {

    private static MapObjectHasDefectOperations mInstance = null;

    private MapObjectHasDefectOperations() {
        super();
        initEntityDao();
    }

    public static MapObjectHasDefectOperations getInstance() {
        if (mInstance == null) {
            mInstance = new MapObjectHasDefectOperations();
            mInstance.registerEventDispatcher(new MapObjectHasDefectEventListener());
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        setDao(daoSession.getMapObjectHasDefectDao());
    }

    public MapObjectHasDefect load(long mapObjectId, long mapObjectDefectId) {
        Query<MapObjectHasDefect> mapObject_DefectsQuery = null;
        synchronized (this) {
            if (mapObject_DefectsQuery == null) {
                QueryBuilder<MapObjectHasDefect> queryBuilder = queryBuilder();
                queryBuilder.where(
                        MapObjectHasDefectDao.Properties.MapObjectId.eq(null),
                        MapObjectHasDefectDao.Properties.MapObjectDefectId.eq(null),
                        MapObjectHasDefectDao.Properties.Deleted.eq(null)
                );
                mapObject_DefectsQuery = queryBuilder.build();
            }
        }
        Query<MapObjectHasDefect> query = mapObject_DefectsQuery.forCurrentThread();
        query.setParameter(0, mapObjectId);
        query.setParameter(1, mapObjectDefectId);
        query.setParameter(2, false);
        return query.unique();
    }

}
