
package cu.phibrain.plugins.cardinal.io.database.entity.model;

import android.util.Base64;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Ero on 15/02/2021.
 * Entity mapped to table "Layer".
 */
@Entity(
        nameInDb = "CARDINAL_LAYER",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class Layer implements Serializable, IEntity {
    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("group_layer")
    @Expose
    private long groupId;

    @ToMany(referencedJoinProperty = "layerId")
    @SerializedName("mapobjectypes")
    @Expose
    private List<MapObjecType> mapobjectypes;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("view_zoom_level")
    @Expose
    private Integer viewZoomLevel;

    @SerializedName("edit_zoom_level")
    @Expose
    private Integer editZoomLevel;

    @SerializedName("active")
    @Expose

    private Boolean enabled;

    private final static long serialVersionUID = -8834285743731834345L;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 2004179447)
    private transient LayerDao myDao;

    /**
     * No args constructor for use in serialization
     */
    public Layer() {
    }



    @Generated(hash = 217052673)
    public Layer(Long id, String name, long groupId, String icon, Integer viewZoomLevel,
            Integer editZoomLevel, Boolean enabled) {
        this.id = id;
        this.name = name;
        this.groupId = groupId;
        this.icon = icon;
        this.viewZoomLevel = viewZoomLevel;
        this.editZoomLevel = editZoomLevel;
        this.enabled = enabled;
    }



    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getGroupId() {
        return this.groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public byte[] getIconAsByteArray() {
        if(this.icon!=null && !this.icon.isEmpty())
            return Base64.decode(this.icon.replaceFirst("^data:image/[^;]*;base64,?",""), 0);
        return null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1526497424)
    public List<MapObjecType> getMapobjectypes() {
        if (mapobjectypes == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjecTypeDao targetDao = daoSession.getMapObjecTypeDao();
            List<MapObjecType> mapobjectypesNew = targetDao._queryLayer_Mapobjectypes(id);
            synchronized (this) {
                if (mapobjectypes == null) {
                    mapobjectypes = mapobjectypesNew;
                }
            }
        }
        return mapobjectypes;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1592221182)
    public synchronized void resetMapobjectypes() {
        mapobjectypes = null;
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

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Boolean getIsActive() {
        return this.enabled;
    }

    public Integer getViewZoomLevel() {
        return this.viewZoomLevel==0 ? 8  : this.viewZoomLevel;
    }

    public void setViewZoomLevel(Integer viewZoomLevel) {
        this.viewZoomLevel = viewZoomLevel;
    }

    public Integer getEditZoomLevel() {

        return this.editZoomLevel==0 ? 17 : this.editZoomLevel;
    }

    public void setEditZoomLevel(Integer editZoomLevel) {
        this.editZoomLevel = editZoomLevel;
    }


    public Boolean getEnabled() {
        return this.enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
    public void setIsActive(Boolean isActive) {
        this.enabled = isActive;

    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 213609621)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLayerDao() : null;
    }

}
