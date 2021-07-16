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

@Entity(
        nameInDb = "CARDINAL_MAP_OBJECT_METADATA",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class MapObjectMetadata implements Serializable, IEntity{
    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("map_object")
    @Expose
    private long mapObjectId;

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("obj_attribute")
    @Expose
    private long objAttributeId;

    @ToOne(joinProperty = "objAttributeId")
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

    @Generated(hash = 182277423)
    public MapObjectMetadata(Long id, long mapObjectId, String value, long objAttributeId) {
        this.id = id;
        this.mapObjectId = mapObjectId;
        this.value = value;
        this.objAttributeId = objAttributeId;
    }

    @Generated(hash = 571951489)
    public MapObjectMetadata() {
    }

    public MapObjectMetadata( long mapObjectId, String value, long objAttributeId) {
        this.mapObjectId = mapObjectId;
        this.value = value;
        this.objAttributeId = objAttributeId;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 2058218660)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMapObjectMetadataDao() : null;
    }


}
