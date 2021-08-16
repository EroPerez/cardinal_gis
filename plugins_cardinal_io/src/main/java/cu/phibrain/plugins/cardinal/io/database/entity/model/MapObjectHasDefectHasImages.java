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
        nameInDb = "CARDINAL_MAP_OBJECT_HAS_DEFECT_HAS_IMAGES",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class MapObjectHasDefectHasImages implements Serializable, IExportable {
    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose(serialize = false)
    private Long id;

    private Long remoteId;

    @SerializedName("defect")
    @Expose
    private long defectId;

    @ToOne(joinProperty = "defectId")
    @Expose(serialize = false, deserialize = false)
    private MapObjectHasDefect defectObj;

    @SerializedName("filename")
    @Expose
    private byte[] image;

    @SerializedName("created_at")
    @Expose
    private Date createdAt;

    @SerializedName("longitude")
    @Expose
    double lon;
    @SerializedName("latitude")
    @Expose
    double lat;
    @SerializedName("elevation")
    @Expose
    double elevation;

    @SerializedName("azimuth")
    @Expose
    double azimuth;

    private final static long serialVersionUID = -4499872341492642958L;
    private Date SyncDate;

    @Generated(hash = 1814808325)
    public MapObjectHasDefectHasImages(Long id, Long remoteId, long defectId, byte[] image, Date createdAt, double lon, double lat,
                                       double elevation, double azimuth, Date SyncDate, Date updatedAt, Boolean isSync) {
        this.id = id;
        this.remoteId = remoteId;
        this.defectId = defectId;
        this.image = image;
        this.createdAt = createdAt;
        this.lon = lon;
        this.lat = lat;
        this.elevation = elevation;
        this.azimuth = azimuth;
        this.SyncDate = SyncDate;
        this.updatedAt = updatedAt;
        this.isSync = isSync;
    }

    @Generated(hash = 576647378)
    public MapObjectHasDefectHasImages() {
    }

    public MapObjectHasDefectHasImages(Long remoteId, long defectId, byte[] image, Date createdAt, double lon,
                                       double lat, double elevation, double azimuth) {
        this.remoteId = remoteId;
        this.defectId = defectId;
        this.image = image;
        this.createdAt = createdAt;
        this.lon = lon;
        this.lat = lat;
        this.elevation = elevation;
        this.azimuth = azimuth;
        this.SyncDate = null;
        this.updatedAt = null;
        this.isSync = false;

    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDefectId() {
        return this.defectId;
    }

    public void setDefectId(long defectId) {
        this.defectId = defectId;
    }

    public byte[] getImage() {
        return this.image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public double getLon() {
        return this.lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getElevation() {
        return this.elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public double getAzimuth() {
        return this.azimuth;
    }

    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }

    // Region de los datos de ayuda en la syncronización

    @Expose(serialize = false, deserialize = false)
    private Date updatedAt;

    @Expose(serialize = false, deserialize = false)
    private Boolean isSync;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1825896793)
    private transient MapObjectHasDefectHasImagesDao myDao;

    @Generated(hash = 1008767752)
    private transient Long defectObj__resolvedKey;

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
    public Boolean mustExport() {
        return !isSync || (updatedAt != null && SyncDate.before(updatedAt));
    }

    @Override
    public IExportable toRemoteObject() {
        return new MapObjectHasDefectHasImages(remoteId, getDefectObj().getRemoteId(), image, createdAt, lon, lat, elevation, azimuth);
    }

    @Override
    public Long getRemoteId() {
        return remoteId;
    }

    @Override
    public void setRemoteId(Long remoteId) {
        this.remoteId = remoteId;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1421414891)
    public MapObjectHasDefect getDefectObj() {
        long __key = this.defectId;
        if (defectObj__resolvedKey == null || !defectObj__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjectHasDefectDao targetDao = daoSession.getMapObjectHasDefectDao();
            MapObjectHasDefect defectObjNew = targetDao.load(__key);
            synchronized (this) {
                defectObj = defectObjNew;
                defectObj__resolvedKey = __key;
            }
        }
        return defectObj;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1529622812)
    public void setDefectObj(@NotNull MapObjectHasDefect defectObj) {
        if (defectObj == null) {
            throw new DaoException(
                    "To-one property 'defectId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.defectObj = defectObj;
            defectId = defectObj.getId();
            defectObj__resolvedKey = defectId;
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

    public Date getSyncDate() {
        return this.SyncDate;
    }

    public void setSyncDate(Date SyncDate) {
        this.SyncDate = SyncDate;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1008629471)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMapObjectHasDefectHasImagesDao() : null;
    }


}
