
package cu.phibrain.plugins.cardinal.io.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;
import java.util.List;

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

    @ToOne(joinProperty = "id")
    @SerializedName("parent")
    @Expose
    private MapObjecType parent;

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

//    @SerializedName("rules")
//    @Expose
//    private List<Integer> rules = new ArrayList<Integer>();

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

    @Generated(hash = 1293412156)
    private transient Long parent__resolvedKey;


    /**
     * No args constructor for use in serialization
     */
    public MapObjecType() {
    }


    @Generated(hash = 902182871)
    public MapObjecType(Long id, String icon, String caption, String description,
                        Long layerId) {
        this.id = id;
        this.icon = icon;
        this.caption = caption;
        this.description = description;
        this.layerId = layerId;
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
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
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1655725988)
    public MapObjecType getParent() {
        Long __key = this.id;
        if (parent__resolvedKey == null || !parent__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjecTypeDao targetDao = daoSession.getMapObjecTypeDao();
            MapObjecType parentNew = targetDao.load(__key);
            synchronized (this) {
                parent = parentNew;
                parent__resolvedKey = __key;
            }
        }
        return parent;
    }


    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1754508159)
    public void setParent(MapObjecType parent) {
        synchronized (this) {
            this.parent = parent;
            id = parent == null ? null : parent.getId();
            parent__resolvedKey = id;
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


    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1500504985)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMapObjecTypeDao() : null;
    }


}
