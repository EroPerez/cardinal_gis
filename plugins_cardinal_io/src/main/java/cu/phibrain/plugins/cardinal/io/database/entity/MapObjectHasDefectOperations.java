package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.MapObjectHasDefect;
import cu.phibrain.plugins.cardinal.io.model.MapObjectHasDefectDao;

public class MapObjectHasDefectOperations extends BaseRepo<MapObjectHasDefect, MapObjectHasDefectDao> {

    private static MapObjectHasDefectOperations mInstance = null;

    private MapObjectHasDefectOperations() {
        super();
        initEntityDao();
    }

    public static MapObjectHasDefectOperations getInstance() {
        if (mInstance == null) {
            mInstance = new MapObjectHasDefectOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getMapObjectHasDefectDao();
    }

}
