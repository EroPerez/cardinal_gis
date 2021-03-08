package cu.phibrain.plugins.cardinal.io.database.entity;

import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.model.MapObjecTypeDao;

public class MapObjecTypeOperations extends BaseRepo {

    private static MapObjecTypeOperations mInstance = null;
    private MapObjecTypeDao dao;

    private MapObjecTypeOperations() {
        super();
        initEntityDao();
    }

    public static MapObjecTypeOperations getInstance() {
        if (mInstance == null) {
            mInstance = new MapObjecTypeOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getMapObjecTypeDao();
    }

    public void insertMapObjecTypesList(List<MapObjecType> mapObjecTypeList) {
        dao.insertOrReplaceInTx(mapObjecTypeList);
    }


    public void insert(MapObjecType mapObjecType) {
        dao.insertOrReplaceInTx(mapObjecType);
    }

    /**
     * @return list of user entity from the table name UserEntity in the database
     */
    public List<MapObjecType> getMapObjecTypeList() {
        return dao.queryBuilder()
                .list();
    }

    public void delete(Long mapObjecTypeId) {
        dao.deleteByKey(mapObjecTypeId);
    }


    public void update(MapObjecType mapObjecType) {
        dao.updateInTx(mapObjecType);
    }
}
