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
import java.util.Collections;
import java.util.Date;
import java.util.List;

import eu.geopaparazzi.library.kml.KmlRepresenter;

@Entity(
        nameInDb = "CARDINAL_MAP_OBJECT_IMAGES",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class MapObjectImages implements Serializable, IExportable, KmlRepresenter {
    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose(serialize = false)
    private Long id;

    private Long remoteId;

    @SerializedName("map_object")
    @Expose
    private long mapObjectId;

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

    @ToOne(joinProperty = "mapObjectId")
    @Expose(serialize = false, deserialize = false)
    private MapObject mapObject;


    private final static long serialVersionUID = -4499234149200958L;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 188420039)
    private transient MapObjectImagesDao myDao;

    @Generated(hash = 1028776990)
    private transient Long mapObject__resolvedKey;
    private Date SyncDate;

    public MapObjectImages(Long remoteId, long mapObjectId, byte[] image, Date createdAt, double lon,
                           double lat, double elevation, double azimuth) {
        this.remoteId = remoteId;
        this.mapObjectId = mapObjectId;
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

    @Generated(hash = 1283955147)
    public MapObjectImages(Long id, Long remoteId, long mapObjectId, byte[] image, Date createdAt, double lon,
            double lat, double elevation, double azimuth, Date SyncDate, Date updatedAt, Boolean isSync) {
        this.id = id;
        this.remoteId = remoteId;
        this.mapObjectId = mapObjectId;
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

    @Generated(hash = 212149383)
    public MapObjectImages() {
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

    public byte[] getImage() {
        return this.image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    @Override
    public Date getCreatedAt() {
        return this.createdAt;
    }

    @Override
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

    /**
     * @return the timestamp.
     */
    public long getTs() {
        return this.createdAt.getTime();
    }

    /**
     * Transforms the object in its kml representation.
     *
     * @return the kml representation.
     * @throws Exception if something goes wrong.
     */
    @Override
    public String toKmlString() throws Exception {
        StringBuilder sB = new StringBuilder();
        String name = getName();
        sB.append("<Placemark>\n");
        if (name != null && name.length() > 0) {
            sB.append("<name>").append(name).append("</name>\n");
        } else {
            sB.append("<name>").append(this.getTs()).append("</name>\n");
        }
        sB.append("<description><![CDATA[<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">\n");
        sB.append("<html><head><title></title>");
        sB.append("<meta http-equiv=\"Content-Type\" content=\"text/html; charset=iso-8859-1\">");
        sB.append("</head><body>");
        sB.append("<img src=\"" + name + "\" width=\"300\">");
        sB.append("</body></html>]]></description>\n");
        sB.append("<styleUrl>#camera-icon</styleUrl>\n");
        sB.append("<Point>\n");
        sB.append("<coordinates>").append(lon).append(",").append(lat).append(",").append(elevation);
        sB.append("</coordinates>\n");
        sB.append("</Point>\n");
        sB.append("</Placemark>\n");

        return sB.toString();
    }

    /**
     * Getter for image flag.
     *
     * @return <code>true</code> if the object has also an image that needs to be embedded in the kmz.
     */
    @Override
    public boolean hasImages() {
        return true;
    }

    /**
     * Get image ids list.
     *
     * @return the list of image ids.
     */
    @Override
    public List<String> getImageIds() {
        return Collections.singletonList(id + "");
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

    public String getName() {
        StringBuilder sB = new StringBuilder();
        sB.append("IMG_").append(id).append("_OF_").append(getMapObject().getCode()).append(".jpg");
        return sB.toString();

    }


    // Region de los datos de ayuda en la syncronización

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
        return new MapObjectImages(remoteId, getMapObject().getRemoteId(), image, createdAt, lon, lat, elevation, azimuth);
    }

    @Override
    public Long getRemoteId() {
        return this.remoteId;
    }

    @Override
    public void setRemoteId(Long remoteId) {
        this.remoteId = remoteId;
    }

    public Date getSyncDate() {
        return this.SyncDate;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1486978210)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMapObjectImagesDao() : null;
    }
}
