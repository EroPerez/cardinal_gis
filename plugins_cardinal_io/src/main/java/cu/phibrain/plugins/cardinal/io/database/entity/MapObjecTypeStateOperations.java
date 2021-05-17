package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.MapObjecTypeState;
import cu.phibrain.plugins.cardinal.io.model.MapObjecTypeStateDao;

public class MapObjecTypeStateOperations extends BaseRepo<MapObjecTypeState, MapObjecTypeStateDao> {

    private static MapObjecTypeStateOperations mInstance = null;
    private MapObjecTypeStateDao dao;

    private MapObjecTypeStateOperations() {
        super();
        initEntityDao();
    }

    public static MapObjecTypeStateOperations getInstance() {
        if (mInstance == null) {
            mInstance = new MapObjecTypeStateOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getMapObjecTypeStateDao();
    }

}
