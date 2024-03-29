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
import java.util.Date;

@Entity(
        nameInDb = "CARDINAL_MAP_OBJECT_METADATA",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class MapObjectMetadata implements Serializable, IExportable {
    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose(serialize = false)
    private Long id;

    private Long remoteId;

    @SerializedName("map_object")
    @Expose
    private long mapObjectId;

    @ToOne(joinProperty = "mapObjectId")
    @Expose(serialize = false, deserialize = false)
    private MapObject mapObject;

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("obj_attribute")
    @Expose
    private long objAttributeId;

    @ToOne(joinProperty = "objAttributeId")
    @Expose(serialize = false, deserialize = false)
    private MapObjectTypeAttribute attribute;

    private final static long serialVersionUID = -4499234649201958L;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 726544085)
    private transient MapObjectMetadataDao myDao;
    private Date SyncDate;
    private Boolean deleted;

    @Generated(hash = 1780108763)
    public MapObjectMetadata(Long id, Long remoteId, long mapObjectId, String value, long objAttributeId,
            Date SyncDate, Boolean deleted, Date updatedAt, Boolean isSync, Date createdAt) {
        this.id = id;
        this.remoteId = remoteId;
        this.mapObjectId = mapObjectId;
        this.value = value;
        this.objAttributeId = objAttributeId;
        this.SyncDate = SyncDate;
        this.deleted = deleted;
        this.updatedAt = updatedAt;
        this.isSync = isSync;
        this.createdAt = createdAt;
    }

    @Generated(hash = 571951489)
    public MapObjectMetadata() {
    }

    public MapObjectMetadata(Long remoteId, long mapObjectId, String value, long objAttributeId) {
        this.remoteId = remoteId;
        this.mapObjectId = mapObjectId;
        this.value = value;
        this.objAttributeId = objAttributeId;
        this.updatedAt = null;
        this.isSync = false;
        this.createdAt = new Date();
        this.SyncDate = null;
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

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public long getObjAttributeId() {
        return this.objAttributeId;
    }

    public void setObjAttributeId(long objAttributeId) {
        this.objAttributeId = objAttributeId;
    }

    @Generated(hash = 735162862)
    private transient Long attribute__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 256730106)
    public MapObjectTypeAttribute getAttribute() {
        long __key = this.objAttributeId;
        if (attribute__resolvedKey == null || !attribute__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjectTypeAttributeDao targetDao = daoSession.getMapObjectTypeAttributeDao();
            MapObjectTypeAttribute attributeNew = targetDao.load(__key);
            synchronized (this) {
                attribute = attributeNew;
                attribute__resolvedKey = __key;
            }
        }
        return attribute;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 754356559)
    public void setAttribute(@NotNull MapObjectTypeAttribute attribute) {
        if (attribute == null) {
            throw new DaoException(
                    "To-one property 'objAttributeId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.attribute = attribute;
            objAttributeId = attribute.getId();
            attribute__resolvedKey = objAttributeId;
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

    // Region de los datos de ayuda en la syncronización
    @Expose(serialize = false, deserialize = false)
    private Date updatedAt;

    @Expose(serialize = false, deserialize = false)
    private Boolean isSync;

    @Expose(serialize = false, deserialize = false)
    private Date createdAt;

    @Generated(hash = 1028776990)
    private transient Long mapObject__resolvedKey;

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

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
        MapObjectMetadata meta = new MapObjectMetadata(remoteId, getMapObject().getRemoteId(), value, objAttributeId);
        meta.setDeleted(deleted);
        return meta;
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
        return this.deleted ;
    }

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

    public Date getSyncDate() {
        return this.SyncDate;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2058218660)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMapObjectMetadataDao() : null;
    }


}
