
package cu.phibrain.plugins.cardinal.io.model;

import android.util.Base64;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.model.converter.GeomTypeConverter;

/**
 * Created by Ero on 15/02/2021.
 * Entity mapped to table "Mapobjectype".
 */
@Entity(
        nameInDb = "CARDINAL_MAP_OBJECT_TYPE",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)

public class MapObjecType implements Serializable {

    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("parent")
    @Expose
    private long parentId;

    @ToOne(joinProperty = "parentId")
    private MapObjecType parentObj;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("caption")
    @Expose
    private String caption;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("layer")
    @Expose
    private Long layerId;

    @ToMany(referencedJoinProperty = "originId")
    @SerializedName("rules")
    @Expose
    private List<TopologicalRule> topoRule;

    @ToMany(referencedJoinProperty = "mapObjecTypeId")
    @SerializedName("attrs")
    @Expose
    private List<MapObjectTypeAttribute> attributes;

    @ToMany(referencedJoinProperty = "mapObjecTypeId")
    @SerializedName("states")
    @Expose
    private List<MapObjecTypeState> states;

    @ToMany(referencedJoinProperty = "mapObjecTypeId")
    @SerializedName("defects")
    @Expose
    private List<MapObjecTypeDefect> defects;

    public enum GeomType {
        @SerializedName("0")
        POINT(0),
        @SerializedName("2")
        POLYLINE(2),
        @SerializedName("3")
        POLYGON(3);

        private final int id;

        public int getId() {
            return id;
        }

        @Nullable
        public static GeomType fromId(int id) {
            for (GeomType type : GeomType.values()) {
                if (type.getId() == id) {
                    return type;
                }
            }
            return null;
        }

        GeomType(int id) {
            this.id = id;
        }
    }

    @Convert(converter = GeomTypeConverter.class, columnType = Integer.class)
    @SerializedName("geometry_type")
    @Expose
    private GeomType geomType;

    @SerializedName("is_abstract")
    @Expose
    private boolean isAbstract;

    @ToMany(referencedJoinProperty = "mapObjectTypeId")
    private List<MapObject> mapObjects;


    private final static long serialVersionUID = -5405345251211056739L;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1887801799)
    private transient MapObjecTypeDao myDao;

    @Generated(hash = 22875058)
    private transient Long parentObj__resolvedKey;

    /**
     * No args constructor for use in serialization
     */
    public MapObjecType() {
    }

    @Generated(hash = 150850807)
    public MapObjecType(Long id, long parentId, String icon, String caption, String description,
            Long layerId, GeomType geomType, boolean isAbstract) {
        this.id = id;
        this.parentId = parentId;
        this.icon = icon;
        this.caption = caption;
        this.description = description;
        this.layerId = layerId;
        this.geomType = geomType;
        this.isAbstract = isAbstract;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentId() {
        return this.parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public byte[] getIconAsByteArray() {
        if(!this.icon.isEmpty())
            return Base64.decode(this.icon.replaceFirst("^data:image/[^;]*;base64,?",""), 0);
        return null;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCaption() {
        return this.caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLayerId() {
        return this.layerId;
    }

    public void setLayerId(Long layerId) {
        this.layerId = layerId;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 107888718)
    public List<MapObjectTypeAttribute> getAttributes() {
        if (attributes == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjectTypeAttributeDao targetDao = daoSession.getMapObjectTypeAttributeDao();
            List<MapObjectTypeAttribute> attributesNew = targetDao
                    ._queryMapObjecType_Attributes(id);
            synchronized (this) {
                if (attributes == null) {
                    attributes = attributesNew;
                }
            }
        }
        return attributes;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1697487056)
    public synchronized void resetAttributes() {
        attributes = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 897256307)
    public List<MapObjecTypeState> getStates() {
        if (states == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjecTypeStateDao targetDao = daoSession.getMapObjecTypeStateDao();
            List<MapObjecTypeState> statesNew = targetDao._queryMapObjecType_States(id);
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
    @Generated(hash = 1336070364)
    public List<MapObjecTypeDefect> getDefects() {
        if (defects == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjecTypeDefectDao targetDao = daoSession.getMapObjecTypeDefectDao();
            List<MapObjecTypeDefect> defectsNew = targetDao._queryMapObjecType_Defects(id);
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

    public void setParentId(long parentId) {
        this.parentId = parentId;
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

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 591080823)
    public MapObjecType getParentObj() {
        long __key = this.parentId;
        if (parentObj__resolvedKey == null || !parentObj__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjecTypeDao targetDao = daoSession.getMapObjecTypeDao();
            MapObjecType parentObjNew = targetDao.load(__key);
            synchronized (this) {
                parentObj = parentObjNew;
                parentObj__resolvedKey = __key;
            }
        }
        return parentObj;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1923743242)
    public void setParentObj(@NotNull MapObjecType parentObj) {
        if (parentObj == null) {
            throw new DaoException(
                    "To-one property 'parentId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.parentObj = parentObj;
            parentId = parentObj.getId();
            parentObj__resolvedKey = parentId;
        }
    }

    public GeomType getGeomType() {
        return this.geomType;
    }

    public void setGeomType(GeomType geomType) {
        this.geomType = geomType;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 664408944)
    public List<TopologicalRule> getTopoRule() {
        if (topoRule == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            TopologicalRuleDao targetDao = daoSession.getTopologicalRuleDao();
            List<TopologicalRule> topoRuleNew = targetDao._queryMapObjecType_TopoRule(id);
            synchronized (this) {
                if (topoRule == null) {
                    topoRule = topoRuleNew;
                }
            }
        }
        return topoRule;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 819251379)
    public synchronized void resetTopoRule() {
        topoRule = null;
    }

    public boolean getIsAbstract() {
        return this.isAbstract;
    }

    public void setIsAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 947238317)
    public List<MapObject> getMapObjects() {
        if (mapObjects == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjectDao targetDao = daoSession.getMapObjectDao();
            List<MapObject> mapObjectsNew = targetDao._queryMapObjecType_MapObjects(id);
            synchronized (this) {
                if (mapObjects == null) {
                    mapObjects = mapObjectsNew;
                }
            }
        }
        return mapObjects;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 917535720)
    public synchronized void resetMapObjects() {
        mapObjects = null;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1500504985)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMapObjecTypeDao() : null;
    }



}
