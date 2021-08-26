package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import java.util.Iterator;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.entity.model.IExportable;
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


    public List<MapObjectHasDefectHasImages> getImages(long defectId) {

        List<MapObjectHasDefectHasImages> entities = getDao()._queryMapObjectHasDefect_Images(defectId);

        for (Iterator<MapObjectHasDefectHasImages> it = entities.iterator(); it.hasNext(); ) {
            MapObjectHasDefectHasImages entity = it.next();
            if (entity instanceof IExportable && ((IExportable) entity).getDeleted()) {
                it.remove();
            }
        }
        return entities;
    }
}
