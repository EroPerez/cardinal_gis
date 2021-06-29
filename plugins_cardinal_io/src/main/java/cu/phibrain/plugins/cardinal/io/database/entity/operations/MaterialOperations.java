package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.Material;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MaterialDao;

public class MaterialOperations extends BaseOperations<Material, MaterialDao> {

    private static MaterialOperations mInstance = null;

    private MaterialOperations() {
        super();
        initEntityDao();
    }

    public static MaterialOperations getInstance() {
        if (mInstance == null) {
            mInstance = new MaterialOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        setDao(daoSession.getMaterialDao());
    }

}
