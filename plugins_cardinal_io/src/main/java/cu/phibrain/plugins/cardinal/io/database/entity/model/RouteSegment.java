package cu.phibrain.plugins.cardinal.io.database.entity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;
import java.util.Date;


/**
 * Created by Ero on 04/05/2021.
 * Entity mapped to table "RouteSegment".
 */
@Entity(
        nameInDb = "CARDINAL_ROUTE_SEGMENT",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true,

        indexes = {
                @Index(value = "originId,destinyId", unique = true)
        }
)
public class RouteSegment implements Serializable, IExportable {

    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose(serialize = false)
    private Long id;

    private Long remoteId;

    @SerializedName("origin")
    @Expose
    private long originId;

    @ToOne(joinProperty = "originId")
    private MapObject originObj;

    @SerializedName("destiny")
    @Expose
    private long destinyId;

    @ToOne(joinProperty = "destinyId")
    private MapObject destinyObj;

    @SerializedName("created_at")
    @Expose
    private Date createdAt;


    private final static long serialVersionUID = -4499872341492642530L;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 355299170)
    private transient RouteSegmentDao myDao;
    private Date SyncDate;
    private Boolean deleted;


    @Generated(hash = 387422645)
    public RouteSegment(Long id, Long remoteId, long originId, long destinyId, Date createdAt,
                        Date SyncDate, Boolean deleted, Date updatedAt, Boolean isSync) {
        this.id = id;
        this.remoteId = remoteId;
        this.originId = originId;
        this.destinyId = destinyId;
        this.createdAt = createdAt;
        this.SyncDate = SyncDate;
        this.deleted = deleted;
        this.updatedAt = updatedAt;
        this.isSync = isSync;
    }

    public RouteSegment(Long remoteId, long originId, long destinyId) {
        this.id = null;
        this.remoteId = remoteId;
        this.originId = originId;
        this.destinyId = destinyId;
        this.createdAt = new Date();
        this.SyncDate = null;
        this.updatedAt = null;
        this.isSync = false;
    }


    @Generated(hash = 433593099)
    public RouteSegment() {
    }


    @Override
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


    public long getDestinyId() {
        return this.destinyId;
    }


    public void setDestinyId(long destinyId) {
        this.destinyId = destinyId;
    }

    @Override
    public Date getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    @Generated(hash = 1240056652)
    private transient Long originObj__resolvedKey;


    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 512731049)
    public MapObject getOriginObj() {
        long __key = this.originId;
        if (originObj__resolvedKey == null || !originObj__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjectDao targetDao = daoSession.getMapObjectDao();
            MapObject originObjNew = targetDao.load(__key);
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
    @Generated(hash = 990646929)
    public void setOriginObj(@NotNull MapObject originObj) {
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


    @Generated(hash = 274289781)
    private transient Long destinyObj__resolvedKey;


    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 147799781)
    public MapObject getDestinyObj() {
        long __key = this.destinyId;
        if (destinyObj__resolvedKey == null || !destinyObj__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjectDao targetDao = daoSession.getMapObjectDao();
            MapObject destinyObjNew = targetDao.load(__key);
            synchronized (this) {
                destinyObj = destinyObjNew;
                destinyObj__resolvedKey = __key;
            }
        }
        return destinyObj;
    }


    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 779157199)
    public void setDestinyObj(@NotNull MapObject destinyObj) {
        if (destinyObj == null) {
            throw new DaoException(
                    "To-one property 'destinyId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.destinyObj = destinyObj;
            destinyId = destinyObj.getId();
            destinyObj__resolvedKey = destinyId;
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


    @Expose(serialize = false, deserialize = false)
    private Date updatedAt;

    @Expose(serialize = false, deserialize = false)
    private Boolean isSync;


    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public void setUpdatedAt(Date updated) {
        this.updatedAt = updated;
    }

    @Override
    public Boolean getIsSync() {
        return isSync;
    }

    @Override
    public void setIsSync(Boolean isSync) {
        this.isSync = isSync;
    }

    @Override
    public void setSyncDate(Date SyncDate) {
        this.SyncDate = SyncDate;
    }

    @Override
    public Boolean mustExport() {
        return !isSync || (updatedAt != null && SyncDate.before(updatedAt));
    }

    @Override
    public IExportable toRemoteObject() {
        try {
            MapObject origin = getOriginObj();
            MapObject destiny = getDestinyObj();
            RouteSegment route = new RouteSegment(remoteId, origin.getRemoteId(), destiny.getRemoteId());
            route.setDeleted(deleted);
            return route;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public Long getRemoteId() {
        return remoteId;
    }

    @Override
    public void setRemoteId(Long remoteId) {
        this.remoteId = remoteId;
    }

    @Override
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public Boolean getDeleted() {
        return this.deleted;
    }


    public Date getSyncDate() {
        return this.SyncDate;
    }

    @Override
    public String toString() {
        return "RouteSegment{" +
                "origin=" + originId +
                ", destiny=" + destinyId +
                ", remoteId=" + remoteId +
                ", createdAt=" + createdAt +
                '}';
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1613250219)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getRouteSegmentDao() : null;
    }
}
