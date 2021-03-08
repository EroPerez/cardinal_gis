package cu.phibrain.plugins.cardinal.io.database.entity;

import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.MapObjecTypeState;
import cu.phibrain.plugins.cardinal.io.model.MapObjecTypeStateDao;

public class MapObjecTypeStateOperations extends BaseRepo {

    private static MapObjecTypeStateOperations mInstance = null;
    private MapObjecTypeStateDao dao;

    private MapObjecTypeStateOperations() {
        super();
        initEntityDao();
    }

    public static MapObjecTypeStateOperations getInstance() {
        if (mInstance == null) {
            mInstance = new MapObjecTypeStateOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getMapObjecTypeStateDao();
    }

    public void insertMapObjecTypeStateList(List<MapObjecTypeState> mapObjecTypeStateList) {
        dao.insertOrReplaceInTx(mapObjecTypeStateList);
    }


    public void insert(MapObjecTypeState mapObjecTypeState) {
        dao.insertOrReplaceInTx(mapObjecTypeState);
    }

    /**
     * @return list of user entity from the table name UserEntity in the database
     */
    public List<MapObjecTypeState> getMapObjecTypeStateList() {
        return dao.queryBuilder()
                .list();
    }

    public void delete(Long mapObjecTypeStateId) {
        dao.deleteByKey(mapObjecTypeStateId);
    }


    public void update(MapObjecTypeState mapObjecTypeState) {
        dao.updateInTx(mapObjecTypeState);
    }
}
