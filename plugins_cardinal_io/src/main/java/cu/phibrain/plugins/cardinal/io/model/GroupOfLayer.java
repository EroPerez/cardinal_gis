
package cu.phibrain.plugins.cardinal.io.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;


/**
 * Created by Ero on 17/09/2020.
 * Entity mapped to table "Groupoflayer".
 */
@Entity(
        nameInDb = "CARDINAL_LAYER_GROUP",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class GroupOfLayer implements Serializable {
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
    @Generated(hash = 1695020211)
    private transient GroupOfLayerDao myDao;

    /**
     * No args constructor for use in serialization
     */
    public GroupOfLayer() {
    }

    @Generated(hash = 1570768320)
    public GroupOfLayer(Long id, String name, long projectId) {
        this.id = id;
        this.name = name;
        this.projectId = projectId;
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
    @Generated(hash = 1226305366)
    public List<Layer> getLayers() {
        if (layers == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LayerDao targetDao = daoSession.getLayerDao();
            List<Layer> layersNew = targetDao._queryGroupOfLayer_Layers(id);
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 514741472)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getGroupOfLayerDao() : null;
    }


}
