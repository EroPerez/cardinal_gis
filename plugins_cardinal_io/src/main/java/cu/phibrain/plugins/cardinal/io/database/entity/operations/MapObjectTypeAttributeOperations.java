package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectTypeAttribute;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectTypeAttributeDao;

public class MapObjectTypeAttributeOperations extends BaseOperations<MapObjectTypeAttribute, MapObjectTypeAttributeDao> {

    private static MapObjectTypeAttributeOperations mInstance = null;

    private MapObjectTypeAttributeOperations() {
        super();
        initEntityDao();
    }

    public static MapObjectTypeAttributeOperations getInstance() {
        if (mInstance == null) {
            mInstance = new MapObjectTypeAttributeOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        setDao(daoSession.getMapObjectTypeAttributeDao());
    }

}
