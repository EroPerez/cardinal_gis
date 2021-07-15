
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
 * Created by Ero on 17/09/2020.
 * Entity mapped to table "Groupoflayer".
 */
@Entity(
        nameInDb = "CARDINAL_NETWORKS",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class Networks implements Serializable, IEntity{
    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("project")
    @Expose
    private long projectId;

    @SerializedName("icon")
    @Expose
    private String icon;

    @ToMany(referencedJoinProperty = "groupId")
    @SerializedName("layers")
    @Expose
    private List<Layer> layers;

    private final static long serialVersionUID = -3981581245574351327L;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 742475979)
    private transient NetworksDao myDao;

    /**
     * No args constructor for use in serialization
     */
    public Networks() {
    }

    @Generated(hash = 206461112)
    public Networks(Long id, String name, long projectId, String icon) {
        this.id = id;
        this.name = name;
        this.projectId = projectId;
        this.icon = icon;
    }

    public byte[] getIconAsByteArray() {
        if(this.icon!=null && !this.icon.isEmpty())
            return Base64.decode(this.icon.replaceFirst("^data:image/[^;]*;base64,?",""), 0);
        return null;
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

    public long getProjectId() {
        return this.projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 121899908)
    public List<Layer> getLayers() {
        if (layers == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LayerDao targetDao = daoSession.getLayerDao();
            List<Layer> layersNew = targetDao._queryNetworks_Layers(id);
            synchronized (this) {
                if (layers == null) {
                    layers = layersNew;
                }
            }
        }
        return layers;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1857330458)
    public synchronized void resetLayers() {
        layers = null;
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

    @Override
    public String toString() {
        return this.name;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1697597019)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getNetworksDao() : null;
    }


}
