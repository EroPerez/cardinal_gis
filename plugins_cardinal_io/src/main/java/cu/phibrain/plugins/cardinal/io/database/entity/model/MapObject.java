
package cu.phibrain.plugins.cardinal.io.database.entity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Transient;
import org.locationtech.jts.geom.Point;
import org.oscim.core.GeoPoint;
import org.oscim.utils.geom.GeomBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import cu.phibrain.plugins.cardinal.io.database.entity.model.converter.GPGeoPointListConverter;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.gpx.GpxRepresenter;
import eu.geopaparazzi.library.gpx.GpxUtilities;
import eu.geopaparazzi.library.kml.KmlRepresenter;
import eu.geopaparazzi.library.util.TimeUtilities;
import eu.geopaparazzi.library.util.Utilities;
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
        generateGettersSetters = true

//        indexes = {
//                @Index(value = "sessionId,code", unique = true)
//        }
)
public class MapObject implements Serializable, IExportable, KmlRepresenter, GpxRepresenter {

    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose(serialize = false)
    private Long id;

    private Long remoteId;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("joined")
    @Expose
    private Long joinId;

    @ToOne(joinProperty = "joinId")
    @Expose(serialize = false)
    private MapObject joinObj;

    @SerializedName("map_object_type")
    @Expose
    private Long mapObjectTypeId;

    @ToOne(joinProperty = "mapObjectTypeId")
    @Expose(serialize = false)
    private MapObjecType objectType;

    @Convert(converter = GPGeoPointListConverter.class, columnType = String.class)
    @SerializedName("coord")
    @Expose
    private List<GPGeoPoint> coord;

    @SerializedName("elevation")
    @Expose
    double elevation;

    @SerializedName("observation")
    @Expose
    private String observation;

    @SerializedName("created_at")
    @Expose(serialize = false)
    private Date createdAt;

    @Expose(serialize = false, deserialize = false)
    private Date updatedAt;

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
    @Expose(serialize = false)
    private List<RouteSegment> routeSegments;

    @ToMany(referencedJoinProperty = "mapObjectId")
    @SerializedName("states")
    @Expose(serialize = false)
    private List<MapObjectHasState> states;

    @ToMany(referencedJoinProperty = "mapObjectId")
    @SerializedName("defects")
    @Expose(serialize = false)
    private List<MapObjectHasDefect> defects;

    @SerializedName("stock_code")
    @Expose
    private Long stockCodeId;

    @ToOne(joinProperty = "stockCodeId")
    @Expose(serialize = false)
    private Stock stockCode;

    @SerializedName("work_session")
    @Expose
    private Long sessionId;

    @ToOne(joinProperty = "sessionId")
    @Expose(serialize = false)
    private WorkSession session;

    @ToMany(referencedJoinProperty = "mapObjectId")
    @SerializedName("object_images")
    @Expose(serialize = false)
    private List<MapObjectImages> images;

    @ToMany(referencedJoinProperty = "mapObjectId")
    @SerializedName("metadata")
    @Expose(serialize = false)
    private List<MapObjectMetadata> metadata;

    @SerializedName("is_completed")
    @Expose
    private Boolean isCompleted;


    @ToMany(referencedJoinProperty = "joinId")
    @Expose(serialize = false, deserialize = false)
    private List<MapObject> joinedList;

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

    /**
     * Used to mark as deleted
     */
    private Boolean deleted;

    /**
     * Used set and active object when object has coupled object.
     */
    private transient MapObject activeObject;


    @Generated(hash = 226692444)
    public MapObject(
            Long id,
            Long remoteId,
            String code,
            Long joinId,
            Long mapObjectTypeId,
            List<GPGeoPoint> coord,
            double elevation,
            String observation,
            Date createdAt,
            Date updatedAt,
            Boolean isSync,
            Date SyncDate,
            long nodeGrade,
            Long stockCodeId,
            Long sessionId,
            Boolean isCompleted,
            Boolean deleted) {
        this.id = id;
        this.remoteId = remoteId;
        this.code = code;
        this.joinId = joinId;
        this.mapObjectTypeId = mapObjectTypeId;
        this.coord = coord;
        this.elevation = elevation;
        this.observation = observation;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isSync = isSync;
        this.SyncDate = SyncDate;
        this.nodeGrade = nodeGrade;
        this.stockCodeId = stockCodeId;
        this.sessionId = sessionId;
        this.isCompleted = isCompleted;
        this.deleted = deleted;
    }

    @Generated(hash = 705302497)
    public MapObject() {
    }

    public MapObject(
            Long id,
            Long remoteId,
            String code,
            Long joinId,
            Long mapObjectTypeId,
            List<GPGeoPoint> coord,
            double elevation,
            String observation,
            Boolean isSync,
            Date SyncDate,
            long nodeGrade,
            Long stockCodeId,
            Long sessionId,
            Boolean isCompleted) {
        this.id = id;
        this.remoteId = remoteId;
        this.code = code;
        this.joinId = joinId;
        this.mapObjectTypeId = mapObjectTypeId;
        this.coord = coord;
        this.elevation = elevation;
        this.observation = observation;
        this.isSync = isSync;
        this.SyncDate = SyncDate;
        this.nodeGrade = nodeGrade;
        this.stockCodeId = stockCodeId;
        this.sessionId = sessionId;
        this.isCompleted = isCompleted;
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

    public Long getJoinId() {
        return this.joinId;
    }

    public void setJoinId(Long joinId) {
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

    @Override
    public Date getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Boolean getIsSync() {
        return this.isSync;
    }

    @Override
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

    public Long getStockCodeId() {
        return this.stockCodeId;
    }

    public void setStockCodeId(Long stockCodeId) {
        this.stockCodeId = stockCodeId;
    }

    public Long getSessionId() {
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
    @Generated(hash = 611248148)
    public MapObject getJoinObj() {
        Long __key = this.joinId;
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
    @Generated(hash = 442678596)
    public void setJoinObj(MapObject joinObj) {
        synchronized (this) {
            this.joinObj = joinObj;
            joinId = joinObj == null ? null : joinObj.getId();
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
    @Generated(hash = 94143268)
    public Stock getStockCode() {
        Long __key = this.stockCodeId;
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
    @Generated(hash = 1125524599)
    public void setStockCode(Stock stockCode) {
        synchronized (this) {
            this.stockCode = stockCode;
            stockCodeId = stockCode == null ? null : stockCode.getId();
            stockCode__resolvedKey = stockCodeId;
        }
    }

    @Generated(hash = 274049648)
    private transient Long session__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1649474943)
    public WorkSession getSession() {
        Long __key = this.sessionId;
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
    @Generated(hash = 588104499)
    public void setSession(WorkSession session) {
        synchronized (this) {
            this.session = session;
            sessionId = session == null ? null : session.getId();
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


    /**
     * Get a map object type layer
     */
    public Layer getLayer() {
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
                "remoteId=" + remoteId +
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


    public boolean isTerminal() {
        return this.getObjectType().getIsTerminal();
    }


    public boolean belongToTopoLayer() {
        return this.getNodeGrade() > 0;
    }


    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1851045019)
    public List<MapObject> getJoinedList() {
        if (joinedList == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjectDao targetDao = daoSession.getMapObjectDao();
            List<MapObject> joinedListNew = targetDao._queryMapObject_JoinedList(id);
            synchronized (this) {
                if (joinedList == null) {
                    joinedList = joinedListNew;
                }
            }
        }
        return joinedList;
    }


    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 93047367)
    public synchronized void resetJoinedList() {
        joinedList = null;
    }


    public MapObjecType.GeomType getGeomType() {

        return this.getObjectType().getGeomType();

    }

    public GPGeoPoint getCentroid() {
        GeomBuilder builder = new GeomBuilder();
        for (GeoPoint point : this.getCoord()) {
            builder.point(point.getLongitude(),
                    point.getLatitude());
        }
        Point centroid = null;
        MapObjecType.GeomType geomType = getGeomType();
        if (geomType == MapObjecType.GeomType.POLYGON) {
            centroid = builder.toPolygon().getCentroid();
        } else if (geomType == MapObjecType.GeomType.POLYLINE) {
            centroid = builder.toLineString().getCentroid();
        } else if (geomType == MapObjecType.GeomType.POINT) {
            centroid = builder.toPoint().getCentroid();
        }
        return centroid == null ? null : new GPGeoPoint(centroid.getY() + 0.000001, centroid.getX() + 0.000001);

    }

    /**
     * @return min lat.
     */
    @Override
    public double getMinLat() {

        return coord.get(0).getLatitude();
    }

    /**
     * @return min lon.
     */
    @Override
    public double getMinLon() {
        return coord.get(0).getLongitude();
    }

    /**
     * @return max lat.
     */
    @Override
    public double getMaxLat() {
        return coord.get(coord.size() - 1).getLatitude();
    }

    /**
     * @return max lon.
     */
    @Override
    public double getMaxLon() {
        return coord.get(coord.size() - 1).getLongitude();
    }

    /**
     * Transforms the object in its gpx representation.
     *
     * @return the gpx representation.
     * @throws Exception if something goes wrong.
     */
    @Override
    public String toGpxString() throws Exception {
        String description = Utilities.makeXmlSafe(this.observation);
        description = description.replaceAll("\n", "; "); //$NON-NLS-1$//$NON-NLS-2$
        String name = Utilities.makeXmlSafe(this.code);
        name = name.replaceAll("\n", "; "); //$NON-NLS-1$//$NON-NLS-2$

        long time = getCreatedAt().getTime();
        String dateString = TimeUtilities.INSTANCE.TIME_FORMATTER_GPX_UTC.format(new Date(time));

        GPGeoPoint point = this.getCentroid();

        String wayPointString = GpxUtilities.getWayPointString(point.getLatitude(), point.getLongitude(), this.elevation, name, description, dateString);
        return wayPointString;
    }

    /**
     * Transforms the object in its kml representation.
     *
     * @return the kml representation.
     * @throws Exception if something goes wrong.
     */
    @Override
    public String toKmlString() throws Exception {
        imageIds = new ArrayList<>();
        String name = Utilities.makeXmlSafe(this.code);
        StringBuilder sB = new StringBuilder();
        sB.append("<Placemark>\n");
        sB.append("<styleUrl>#info-icon</styleUrl>\n");
        sB.append("<name>").append(name).append("</name>\n");
        sB.append("<description>\n");

        List<MapObjectImages> imagesList = getImages();

        if (imagesList != null && imagesList.size() > 0) {

            sB.append("<![CDATA[\n");
            sB.append("<h1> Images </h1>\n");

            for (MapObjectImages image : imagesList) {
                sB.append("<h2> Image #").append(image.getId()).append("</h2>\n");

                sB.append("<table style=\"text-align: left; width: 100%;\" border=\"1\" cellpadding=\"5\" cellspacing=\"2\">");
                sB.append("<tbody>");

                String imgName = image.getName();

                sB.append("<tr>");
                sB.append("<td colspan=\"2\" style=\"text-align: left; vertical-align: top; width: 100%;\">");
                sB.append("<img src=\"").append(imgName).append("\" width=\"300\">");
                sB.append("</td>");
                sB.append("</tr>");

                imageIds.add(String.valueOf(image.getId()));

                sB.append("</tbody>");
                sB.append("</table>");
            }


            sB.append("]]>\n");
        } else {
            String description = Utilities.makeXmlSafe(this.observation);
            sB.append(description);
            sB.append("\n");
            sB.append(this.getCreatedAt());
        }

        sB.append("</description>\n");
        sB.append("<gx:balloonVisibility>1</gx:balloonVisibility>\n");
        sB.append("<Point>\n");
        GPGeoPoint point = this.getCentroid();
        sB.append("<coordinates>").append(point.getLongitude()).append(",").append(point.getLatitude()).append(",0</coordinates>\n");
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
        return !getImages().isEmpty();
    }

    @Transient
    @Expose(serialize = false, deserialize = false)
    private List<String> imageIds = null;

    /**
     * Get image ids list.
     *
     * @return the list of image ids.
     */
    @Override
    public List<String> getImageIds() {

        if (imageIds == null) {
            try {
                List<MapObjectImages> imagesList = getImages();
                imageIds = new ArrayList<>();
                for (MapObjectImages img :
                        imagesList) {
                    imageIds.add(String.valueOf(img.getId()));
                }
            } catch (Exception e) {
                GPLog.error(this, null, e);
            }
        }
        if (imageIds == null) {
            return Collections.emptyList();
        }
        return imageIds;
    }

    // Region de los datos de ayuda en la syncronización
    @Override
    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    @Override
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public Boolean mustExport() {
        return !isSync || (updatedAt != null && SyncDate.before(updatedAt));
    }

    @Override
    public IExportable toRemoteObject() {

        MapObject join = getJoinObj();

        MapObject obj;
        if (join == null) {
            obj = new MapObject(id, remoteId, code, null, mapObjectTypeId, coord, elevation, observation,
                    isSync, SyncDate, nodeGrade, stockCodeId, sessionId, isCompleted);
        } else {
            obj = new MapObject(id, remoteId, code, join.getRemoteId(), mapObjectTypeId, coord, elevation, observation,
                    isSync, SyncDate, nodeGrade, stockCodeId, sessionId, isCompleted);
        }

        obj.setDeleted(deleted);

        return obj;
    }

    public void setSessionId(Long sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public Long getRemoteId() {
        return remoteId;
    }

    @Override
    public void setRemoteId(Long remoteId) {
        this.remoteId = remoteId;
    }

    public void setRemoteId(long remoteId) {
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

    public byte[] getIcon() {
        return getObjectType().getIconAsByteArray();
    }

    public MapObject getActiveObject() {
        if (this.activeObject == null) {
            return this;
        }

        return this.activeObject;
    }

    public void setActiveObject(MapObject activeObject) {
        this.activeObject = activeObject;
    }


    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 788755102)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMapObjectDao() : null;
    }
}
