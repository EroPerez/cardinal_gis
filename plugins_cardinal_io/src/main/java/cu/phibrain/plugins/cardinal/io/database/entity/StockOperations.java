package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.Stock;
import cu.phibrain.plugins.cardinal.io.model.StockDao;


public class StockOperations extends BaseRepo<Stock, StockDao> {

    private static StockOperations mInstance = null;
    private StockDao dao;

    private StockOperations() {
        super();
        initEntityDao();
    }

    public static StockOperations getInstance() {
        if (mInstance == null) {
            mInstance = new StockOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getStockDao();
    }
}
