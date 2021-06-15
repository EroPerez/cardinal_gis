package cu.phibrain.plugins.cardinal.io.database.entity;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.model.MapObjecTypeDao;
import cu.phibrain.plugins.cardinal.io.model.MapObject;

public class MapObjecTypeOperations extends BaseRepo<MapObjecType, MapObjecTypeDao> {

    private static MapObjecTypeOperations mInstance = null;

    private MapObjecTypeOperations() {
        super();
        initEntityDao();
    }

    public static MapObjecTypeOperations getInstance() {
        if (mInstance == null) {
            mInstance = new MapObjecTypeOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getMapObjecTypeDao();
    }

    public List<MapObjecType> searchChildMto(List<MapObjecType> objcTypeList, MapObjecType mapObjecType){
        Query<MapObjecType> MapObjectsQuery;
        synchronized (this) {
            QueryBuilder<MapObjecType> queryBuilder = queryBuilder();
            queryBuilder.where(MapObjecTypeDao.Properties.ParentId.eq(null));
            MapObjectsQuery = queryBuilder.build();
        }
        Query<MapObjecType> query = MapObjectsQuery.forCurrentThread();
        query.setParameter(0, mapObjecType.getId());
        //Load Mto by parent
        List<MapObjecType> mtoChildList = query.list();
        for (MapObjecType mtoIndex:mtoChildList) {
            if (mtoIndex.getIsAbstract())
                searchChildMto(objcTypeList,mtoIndex);
            else
                objcTypeList.add(mtoIndex);
        }
        return  objcTypeList;

    }
}
