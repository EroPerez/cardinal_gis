package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecTypeState;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecTypeStateDao;

public class MapObjecTypeStateOperations extends BaseOperations<MapObjecTypeState, MapObjecTypeStateDao> {

    private static MapObjecTypeStateOperations mInstance = null;

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
        setDao(daoSession.getMapObjecTypeStateDao());
    }

}
