package cu.phibrain.plugins.cardinal.io.database.base;
import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;
import java.util.List;
import cu.phibrain.plugins.cardinal.io.model.DaoMaster;
import cu.phibrain.plugins.cardinal.io.model.DaoSession;
import cu.phibrain.plugins.cardinal.io.utils.Observer;
import cu.phibrain.plugins.cardinal.io.utils.Subject;

public class BaseRepo<Entity, Dao extends AbstractDao<Entity, Long>> implements Observer {

    private static BaseRepo instance = null;
    protected DaoSession daoSession;
    protected Dao dao;

    public BaseRepo() {
        daoSession = DaoSessionManager.getInstance().getDaoSession();
    }

    public static BaseRepo getInstance() {
        if (instance == null) {
            instance = new BaseRepo();
        }
        return instance;
    }

    public void truncate(Class classToDelete) {
        daoSession.deleteAll(classToDelete);
    }


    public void truncateAllTables() {
        daoSession.getDatabase().beginTransaction();
        try {
            DaoMaster.dropAllTables(daoSession.getDatabase(), true);
            DaoMaster.createAllTables(daoSession.getDatabase(), true);
            daoSession.getDatabase().setTransactionSuccessful();
        } finally {
            daoSession.getDatabase().endTransaction();
        }
    }

    @Override
    public void update() {
        daoSession = DaoSessionManager.getInstance().getDaoSession();
        initEntityDao();
    }

    @Override
    public void attachTo(Subject sub) {
        sub.register(this);
    }

    protected void initEntityDao() {

    }

    public Entity load(Long key) {
        return dao.load(key);
    }

    /**
     * @return list of user entity from the table name Entity in the database
     */
    public List<Entity> getAll() {
        return dao.queryBuilder()
                .list();
    }

    public void insertAll(List<Entity> entityList) {
        if (entityList != null && !entityList.isEmpty())
            dao.insertOrReplaceInTx(entityList);
    }

    public void insert(Entity entity) {
        if (entity != null) dao.insertOrReplaceInTx(entity);
    }

    public void delete(Long Id) {
        dao.deleteByKey(Id);
    }

    public void update(Entity entity) {

        if (entity != null) dao.updateInTx(entity);
    }

    /**
     * @return QueryBuilder for complex query.
     */
    public QueryBuilder<Entity> queryBuilder() {
        return dao.queryBuilder();
    }
}
