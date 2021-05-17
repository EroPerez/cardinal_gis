package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.MapObjectImages;
import cu.phibrain.plugins.cardinal.io.model.MapObjectImagesDao;

public class MapObjectImagesOperations extends BaseRepo<MapObjectImages, MapObjectImagesDao> {

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
        dao = daoSession.getMapObjectImagesDao();
    }
}
