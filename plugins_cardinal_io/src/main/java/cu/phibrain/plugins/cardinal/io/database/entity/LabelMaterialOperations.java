package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.LabelMaterial;
import cu.phibrain.plugins.cardinal.io.model.LabelMaterialDao;

public class LabelMaterialOperations extends BaseRepo<LabelMaterial, LabelMaterialDao> {

    private static LabelMaterialOperations mInstance = null;

    private LabelMaterialOperations() {
        super();
        initEntityDao();
    }

    public static LabelMaterialOperations getInstance() {
        if (mInstance == null) {
            mInstance = new LabelMaterialOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getLabelMaterialDao();
    }
}
