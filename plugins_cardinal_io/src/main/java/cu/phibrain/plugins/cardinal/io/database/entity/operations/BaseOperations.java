package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.Date;
import java.util.Iterator;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.base.DaoSessionManager;
import cu.phibrain.plugins.cardinal.io.database.entity.events.EntityEventListener;
import cu.phibrain.plugins.cardinal.io.database.entity.model.DaoMaster;
import cu.phibrain.plugins.cardinal.io.database.entity.model.DaoSession;
import cu.phibrain.plugins.cardinal.io.database.entity.model.IEntity;
import cu.phibrain.plugins.cardinal.io.database.entity.model.IExportable;
import cu.phibrain.plugins.cardinal.io.utils.Observer;
import cu.phibrain.plugins.cardinal.io.utils.Subject;

public class BaseOperations<Entity extends IEntity, Dao extends AbstractDao<Entity, Long>> implements Observer {

    private static BaseOperations instance = null;
    protected DaoSession daoSession;
    protected Dao dao;


    protected EntityEventListener dispatcher;

    public BaseOperations() {
        daoSession = DaoSessionManager.getInstance().getDaoSession();
        this.dispatcher = null;
    }

    public static BaseOperations getInstance() {
        if (instance == null) {
            instance = new BaseOperations();
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

    public void registerEventDispatcher(EntityEventListener listener) {
        this.dispatcher = listener;
    }

    public void unregisterEventDispatcher() {
        this.dispatcher = null;
    }

    public Entity load(Long key) {
        Entity entity = getDao().load(key);
        if (entity instanceof IExportable && ((IExportable) entity).getDeleted()) {
            detach(entity);
            return null;
        }
        return entity;
    }

    /**
     * Load entity even it is marked as deleted, Exportable entity are mark as deleted if they are remote(shared to cloud)
     *
     * @param key
     * @param ifIsDeleted
     * @return Entity
     */
    public Entity load(Long key, boolean ifIsDeleted) {
        Entity entity = getDao().load(key);
        if (!ifIsDeleted && entity instanceof IExportable && ((IExportable) entity).getDeleted()) {
            detach(entity);
            return null;
        }
        return entity;
    }

    /**
     * @return list of user entity from the table name Entity in the database
     */
    public List<Entity> getAll() {
        List<Entity> entities = getDao().queryBuilder()
                .list();

        for (Iterator<Entity> it = entities.iterator(); it.hasNext(); ) {
            Entity entity = it.next();
            if (entity instanceof IExportable && ((IExportable) entity).getDeleted()) {
                it.remove();
                detach(entity);
            }
        }

        return entities;
    }

    /**
     * Insert a batch of Entity, this method dispatch event
     *
     * @param entityList
     */
    public void insertAll(List<Entity> entityList) {
        if (entityList != null && !entityList.isEmpty())
            for (Entity entity : entityList) {
                insert(entity);
            }

    }

    public void insert(Entity entity) {
        if (entity != null) {

            //update field value if Exportable
            if (entity instanceof IExportable) {

                ((IExportable) entity).setDeleted(false);

                if (((IExportable) entity).getCreatedAt() == null)
                    ((IExportable) entity).setCreatedAt(new Date());

                if (hasKey(entity)) {
                    ((IExportable) entity).setIsSync(true);
                    ((IExportable) entity).setSyncDate(new Date());
                    ((IExportable) entity).setRemoteId(entity.getId());
                } else
                    ((IExportable) entity).setIsSync(false);
            }

            //Dispatch pre insert event
            if (this.dispatcher != null)
                this.dispatcher.onBeforeEntityInsert(entity, this);

            getDao().insertOrReplaceInTx(entity);

            //Dispatch post insert event
            if (this.dispatcher != null)
                this.dispatcher.onAfterEntityInsert(entity, this);
        }
    }

    /**
     * Delete entity by key
     *
     * @param Id
     */
    public void delete(Long Id) {

        Entity entity = this.load(Id);
        if (entity != null) {

            //Dispatch pre delete event
            if (this.dispatcher != null)
                this.dispatcher.onBeforeEntityDelete(entity, this);

            if (entity instanceof IExportable && ((IExportable) entity).getIsSync()) {
                ((IExportable) entity).setDeleted(true);
                ((IExportable) entity).setUpdatedAt(new Date());
                getDao().updateInTx(entity);
                this.detach(entity);
            } else
                getDao().deleteByKey(Id);

            //Dispatch post delete event
            if (this.dispatcher != null)
                this.dispatcher.onAfterEntityDelete(entity, this);
        }
    }

    public void delete(Entity entity) {
        if (entity != null) {
            //Dispatch pre delete event
            if (this.dispatcher != null)
                this.dispatcher.onBeforeEntityDelete(entity, this);

            if (entity instanceof IExportable && ((IExportable) entity).getIsSync()) {
                ((IExportable) entity).setDeleted(true);
                ((IExportable) entity).setUpdatedAt(new Date());
                getDao().updateInTx(entity);
            } else
                getDao().delete(entity);

            //Dispatch post delete event
            if (this.dispatcher != null)
                this.dispatcher.onAfterEntityDelete(entity, this);
        }
    }

    /**
     * Delete a batch of Entity, this method do no t dispatch any event
     *
     * @param entityList
     */
    public void deleteAll(List<Entity> entityList) {
        if (entityList != null && !entityList.isEmpty())
            for (Entity entity : entityList) {
                delete(entity);
            }

    }

    public void update(Entity entity) {

        if (entity != null) {

            //update field value if Exportable
            if (entity instanceof IExportable)
                ((IExportable) entity).setUpdatedAt(new Date());

            //Dispatch pre update event
            if (this.dispatcher != null)
                this.dispatcher.onBeforeEntityUpdate(entity, this);

            getDao().updateInTx(entity);

            //Dispatch post update event
            if (this.dispatcher != null)
                this.dispatcher.onAfterEntityUpdate(entity, this);
        }
    }

    /**
     * Update entity without tracking updateAt parameter
     *
     * @param entity
     */
    public void update2(Entity entity) {

        if (entity != null) {
            //Dispatch pre update event
            if (this.dispatcher != null)
                this.dispatcher.onBeforeEntityUpdate(entity, this);

            getDao().updateInTx(entity);

            //Dispatch post update event
            if (this.dispatcher != null)
                this.dispatcher.onAfterEntityUpdate(entity, this);
        }
    }


    public void save(Entity entity) {
        if (hasKey(entity)) {
            update(entity);
        } else {
            insert(entity);
        }
    }

    private boolean hasKey(Entity entity) {
        return entity.getId() != null;
    }

    /**
     * @return QueryBuilder for complex query.
     */
    public QueryBuilder<Entity> queryBuilder() {
        return getDao().queryBuilder();
    }

    public Dao getDao() {
        return dao;
    }

    public void setDao(Dao dao) {
        this.dao = dao;
    }

    public boolean detach(Entity entity) {
        return getDao().detach(entity);
    }
}
