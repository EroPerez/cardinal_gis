package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.MapObjectMetadata;
import cu.phibrain.plugins.cardinal.io.model.MapObjectMetadataDao;

public class MapObjectMetadataOperations extends BaseRepo<MapObjectMetadata, MapObjectMetadataDao> {

    private static MapObjectMetadataOperations mInstance = null;

    private MapObjectMetadataOperations() {
        super();
        initEntityDao();
    }

    public static MapObjectMetadataOperations getInstance() {
        if (mInstance == null) {
            mInstance = new MapObjectMetadataOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getMapObjectMetadataDao();
    }
}
