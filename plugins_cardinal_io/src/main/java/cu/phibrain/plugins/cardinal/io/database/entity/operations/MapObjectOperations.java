package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.events.MapObjectEntityEventListener;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectDao;

public class MapObjectOperations extends BaseOperations<MapObject, MapObjectDao> {

    private static MapObjectOperations mInstance = null;

    private MapObjectOperations() {
        super();
        initEntityDao();
    }

    public static MapObjectOperations getInstance() {
        if (mInstance == null) {
            mInstance = new MapObjectOperations();
            mInstance.registerEventDispatcher(new MapObjectEntityEventListener());
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        setDao(daoSession.getMapObjectDao());
    }

}
