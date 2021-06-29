package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefectHasImages;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefectHasImagesDao;

public class MapObjectHasDefectHasImagesOperations extends BaseOperations<MapObjectHasDefectHasImages, MapObjectHasDefectHasImagesDao> {

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
        setDao(daoSession.getMapObjectHasDefectHasImagesDao());
    }


    public List<MapObjectHasDefectHasImages> getImages(long defectId){
         return getDao()._queryMapObjectHasDefect_Images(defectId);
    }
}
