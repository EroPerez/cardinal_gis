package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecTypeDefect;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecTypeDefectDao;

public class MapObjecTypeDefectOperations extends BaseOperations<MapObjecTypeDefect, MapObjecTypeDefectDao> {

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
        setDao(daoSession.getMapObjecTypeDefectDao());
    }

}
