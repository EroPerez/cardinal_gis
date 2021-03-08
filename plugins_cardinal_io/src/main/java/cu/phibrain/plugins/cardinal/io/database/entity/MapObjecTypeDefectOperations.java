package cu.phibrain.plugins.cardinal.io.database.entity;

import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.MapObjecTypeDefect;
import cu.phibrain.plugins.cardinal.io.model.MapObjecTypeDefectDao;

public class MapObjecTypeDefectOperations extends BaseRepo {

    private static MapObjecTypeDefectOperations mInstance = null;
    private MapObjecTypeDefectDao dao;

    private MapObjecTypeDefectOperations() {
        super();
        initEntityDao();
    }

    public static MapObjecTypeDefectOperations getInstance() {
        if (mInstance == null) {
            mInstance = new MapObjecTypeDefectOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getMapObjecTypeDefectDao();
    }

    public void insertMapObjecTypeDefectList(List<MapObjecTypeDefect> mapObjecTypeDefectList) {
        dao.insertOrReplaceInTx(mapObjecTypeDefectList);
    }


    public void insert(MapObjecTypeDefect mapObjecTypeDefect) {
        dao.insertOrReplaceInTx(mapObjecTypeDefect);
    }

    /**
     * @return list of user entity from the table name UserEntity in the database
     */
    public List<MapObjecTypeDefect> getMapObjecTypeDefectList() {
        return dao.queryBuilder()
                .list();
    }

    public void delete(Long mapObjecTypeDefectId) {
        dao.deleteByKey(mapObjecTypeDefectId);
    }


    public void update(MapObjecTypeDefect mapObjecTypeDefect) {
        dao.updateInTx(mapObjecTypeDefect);
    }
}
