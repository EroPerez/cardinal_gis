package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.MapObjectHasState;
import cu.phibrain.plugins.cardinal.io.model.MapObjectHasStateDao;

public class MapObjectHasStateOperations extends BaseRepo<MapObjectHasState, MapObjectHasStateDao> {

    private static MapObjectHasStateOperations mInstance = null;

    private MapObjectHasStateOperations() {
        super();
        initEntityDao();
    }

    public static MapObjectHasStateOperations getInstance() {
        if (mInstance == null) {
            mInstance = new MapObjectHasStateOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getMapObjectHasStateDao();
    }

}
