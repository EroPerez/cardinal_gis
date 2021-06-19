package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.Material;
import cu.phibrain.plugins.cardinal.io.model.MaterialDao;

public class MaterialOperations extends BaseRepo<Material, MaterialDao> {

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
        dao = daoSession.getMaterialDao();
    }

}
