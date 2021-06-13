package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.MapObject;
import cu.phibrain.plugins.cardinal.io.model.MapObjectDao;

public class MapObjectOperations extends BaseRepo<MapObject, MapObjectDao> {

    private static MapObjectOperations mInstance = null;

    private MapObjectOperations() {
        super();
        initEntityDao();
    }

    public static MapObjectOperations getInstance() {
        if (mInstance == null) {
            mInstance = new MapObjectOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getMapObjectDao();
    }

}
