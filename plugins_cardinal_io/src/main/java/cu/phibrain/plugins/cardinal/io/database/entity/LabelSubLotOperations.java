package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.LabelSubLot;
import cu.phibrain.plugins.cardinal.io.model.LabelSubLotDao;

public class LabelSubLotOperations extends BaseRepo<LabelSubLot, LabelSubLotDao> {

    private static LabelSubLotOperations mInstance = null;

    private LabelSubLotOperations() {
        super();
        initEntityDao();
    }

    public static LabelSubLotOperations getInstance() {
        if (mInstance == null) {
            mInstance = new LabelSubLotOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getLabelSubLotDao();
    }

}
