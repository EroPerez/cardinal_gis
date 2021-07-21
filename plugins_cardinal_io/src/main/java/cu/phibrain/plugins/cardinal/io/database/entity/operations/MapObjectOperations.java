package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.entity.events.MapObjectEntityEventListener;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.RouteSegment;
import cu.phibrain.plugins.cardinal.io.database.entity.model.RouteSegmentDao;

public class MapObjectOperations extends BaseOperations<MapObject, MapObjectDao> {

    private static MapObjectOperations mInstance = null;

    private MapObjectOperations() {
        super();
        initEntityDao();
    }

    public static MapObjectOperations getInstance() {
        if (mInstance == null) {
            mInstance = new MapObjectOperations();
            mInstance.registerEventDispatcher(new MapObjectEntityEventListener());
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        setDao(daoSession.getMapObjectDao());
    }


    public List<RouteSegment> getRouteSegments(long Id) {
        Query<RouteSegment> mapObject_RouteSegmentsQuery = null;
        synchronized (this) {
            if (mapObject_RouteSegmentsQuery == null) {
                RouteSegmentDao targetDao = daoSession.getRouteSegmentDao();
                QueryBuilder<RouteSegment> queryBuilder = targetDao.queryBuilder();
                queryBuilder.whereOr(RouteSegmentDao.Properties.OriginId.eq(null),
                        RouteSegmentDao.Properties.DestinyId.eq(null));
                mapObject_RouteSegmentsQuery = queryBuilder.build();
            }
        }
        Query<RouteSegment> query = mapObject_RouteSegmentsQuery.forCurrentThread();
        query.setParameter(0, Id);
        query.setParameter(1, Id);
        return query.list();
    }
}
