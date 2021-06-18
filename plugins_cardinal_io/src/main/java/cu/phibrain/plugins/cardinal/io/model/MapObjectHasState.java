package cu.phibrain.plugins.cardinal.io.model;

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
 * Created by Ero on 15/02/2021.
 * Entity mapped to table "MapObjectHasDefect".
 */
@Entity(
        nameInDb = "CARDINAL_MAP_OBJECT_HAS_STATE",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true,

        indexes = {
                @Index(value = "mapObjectId,mapObjectStateId", unique = true)
        }
)
public class MapObjectHasState implements Serializable {
    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("map_object_barcode")
    @Expose
    private long mapObjectId;

    @ToOne(joinProperty = "mapObjectId")
    private MapObject mapObject;

    @SerializedName("map_object_state")
    @Expose
    private long mapObjectStateId;

    @ToOne(joinProperty = "mapObjectStateId")
    private MapObjecTypeState mapObjectState;

    @SerializedName("created_at")
    @Expose
    private Date createdAt;


    private final static long serialVersionUID = -4499872341492642588L;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 779550990)
    private transient MapObjectHasStateDao myDao;


    @Generated(hash = 1261174875)
    public MapObjectHasState(Long id, long mapObjectId, long mapObjectStateId,
                             Date createdAt) {
        this.id = id;
        this.mapObjectId = mapObjectId;
        this.mapObjectStateId = mapObjectStateId;
        this.createdAt = createdAt;
    }


    @Generated(hash = 1884819685)
    public MapObjectHasState() {
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public long getMapObjectId() {
        return this.mapObjectId;
    }


    public void setMapObjectId(long mapObjectId) {
        this.mapObjectId = mapObjectId;
    }


    public long getMapObjectStateId() {
        return this.mapObjectStateId;
    }


    public void setMapObjectStateId(long mapObjectStateId) {
        this.mapObjectStateId = mapObjectStateId;
    }


    public Date getCreatedAt() {
        return this.createdAt;
    }


    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    @Generated(hash = 1028776990)
    private transient Long mapObject__resolvedKey;


    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1863170507)
    public MapObject getMapObject() {
        long __key = this.mapObjectId;
        if (mapObject__resolvedKey == null || !mapObject__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjectDao targetDao = daoSession.getMapObjectDao();
            MapObject mapObjectNew = targetDao.load(__key);
            synchronized (this) {
                mapObject = mapObjectNew;
                mapObject__resolvedKey = __key;
            }
        }
        return mapObject;
    }


    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 913991379)
    public void setMapObject(@NotNull MapObject mapObject) {
        if (mapObject == null) {
            throw new DaoException(
                    "To-one property 'mapObjectId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.mapObject = mapObject;
            mapObjectId = mapObject.getId();
            mapObject__resolvedKey = mapObjectId;
        }
    }


    @Generated(hash = 986862629)
    private transient Long mapObjectState__resolvedKey;


    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1444372624)
    public MapObjecTypeState getMapObjectState() {
        long __key = this.mapObjectStateId;
        if (mapObjectState__resolvedKey == null
                || !mapObjectState__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjecTypeStateDao targetDao = daoSession.getMapObjecTypeStateDao();
            MapObjecTypeState mapObjectStateNew = targetDao.load(__key);
            synchronized (this) {
                mapObjectState = mapObjectStateNew;
                mapObjectState__resolvedKey = __key;
            }
        }
        return mapObjectState;
    }


    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 2031520040)
    public void setMapObjectState(@NotNull MapObjecTypeState mapObjectState) {
        if (mapObjectState == null) {
            throw new DaoException(
                    "To-one property 'mapObjectStateId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.mapObjectState = mapObjectState;
            mapObjectStateId = mapObjectState.getId();
            mapObjectState__resolvedKey = mapObjectStateId;
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
    @Generated(hash = 760567419)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMapObjectHasStateDao() : null;
    }
}