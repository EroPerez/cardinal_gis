package cu.phibrain.plugins.cardinal.io.database.entity;

import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.MapObjectTypeAttribute;
import cu.phibrain.plugins.cardinal.io.model.MapObjectTypeAttributeDao;

public class MapObjectTypeAttributeOperations extends BaseRepo {

    private static MapObjectTypeAttributeOperations mInstance = null;
    private MapObjectTypeAttributeDao dao;

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

    public void insertMapObjectTypeAttributeList(List<MapObjectTypeAttribute> mapObjectTypeAttributeList) {
        dao.insertOrReplaceInTx(mapObjectTypeAttributeList);
    }


    public void insert(MapObjectTypeAttribute group) {
        dao.insertOrReplaceInTx(group);
    }

    /**
     * @return list of user entity from the table name UserEntity in the database
     */
    public List<MapObjectTypeAttribute> getMapObjectTypeAttributeList() {
        return dao.queryBuilder()
                .list();
    }

    public void delete(Long mapObjectTypeAttributeId) {
        dao.deleteByKey(mapObjectTypeAttributeId);
    }


    public void update(MapObjectTypeAttribute mapObjectTypeAttribute) {
        dao.updateInTx(mapObjectTypeAttribute);
    }
}
