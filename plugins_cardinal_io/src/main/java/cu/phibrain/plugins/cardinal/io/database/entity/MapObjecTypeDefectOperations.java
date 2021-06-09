package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.MapObjecTypeDefect;
import cu.phibrain.plugins.cardinal.io.model.MapObjecTypeDefectDao;

public class MapObjecTypeDefectOperations extends BaseRepo<MapObjecTypeDefect, MapObjecTypeDefectDao> {

    private static MapObjecTypeDefectOperations mInstance = null;

    private MapObjecTypeDefectOperations() {
        super();
        initEntityDao();
    }

    public static MapObjecTypeDefectOperations getInstance() {
        if (mInstance == null) {
            mInstance = new MapObjecTypeDefectOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getMapObjecTypeDefectDao();
    }

}
