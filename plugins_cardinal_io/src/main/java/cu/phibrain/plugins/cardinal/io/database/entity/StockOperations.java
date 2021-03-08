package cu.phibrain.plugins.cardinal.io.database.entity;

import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.Stock;
import cu.phibrain.plugins.cardinal.io.model.StockDao;


public class StockOperations extends BaseRepo {

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

    public void insertStockList(List<Stock> stockList) {
        dao.insertOrReplaceInTx(stockList);
    }


    public void insertStock(Stock stock) {
        dao.insertOrReplaceInTx(stock);
    }

    /**
     * @return list of user entity from the table name Entity in the database
     */
    public List<Stock> getStockList() {
        return dao.queryBuilder()
                .list();
    }

    public void delete(Long stockId) {
        dao.deleteByKey(stockId);
    }


    public void update(Stock stock) {
        dao.updateInTx(stock);
    }
}
