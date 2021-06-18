package cu.phibrain.plugins.cardinal.io.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(
        nameInDb = "CARDINAL_MAP_OBJECT_HAS_DEFECT",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true,

        indexes = {
                @Index(value = "mapObjectId,mapObjectDefectId", unique = true)
        }
)
public class MapObjectHasDefect implements Serializable {
    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("map_object_barcode")
    @Expose
    private long mapObjectId;

    @ToOne(joinProperty = "mapObjectId")
    private MapObject mapObject;

    @SerializedName("map_object_defect")
    @Expose
    private long mapObjectDefectId;

    @ToOne(joinProperty = "mapObjectDefectId")
    private MapObjecTypeDefect mapObjectDefect;

    @SerializedName("created_at")
    @Expose
    private Date createdAt;

    @ToMany(referencedJoinProperty = "defectId")
    @SerializedName("images_defect")
    @Expose
    public List<MapObjectHasDefectHasImages> images;

    private final static long serialVersionUID = -449987234149264098L;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 674995576)
    private transient MapObjectHasDefectDao myDao;

    @Generated(hash = 1895214310)
    public MapObjectHasDefect(Long id, long mapObjectId, long mapObjectDefectId,
                              Date createdAt) {
        this.id = id;
        this.mapObjectId = mapObjectId;
        this.mapObjectDefectId = mapObjectDefectId;
        this.createdAt = createdAt;
    }

    @Generated(hash = 1575504526)
    public MapObjectHasDefect() {
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

    public long getMapObjectDefectId() {
        return this.mapObjectDefectId;
    }

    public void setMapObjectDefectId(long mapObjectDefectId) {
        this.mapObjectDefectId = mapObjectDefectId;
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

    @Generated(hash = 36891499)
    private transient Long mapObjectDefect__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1016416137)
    public MapObjecTypeDefect getMapObjectDefect() {
        long __key = this.mapObjectDefectId;
        if (mapObjectDefect__resolvedKey == null
                || !mapObjectDefect__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjecTypeDefectDao targetDao = daoSession.getMapObjecTypeDefectDao();
            MapObjecTypeDefect mapObjectDefectNew = targetDao.load(__key);
            synchronized (this) {
                mapObjectDefect = mapObjectDefectNew;
                mapObjectDefect__resolvedKey = __key;
            }
        }
        return mapObjectDefect;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 464431631)
    public void setMapObjectDefect(@NotNull MapObjecTypeDefect mapObjectDefect) {
        if (mapObjectDefect == null) {
            throw new DaoException(
                    "To-one property 'mapObjectDefectId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.mapObjectDefect = mapObjectDefect;
            mapObjectDefectId = mapObjectDefect.getId();
            mapObjectDefect__resolvedKey = mapObjectDefectId;
        }
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1979793312)
    public List<MapObjectHasDefectHasImages> getImages() {
        if (images == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjectHasDefectHasImagesDao targetDao = daoSession
                    .getMapObjectHasDefectHasImagesDao();
            List<MapObjectHasDefectHasImages> imagesNew = targetDao
                    ._queryMapObjectHasDefect_Images(id);
            synchronized (this) {
                if (images == null) {
                    images = imagesNew;
                }
            }
        }
        return images;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 604059028)
    public synchronized void resetImages() {
        images = null;
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
    @Generated(hash = 1055422263)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMapObjectHasDefectDao() : null;
    }
}