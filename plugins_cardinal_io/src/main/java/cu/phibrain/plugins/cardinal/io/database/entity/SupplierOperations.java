package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.Supplier;
import cu.phibrain.plugins.cardinal.io.model.SupplierDao;

public class SupplierOperations extends BaseRepo<Supplier, SupplierDao> {

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
        dao = daoSession.getSupplierDao();
    }
}
