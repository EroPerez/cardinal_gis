package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.Label;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelDao;

public class LabelOperations extends BaseOperations<Label, LabelDao> {

    private static LabelOperations mInstance = null;

    private LabelOperations() {
        super();
        initEntityDao();
    }

    public static LabelOperations getInstance() {
        if (mInstance == null) {
            mInstance = new LabelOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        setDao(daoSession.getLabelDao());
    }
}
