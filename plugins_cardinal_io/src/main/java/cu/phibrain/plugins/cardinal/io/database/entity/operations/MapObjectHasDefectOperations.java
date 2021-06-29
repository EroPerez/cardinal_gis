package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.events.MapObjectHasDefectEventListener;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefect;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefectDao;

public class MapObjectHasDefectOperations extends BaseOperations<MapObjectHasDefect, MapObjectHasDefectDao> {

    private static MapObjectHasDefectOperations mInstance = null;

    private MapObjectHasDefectOperations() {
        super();
        initEntityDao();
    }

    public static MapObjectHasDefectOperations getInstance() {
        if (mInstance == null) {
            mInstance = new MapObjectHasDefectOperations();
            mInstance.registerEventDispatcher(new MapObjectHasDefectEventListener());
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        setDao(daoSession.getMapObjectHasDefectDao());
    }

}
