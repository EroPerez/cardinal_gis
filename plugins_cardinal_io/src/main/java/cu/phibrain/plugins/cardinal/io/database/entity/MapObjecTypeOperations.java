package cu.phibrain.plugins.cardinal.io.database.entity;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.model.MapObjecTypeDao;

public class MapObjecTypeOperations extends BaseRepo<MapObjecType, MapObjecTypeDao> {

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

//    public List<MapObjecType> getMapObjecTypeByLayerList(Long layer_id) {
//        return dao.queryBuilder().where(MapObjecTypeDao.Properties.LayerId(layer_id), new WhereCondition[0]).build()
//                .list();
//    }

//    public List<MapObjecType> getMapObjecTypeByParentList(Long parend_id) {
//        return dao.queryBuilder().where(MapObjecTypeDao.Properties.ParentId(parend_id), new WhereCondition[0]).build()
//                .list();
//    }

    public void delete(Long mapObjecTypeId) {
        dao.deleteByKey(mapObjecTypeId);
    }


    public void update(MapObjecType mapObjecType) {
        dao.updateInTx(mapObjecType);
    }

}
