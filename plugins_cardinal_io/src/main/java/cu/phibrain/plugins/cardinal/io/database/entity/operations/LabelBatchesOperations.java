package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelBatches;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelBatchesDao;

public class LabelBatchesOperations  extends BaseOperations<LabelBatches, LabelBatchesDao> {

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
        setDao(daoSession.getLabelBatchesDao());
    }

}
