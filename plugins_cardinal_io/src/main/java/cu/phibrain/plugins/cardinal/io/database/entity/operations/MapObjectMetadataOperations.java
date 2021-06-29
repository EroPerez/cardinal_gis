package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectMetadata;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectMetadataDao;

public class MapObjectMetadataOperations extends BaseOperations<MapObjectMetadata, MapObjectMetadataDao> {

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
        setDao(daoSession.getMapObjectMetadataDao());
    }
}
