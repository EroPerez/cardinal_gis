package cu.phibrain.plugins.cardinal.io.database.entity;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.Layer;
import cu.phibrain.plugins.cardinal.io.model.LayerDao;

public class LayerOperations extends BaseRepo {

    private static LayerOperations mInstance = null;
    private LayerDao dao;

    private LayerOperations() {
        super();
        initEntityDao();
    }

    public static LayerOperations getInstance() {
        if (mInstance == null) {
            mInstance = new LayerOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getLayerDao();
    }

    public void insertLayerList(List<Layer> layerList) {
        dao.insertOrReplaceInTx(layerList);
    }


    public void insert(Layer layer) {
        dao.insertOrReplaceInTx(layer);
    }

    /**
     * @return list of user entity from the table name UserEntity in the database
     */
    public List<Layer> getLayerList() {
        return dao.queryBuilder()
                .list();
    }

    public List<Layer> getLayerByLeyerGroup(Long layer_group_id) {
        return dao.queryBuilder().where(LayerDao.Properties.GroupId.eq(layer_group_id), new WhereCondition[0]).build().list();

    }

    public void delete(Long layerId) {
        dao.deleteByKey(layerId);
    }


    public void update(Layer layer) {
        dao.updateInTx(layer);
    }

}
