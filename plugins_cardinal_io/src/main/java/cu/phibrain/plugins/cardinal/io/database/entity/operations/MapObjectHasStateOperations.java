package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasState;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasStateDao;

public class MapObjectHasStateOperations extends BaseOperations<MapObjectHasState, MapObjectHasStateDao> {

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
        setDao(daoSession.getMapObjectHasStateDao());
    }

}
