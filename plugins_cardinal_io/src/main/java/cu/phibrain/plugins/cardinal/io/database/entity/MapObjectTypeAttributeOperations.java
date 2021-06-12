package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.MapObjectTypeAttribute;
import cu.phibrain.plugins.cardinal.io.model.MapObjectTypeAttributeDao;

public class MapObjectTypeAttributeOperations extends BaseRepo<MapObjectTypeAttribute, MapObjectTypeAttributeDao> {

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
        dao = daoSession.getMapObjectTypeAttributeDao();
    }

}
