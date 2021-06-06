package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.MapObjectHasDefectHasImages;
import cu.phibrain.plugins.cardinal.io.model.MapObjectHasDefectHasImagesDao;

public class MapObjectHasDefectHasImagesOperations extends BaseRepo<MapObjectHasDefectHasImages, MapObjectHasDefectHasImagesDao> {

    private static MapObjectHasDefectHasImagesOperations mInstance = null;

    private MapObjectHasDefectHasImagesOperations() {
        super();
        initEntityDao();
    }

    public static MapObjectHasDefectHasImagesOperations getInstance() {
        if (mInstance == null) {
            mInstance = new MapObjectHasDefectHasImagesOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getMapObjectHasDefectHasImagesDao();
    }
}
