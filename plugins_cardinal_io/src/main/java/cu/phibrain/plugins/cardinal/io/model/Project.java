
package cu.phibrain.plugins.cardinal.io.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.JoinEntity;
import org.greenrobot.greendao.annotation.OrderBy;
import org.greenrobot.greendao.annotation.ToMany;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * Created by Ero on 17/09/2020.
 * Entity mapped to table "project".
 */
@Entity(
        nameInDb = "MANAGER_PROJECT",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class Project implements Serializable {

    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("created_at")
    @Expose
    private Date createdAt;

    @ToMany
    @JoinEntity(
            entity = ProjectsWorkers.class,
            sourceProperty = "projectId",
            targetProperty = "workerId"
    )
    @SerializedName("workers")
    @Expose
    private List<Worker> workers;

    @ToMany(referencedJoinProperty = "projectId")
    @OrderBy("code ASC")
    @SerializedName("stocks")
    @Expose
    private List<Stock> stocks;


    @ToMany(referencedJoinProperty = "projectId")
    @SerializedName("zone")
    @Expose
    private List<Zone> zone;

    @ToMany(referencedJoinProperty = "projectId")
    @SerializedName("groupoflayers")
    @Expose
    private List<GroupOfLayer> groupoflayers;

    private final static long serialVersionUID = -7160043438657824243L;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1378029107)
    private transient ProjectDao myDao;

    /**
     * No args constructor for use in serialization
     */
    public Project() {
    }

    @Generated(hash = 904858793)
    public Project(Long id, String name, String description, Date createdAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.createdAt = createdAt;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 722190173)
    public List<Worker> getWorkers() {
        if (workers == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WorkerDao targetDao = daoSession.getWorkerDao();
            List<Worker> workersNew = targetDao._queryProject_Workers(id);
            synchronized (this) {
                if (workers == null) {
                    workers = workersNew;
                }
            }
        }
        return workers;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 646726799)
    public synchronized void resetWorkers() {
        workers = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 174669090)
    public List<Stock> getStocks() {
        if (stocks == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            StockDao targetDao = daoSession.getStockDao();
            List<Stock> stocksNew = targetDao._queryProject_Stocks(id);
            synchronized (this) {
                if (stocks == null) {
                    stocks = stocksNew;
                }
            }
        }
        return stocks;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1016192454)
    public synchronized void resetStocks() {
        stocks = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 98135774)
    public List<Zone> getZone() {
        if (zone == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ZoneDao targetDao = daoSession.getZoneDao();
            List<Zone> zoneNew = targetDao._queryProject_Zone(id);
            synchronized (this) {
                if (zone == null) {
                    zone = zoneNew;
                }
            }
        }
        return zone;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 363405998)
    public synchronized void resetZone() {
        zone = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1852042894)
    public List<GroupOfLayer> getGroupoflayers() {
        if (groupoflayers == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            GroupOfLayerDao targetDao = daoSession.getGroupOfLayerDao();
            List<GroupOfLayer> groupoflayersNew = targetDao._queryProject_Groupoflayers(id);
            synchronized (this) {
                if (groupoflayers == null) {
                    groupoflayers = groupoflayersNew;
                }
            }
        }
        return groupoflayers;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 962824708)
    public synchronized void resetGroupoflayers() {
        groupoflayers = null;
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
    @Generated(hash = 2081800561)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getProjectDao() : null;
    }


}
