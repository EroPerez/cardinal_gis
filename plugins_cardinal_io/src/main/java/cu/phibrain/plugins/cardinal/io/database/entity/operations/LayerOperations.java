package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.Layer;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LayerDao;

public class LayerOperations extends BaseOperations<Layer, LayerDao> {

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
        setDao(daoSession.getLayerDao());
    }

}
