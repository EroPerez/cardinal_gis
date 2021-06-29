package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectImages;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectImagesDao;

public class MapObjectImagesOperations extends BaseOperations<MapObjectImages, MapObjectImagesDao> {

    private static MapObjectImagesOperations mInstance = null;

    private MapObjectImagesOperations() {
        super();
        initEntityDao();
    }

    public static MapObjectImagesOperations getInstance() {
        if (mInstance == null) {
            mInstance = new MapObjectImagesOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        setDao(daoSession.getMapObjectImagesDao());
    }
}
