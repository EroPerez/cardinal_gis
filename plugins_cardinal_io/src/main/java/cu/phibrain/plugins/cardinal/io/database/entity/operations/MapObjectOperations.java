package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Iterator;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.entity.events.MapObjectEntityEventListener;
import cu.phibrain.plugins.cardinal.io.database.entity.model.IExportable;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecTypeDefect;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecTypeState;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefect;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasState;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectMetadata;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectTypeAttribute;
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
                queryBuilder.where(
                        RouteSegmentDao.Properties.Deleted.eq(false),
                        queryBuilder.or(RouteSegmentDao.Properties.OriginId.eq(Id),
                                RouteSegmentDao.Properties.DestinyId.eq(Id))
                );

                mapObject_RouteSegmentsQuery = queryBuilder.build();
            }
        }
        Query<RouteSegment> query = mapObject_RouteSegmentsQuery.forCurrentThread();

        return query.list();
    }

    public void clone(MapObject other) {

        MapObjecType mapObjectObjectType = other.getObjectType();

        //Delete all related metadata not common
        List<MapObjectTypeAttribute> attributes = MapObjecTypeOperations.getInstance().getAttrs(mapObjectObjectType.getId());

        for (MapObjectMetadata metadata :
                other.getMetadata()) {
            if (!attributes.contains(metadata.getAttribute()))
                MapObjectMetadataOperations.getInstance().delete(metadata);
        }

        //Create all extra attribute not common
        other.resetMetadata();
        for (MapObjectTypeAttribute attr : attributes) {
            for (MapObjectMetadata metadata : other.getMetadata()) {

                if (!metadata.getDeleted() && !attr.equals(metadata.getAttribute())) {
                    String value = "" + attr.getDefaultValue();
                    if (value.isEmpty()) value = " ";

                    MapObjectMetadataOperations.getInstance().save(new MapObjectMetadata(null, other.getId(), value, attr.getId()));
                }
            }
        }

        //Delete all related states not common
        List<MapObjecTypeState> states = MapObjecTypeOperations.getInstance().getStates(mapObjectObjectType.getId());
        for (MapObjectHasState state :
                other.getStates()) {
            if (!states.contains(state.getMapObjectState()))
                MapObjectHasStateOperations.getInstance().delete(state);
        }

        //Delete all related defects and images of defects not common
        List<MapObjecTypeDefect> defects = MapObjecTypeOperations.getInstance().getDefects(mapObjectObjectType.getId());
        for (MapObjectHasDefect defect :
                other.getDefects()) {
            if (!defects.contains(defect.getMapObjectDefect()))
                MapObjectHasDefectOperations.getInstance().delete(defect);
        }
        save(other);
    }


    public List<MapObject> getJoinedList(long id) {

        List<MapObject> entities = getDao()._queryMapObject_JoinedList(id);

        for (Iterator<MapObject> it = entities.iterator(); it.hasNext(); ) {
            MapObject entity = it.next();
            if (entity instanceof IExportable && ((IExportable) entity).getDeleted()) {
                it.remove();
            }
        }

        return entities;

    }


    public long getRouteSegmentsCount(long Id) {
        Query<RouteSegment> mapObject_RouteSegmentsQuery = null;
        synchronized (this) {
            if (mapObject_RouteSegmentsQuery == null) {
                RouteSegmentDao targetDao = daoSession.getRouteSegmentDao();
                QueryBuilder<RouteSegment> queryBuilder = targetDao.queryBuilder();
                queryBuilder.where(
                        RouteSegmentDao.Properties.Deleted.eq(false),
                        queryBuilder.or(RouteSegmentDao.Properties.OriginId.eq(Id),
                                RouteSegmentDao.Properties.DestinyId.eq(Id))
                );

                return queryBuilder.count();
            }
        }

        return 0L;
    }

}
