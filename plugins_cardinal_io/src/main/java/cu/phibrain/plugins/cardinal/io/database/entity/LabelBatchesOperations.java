package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.LabelBatches;
import cu.phibrain.plugins.cardinal.io.model.LabelBatchesDao;

public class LabelBatchesOperations  extends BaseRepo<LabelBatches, LabelBatchesDao> {

    private static LabelBatchesOperations mInstance = null;

    private LabelBatchesOperations() {
        super();
        initEntityDao();
    }

    public static LabelBatchesOperations getInstance() {
        if (mInstance == null) {
            mInstance = new LabelBatchesOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getLabelBatchesDao();
    }

}
