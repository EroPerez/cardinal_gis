
package cu.phibrain.plugins.cardinal.io.database.entity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
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
import java.util.Objects;

import cu.phibrain.plugins.cardinal.io.database.entity.model.converter.GPGeoPointListConverter;
import eu.geopaparazzi.map.GPGeoPoint;

/**
 * Created by Ero on 04/05/2021.
 * Entity mapped to table "zone".
 */
@Entity(
        nameInDb = "CARDINAL_MAP_OBJECT",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true,

        indexes = {
                @Index(value = "sessionId,code", unique = true)
        }
)
public class MapObject implements Serializable, IEntity {

    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("joined")
    @Expose
    private long joinId;

    @ToOne(joinProperty = "joinId")
    private MapObject joinObj;

    @SerializedName("map_object_type")
    @Expose
    private Long mapObjectTypeId;

    @ToOne(joinProperty = "mapObjectTypeId")
    private MapObjecType objectType;

    @Convert(converter = GPGeoPointListConverter.class, columnType = String.class)
    @SerializedName("coord")
    @Expose
    private List<GPGeoPoint> coord;

    @SerializedName("elevation")
    @Expose
    double elevation ;

    @SerializedName("observation")
    @Expose
    private String observation;

    @SerializedName("created_at")
    @Expose
    private Date createdAt;

    @SerializedName("sincronized")
    @Expose
    private Boolean isSync;

    @SerializedName("sincronized_date")
    @Expose
    private Date SyncDate;

    @SerializedName("node_grade")
    @Expose
    private long nodeGrade;

    @ToMany(referencedJoinProperty = "originId")
    @SerializedName("edges")
    @Expose
    private List<RouteSegment> routeSegments;

    @ToMany(referencedJoinProperty = "mapObjectId")
    @SerializedName("states")
    @Expose
    private List<MapObjectHasState> states;

    @ToMany(referencedJoinProperty = "mapObjectId")
    @SerializedName("defects")
    @Expose
    private List<MapObjectHasDefect> defects;

    @SerializedName("stock_code")
    @Expose
    private long stockCodeId;

    @ToOne(joinProperty = "stockCodeId")
    private Stock stockCode;

    @SerializedName("work_session")
    @Expose
    private long sessionId;

    @ToOne(joinProperty = "sessionId")
    private WorkSession session;

    @ToMany(referencedJoinProperty = "mapObjectId")
    @SerializedName("object_images")
    @Expose
    private List<MapObjectImages> images;

    @ToMany(referencedJoinProperty = "mapObjectId")
    @SerializedName("metadata")
    @Expose
    private List<MapObjectMetadata> metadata;

    @SerializedName("is_completed")
    @Expose
    private Boolean isCompleted;


    @ToMany(referencedJoinProperty = "joinId")
     private List<MapObject> joined;

    private final static long serialVersionUID = -4499872341492642530L;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 48110761)
    private transient MapObjectDao myDao;


    @Generated(hash = 987521458)
    public MapObject(Long id, String code, long joinId, Long mapObjectTypeId, List<GPGeoPoint> coord,
            double elevation, String observation, Date createdAt, Boolean isSync, Date SyncDate,
            long nodeGrade, long stockCodeId, long sessionId, Boolean isCompleted) {
        this.id = id;
        this.code = code;
        this.joinId = joinId;
        this.mapObjectTypeId = mapObjectTypeId;
        this.coord = coord;
        this.elevation = elevation;
        this.observation = observation;
        this.createdAt = createdAt;
        this.isSync = isSync;
        this.SyncDate = SyncDate;
        this.nodeGrade = nodeGrade;
        this.stockCodeId = stockCodeId;
        this.sessionId = sessionId;
        this.isCompleted = isCompleted;
    }


    @Generated(hash = 705302497)
    public MapObject() {
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public String getCode() {
        return this.code;
    }


    public void setCode(String code) {
        this.code = code;
    }


    public long getJoinId() {
        return this.joinId;
    }


    public void setJoinId(long joinId) {
        this.joinId = joinId;
    }


    public Long getMapObjectTypeId() {
        return this.mapObjectTypeId;
    }


    public void setMapObjectTypeId(Long mapObjectTypeId) {
        this.mapObjectTypeId = mapObjectTypeId;
    }


    public List<GPGeoPoint> getCoord() {
        return this.coord;
    }

    public void setCoord(List<GPGeoPoint> coord) {
        this.coord = coord;
    }


    public String getObservation() {
        return this.observation;
    }


    public void setObservation(String observation) {
        this.observation = observation;
    }


    public Date getCreatedAt() {
        return this.createdAt;
    }


    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }


    public Boolean getIsSync() {
        return this.isSync;
    }


    public void setIsSync(Boolean isSync) {
        this.isSync = isSync;
    }


    public Date getSyncDate() {
        return this.SyncDate;
    }


    public void setSyncDate(Date SyncDate) {
        this.SyncDate = SyncDate;
    }


    public long getNodeGrade() {
        return this.nodeGrade;
    }


    public void setNodeGrade(long nodeGrade) {
        this.nodeGrade = nodeGrade;
    }


    public long getStockCodeId() {
        return this.stockCodeId;
    }


    public void setStockCodeId(long stockCodeId) {
        this.stockCodeId = stockCodeId;
    }


    public long getSessionId() {
        return this.sessionId;
    }


    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }


    @Generated(hash = 979072575)
    private transient Long joinObj__resolvedKey;


    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1544046183)
    public MapObject getJoinObj() {
        long __key = this.joinId;
        if (joinObj__resolvedKey == null || !joinObj__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjectDao targetDao = daoSession.getMapObjectDao();
            MapObject joinObjNew = targetDao.load(__key);
            synchronized (this) {
                joinObj = joinObjNew;
                joinObj__resolvedKey = __key;
            }
        }
        return joinObj;
    }


    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1375760507)
    public void setJoinObj(@NotNull MapObject joinObj) {
        if (joinObj == null) {
            throw new DaoException(
                    "To-one property 'joinId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.joinObj = joinObj;
            joinId = joinObj.getId();
            joinObj__resolvedKey = joinId;
        }
    }


    @Generated(hash = 1904019615)
    private transient Long objectType__resolvedKey;


    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 2031550832)
    public MapObjecType getObjectType() {
        Long __key = this.mapObjectTypeId;
        if (objectType__resolvedKey == null || !objectType__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjecTypeDao targetDao = daoSession.getMapObjecTypeDao();
            MapObjecType objectTypeNew = targetDao.load(__key);
            synchronized (this) {
                objectType = objectTypeNew;
                objectType__resolvedKey = __key;
            }
        }
        return objectType;
    }


    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1116319821)
    public void setObjectType(MapObjecType objectType) {
        synchronized (this) {
            this.objectType = objectType;
            mapObjectTypeId = objectType == null ? null : objectType.getId();
            objectType__resolvedKey = mapObjectTypeId;
        }
    }


    @Generated(hash = 1857934689)
    private transient Long stockCode__resolvedKey;


    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 242754685)
    public Stock getStockCode() {
        long __key = this.stockCodeId;
        if (stockCode__resolvedKey == null || !stockCode__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StockDao targetDao = daoSession.getStockDao();
            Stock stockCodeNew = targetDao.load(__key);
            synchronized (this) {
                stockCode = stockCodeNew;
                stockCode__resolvedKey = __key;
            }
        }
        return stockCode;
    }


    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 889399643)
    public void setStockCode(@NotNull Stock stockCode) {
        if (stockCode == null) {
            throw new DaoException(
                    "To-one property 'stockCodeId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.stockCode = stockCode;
            stockCodeId = stockCode.getId();
            stockCode__resolvedKey = stockCodeId;
        }
    }


    @Generated(hash = 274049648)
    private transient Long session__resolvedKey;


    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 412766052)
    public WorkSession getSession() {
        long __key = this.sessionId;
        if (session__resolvedKey == null || !session__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WorkSessionDao targetDao = daoSession.getWorkSessionDao();
            WorkSession sessionNew = targetDao.load(__key);
            synchronized (this) {
                session = sessionNew;
                session__resolvedKey = __key;
            }
        }
        return session;
    }


    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1329887016)
    public void setSession(@NotNull WorkSession session) {
        if (session == null) {
            throw new DaoException(
                    "To-one property 'sessionId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.session = session;
            sessionId = session.getId();
            session__resolvedKey = sessionId;
        }
    }


    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1246147773)
    public List<RouteSegment> getRouteSegments() {
        if (routeSegments == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            RouteSegmentDao targetDao = daoSession.getRouteSegmentDao();
            List<RouteSegment> routeSegmentsNew = targetDao._queryMapObject_RouteSegments(id);
            synchronized (this) {
                if (routeSegments == null) {
                    routeSegments = routeSegmentsNew;
                }
            }
        }
        return routeSegments;
    }


    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1221734057)
    public synchronized void resetRouteSegments() {
        routeSegments = null;
    }


    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 493969114)
    public List<MapObjectHasState> getStates() {
        if (states == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjectHasStateDao targetDao = daoSession.getMapObjectHasStateDao();
            List<MapObjectHasState> statesNew = targetDao._queryMapObject_States(id);
            synchronized (this) {
                if (states == null) {
                    states = statesNew;
                }
            }
        }
        return states;
    }


    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 63018479)
    public synchronized void resetStates() {
        states = null;
    }


    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1189879345)
    public List<MapObjectHasDefect> getDefects() {
        if (defects == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjectHasDefectDao targetDao = daoSession.getMapObjectHasDefectDao();
            List<MapObjectHasDefect> defectsNew = targetDao._queryMapObject_Defects(id);
            synchronized (this) {
                if (defects == null) {
                    defects = defectsNew;
                }
            }
        }
        return defects;
    }


    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 494192730)
    public synchronized void resetDefects() {
        defects = null;
    }


    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 57029152)
    public List<MapObjectImages> getImages() {
        if (images == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjectImagesDao targetDao = daoSession.getMapObjectImagesDao();
            List<MapObjectImages> imagesNew = targetDao._queryMapObject_Images(id);
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
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1078952291)
    public List<MapObjectMetadata> getMetadata() {
        if (metadata == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjectMetadataDao targetDao = daoSession.getMapObjectMetadataDao();
            List<MapObjectMetadata> metadataNew = targetDao._queryMapObject_Metadata(id);
            synchronized (this) {
                if (metadata == null) {
                    metadata = metadataNew;
                }
            }
        }
        return metadata;
    }


    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 299296490)
    public synchronized void resetMetadata() {
        metadata = null;
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


    public Boolean getIsCompleted() {
        return this.isCompleted;
    }


    public void setIsCompleted(Boolean isCompleted) {
        this.isCompleted = isCompleted;
    }


    /** Get a map object type layer */
    public Layer getLayer(){
        return this.getObjectType().getLayerObj();
    }


    public double getElevation() {
        return this.elevation;
    }


    public void setElevation(double elevation) {
        this.elevation = elevation;
    }


    @Override
    public String toString() {
        return "MapObject{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", joinId=" + joinId +
                ", mapObjectTypeId=" + mapObjectTypeId +
                ", elevation=" + elevation +
                ", observation='" + observation + '\'' +
                ", createdAt=" + createdAt +
                ", nodeGrade=" + nodeGrade +
                ", stockCodeId=" + stockCodeId +
                ", sessionId=" + sessionId +
                ", isCompleted=" + isCompleted +
                '}';
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MapObject mapObject = (MapObject) o;
        return sessionId == mapObject.sessionId &&
                id.equals(mapObject.id) &&
                code.equals(mapObject.code) &&
                mapObjectTypeId.equals(mapObject.mapObjectTypeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, code, mapObjectTypeId, sessionId);
    }


    public boolean isTerminal(){
        return this.getObjectType().getIsTerminal();
    }


    public boolean belongToTopoLayer(){
        return getLayer().getIsTopology();
    }


    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 133373295)
    public List<MapObject> getJoined() {
        if (joined == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjectDao targetDao = daoSession.getMapObjectDao();
            List<MapObject> joinedNew = targetDao._queryMapObject_Joined(id);
            synchronized (this) {
                if (joined == null) {
                    joined = joinedNew;
                }
            }
        }
        return joined;
    }


    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 867267577)
    public synchronized void resetJoined() {
        joined = null;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 788755102)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMapObjectDao() : null;
    }
}
