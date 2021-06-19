package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.Layer;
import cu.phibrain.plugins.cardinal.io.model.LayerDao;

public class LayerOperations extends BaseRepo<Layer, LayerDao> {

    private static LayerOperations mInstance = null;

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

}
