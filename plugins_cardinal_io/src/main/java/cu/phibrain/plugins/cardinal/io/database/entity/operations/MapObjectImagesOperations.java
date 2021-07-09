package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

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

    public List<MapObjectImages> loadAll(long mapObjectId) {
        Query<MapObjectImages> mapObject_ImagesQuery = null;
        synchronized (this) {
            if (mapObject_ImagesQuery == null) {
                QueryBuilder<MapObjectImages> queryBuilder = queryBuilder();
                queryBuilder.where(MapObjectImagesDao.Properties.MapObjectId.eq(null));
                mapObject_ImagesQuery = queryBuilder.build();
            }
        }
        Query<MapObjectImages> query = mapObject_ImagesQuery.forCurrentThread();
        query.setParameter(0, mapObjectId);
        return query.list();
    }
}
