package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.Stock;
import cu.phibrain.plugins.cardinal.io.database.entity.model.StockDao;


public class StockOperations extends BaseOperations<Stock, StockDao> {

    private static StockOperations mInstance = null;

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
        setDao(daoSession.getStockDao());
    }
}
