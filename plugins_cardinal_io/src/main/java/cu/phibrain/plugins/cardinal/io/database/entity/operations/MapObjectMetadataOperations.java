package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import java.util.Iterator;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.entity.model.IExportable;
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


    public List<MapObjectMetadata> getAll(long mapObjectId) {

        List<MapObjectMetadata> entities = getDao()._queryMapObject_Metadata(mapObjectId);

        for (Iterator<MapObjectMetadata> it = entities.iterator(); it.hasNext(); ) {
            MapObjectMetadata entity = it.next();
            if (entity instanceof IExportable && ((IExportable) entity).getDeleted()) {
                it.remove();
            }
        }
        return entities;
    }
}
