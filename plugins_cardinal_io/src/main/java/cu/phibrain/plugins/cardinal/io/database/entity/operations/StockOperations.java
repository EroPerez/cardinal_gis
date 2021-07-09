package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

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

    public List<Stock> loadAll(Long projectId) {
        Query<Stock> project_StocksQuery = null;
        synchronized (this) {
            if (project_StocksQuery == null) {
                QueryBuilder<Stock> queryBuilder = queryBuilder();
                queryBuilder.where(StockDao.Properties.ProjectId.eq(null));
                queryBuilder.orderRaw("T.'CODE' ASC");
                project_StocksQuery = queryBuilder.build();
            }
        }
        Query<Stock> query = project_StocksQuery.forCurrentThread();
        query.setParameter(0, projectId);
        return query.list();
    }
}
