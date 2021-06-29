package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.Supplier;
import cu.phibrain.plugins.cardinal.io.database.entity.model.SupplierDao;

public class SupplierOperations extends BaseOperations<Supplier, SupplierDao> {

    private static SupplierOperations mInstance = null;

    private SupplierOperations() {
        super();
        initEntityDao();
    }

    public static SupplierOperations getInstance() {
        if (mInstance == null) {
            mInstance = new SupplierOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        setDao(daoSession.getSupplierDao());
    }
}
