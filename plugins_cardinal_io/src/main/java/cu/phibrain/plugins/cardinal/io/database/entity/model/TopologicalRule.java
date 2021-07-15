package cu.phibrain.plugins.cardinal.io.database.entity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;

/**
 * Created by Ero on 04/05/2021.
 * Entity mapped to table "zone".
 */
@Entity(
        nameInDb = "CARDINAL_TOPOLOGICAL_RULE",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class TopologicalRule implements Serializable, IEntity {
    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("origin")
    @Expose
    private long originId;

    @ToOne(joinProperty = "originId")
    private MapObjecType originObj;

    @SerializedName("target")
    @Expose
    private long targetId;

    @ToOne(joinProperty = "targetId")
    private MapObjecType targetObj;

    private final static long serialVersionUID = -4499872341492642531L;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1078531328)
    private transient TopologicalRuleDao myDao;

    @Generated(hash = 2078706609)
    public TopologicalRule(Long id, long originId, long targetId) {
        this.id = id;
        this.originId = originId;
        this.targetId = targetId;
    }

    @Generated(hash = 1912433237)
    public TopologicalRule() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getOriginId() {
        return this.originId;
    }

    public void setOriginId(long originId) {
        this.originId = originId;
    }

    public long getTargetId() {
        return this.targetId;
    }

    public void setTargetId(long targetId) {
        this.targetId = targetId;
    }

    @Generated(hash = 1240056652)
    private transient Long originObj__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 512353353)
    public MapObjecType getOriginObj() {
        long __key = this.originId;
        if (originObj__resolvedKey == null || !originObj__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjecTypeDao targetDao = daoSession.getMapObjecTypeDao();
            MapObjecType originObjNew = targetDao.load(__key);
            synchronized (this) {
                originObj = originObjNew;
                originObj__resolvedKey = __key;
            }
        }
        return originObj;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 278245810)
    public void setOriginObj(@NotNull MapObjecType originObj) {
        if (originObj == null) {
            throw new DaoException(
                    "To-one property 'originId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.originObj = originObj;
            originId = originObj.getId();
            originObj__resolvedKey = originId;
        }
    }

    @Generated(hash = 355071681)
    private transient Long targetObj__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1127808543)
    public MapObjecType getTargetObj() {
        long __key = this.targetId;
        if (targetObj__resolvedKey == null || !targetObj__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjecTypeDao targetDao = daoSession.getMapObjecTypeDao();
            MapObjecType targetObjNew = targetDao.load(__key);
            synchronized (this) {
                targetObj = targetObjNew;
                targetObj__resolvedKey = __key;
            }
        }
        return targetObj;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1488861554)
    public void setTargetObj(@NotNull MapObjecType targetObj) {
        if (targetObj == null) {
            throw new DaoException(
                    "To-one property 'targetId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.targetObj = targetObj;
            targetId = targetObj.getId();
            targetObj__resolvedKey = targetId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 9960833)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getTopologicalRuleDao() : null;
    }
}
