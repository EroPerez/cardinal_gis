package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelMaterial;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelMaterialDao;

public class LabelMaterialOperations extends BaseOperations<LabelMaterial, LabelMaterialDao> {

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
        setDao(daoSession.getLabelMaterialDao());
    }
}
