
package cu.phibrain.plugins.cardinal.io.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity(
        nameInDb = "MANAGER_WORK_SESSION",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class WorkSession implements Serializable {

    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("start_date")
    @Expose
    private Date startDate;

    @SerializedName("end_date")
    @Expose
    private Date endDate;

    @SerializedName("zone")
    @Expose
    private long zoneId;

    @ToOne(joinProperty = "zoneId")
    private Zone zoneObj;

    @SerializedName("active")
    @Expose
    private Boolean active;

    @ToMany(referencedJoinProperty = "workerSessionId")
    @SerializedName("worker_route")
    @Expose
    private List<WorkerRoute> workerRoute;

    @ToOne(joinProperty = "contractId")
    private Contract contractObj;

    @SerializedName("contract")
    @Expose
    private long contractId;

    @ToMany(referencedJoinProperty = "sessionId")
    @SerializedName("map_objects")
    @Expose
    private List<MapObject> mapObjects;

    @ToMany(referencedJoinProperty = "workerSessionId")
    @SerializedName("materials")
    @Expose
    private List<Material> materials;

    @ToMany(referencedJoinProperty = "workerSessionId")
    @SerializedName("labels")
    @Expose
    private List<LabelSubLot> labels;


    @ToMany(referencedJoinProperty = "sessionId")
    @SerializedName("events")
    @Expose
    private List<SignalEvents> events;

    private final static long serialVersionUID = 2460962077263436904L;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1675214157)
    private transient WorkSessionDao myDao;

    @Generated(hash = 887378589)
    public WorkSession(Long id, Date startDate, Date endDate, long zoneId, Boolean active,
                       long contractId) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.zoneId = zoneId;
        this.active = active;
        this.contractId = contractId;
    }

    @Generated(hash = 955645809)
    public WorkSession() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return this.startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return this.endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public long getZoneId() {
        return this.zoneId;
    }

    public void setZoneId(long zoneId) {
        this.zoneId = zoneId;
    }

    public Boolean getActive() {
        return this.active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public long getContractId() {
        return this.contractId;
    }

    public void setContractId(long contractId) {
        this.contractId = contractId;
    }

    @Generated(hash = 140960598)
    private transient Long contractObj__resolvedKey;

    @Generated(hash = 915656327)
    private transient Long zoneObj__resolvedKey;

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 646316108)
    public List<WorkerRoute> getWorkerRoute() {
        if (workerRoute == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WorkerRouteDao targetDao = daoSession.getWorkerRouteDao();
            List<WorkerRoute> workerRouteNew = targetDao._queryWorkSession_WorkerRoute(id);
            synchronized (this) {
                if (workerRoute == null) {
                    workerRoute = workerRouteNew;
                }
            }
        }
        return workerRoute;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 659670617)
    public synchronized void resetWorkerRoute() {
        workerRoute = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 522711554)
    public List<MapObject> getMapObjects() {
        if (mapObjects == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MapObjectDao targetDao = daoSession.getMapObjectDao();
            List<MapObject> mapObjectsNew = targetDao._queryWorkSession_MapObjects(id);
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

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1915151396)
    public List<Material> getMaterials() {
        if (materials == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            MaterialDao targetDao = daoSession.getMaterialDao();
            List<Material> materialsNew = targetDao._queryWorkSession_Materials(id);
            synchronized (this) {
                if (materials == null) {
                    materials = materialsNew;
                }
            }
        }
        return materials;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1148495594)
    public synchronized void resetMaterials() {
        materials = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 215190859)
    public List<LabelSubLot> getLabels() {
        if (labels == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LabelSubLotDao targetDao = daoSession.getLabelSubLotDao();
            List<LabelSubLot> labelsNew = targetDao._queryWorkSession_Labels(id);
            synchronized (this) {
                if (labels == null) {
                    labels = labelsNew;
                }
            }
        }
        return labels;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 902294403)
    public synchronized void resetLabels() {
        labels = null;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 2077745879)
    public List<SignalEvents> getEvents() {
        if (events == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SignalEventsDao targetDao = daoSession.getSignalEventsDao();
            List<SignalEvents> eventsNew = targetDao._queryWorkSession_Events(id);
            synchronized (this) {
                if (events == null) {
                    events = eventsNew;
                }
            }
        }
        return events;
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1830105409)
    public synchronized void resetEvents() {
        events = null;
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

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1260959430)
    public Contract getContractObj() {
        long __key = this.contractId;
        if (contractObj__resolvedKey == null || !contractObj__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ContractDao targetDao = daoSession.getContractDao();
            Contract contractObjNew = targetDao.load(__key);
            synchronized (this) {
                contractObj = contractObjNew;
                contractObj__resolvedKey = __key;
            }
        }
        return contractObj;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 171903385)
    public void setContractObj(@NotNull Contract contractObj) {
        if (contractObj == null) {
            throw new DaoException(
                    "To-one property 'contractId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.contractObj = contractObj;
            contractId = contractObj.getId();
            contractObj__resolvedKey = contractId;
        }
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 843009192)
    public Zone getZoneObj() {
        long __key = this.zoneId;
        if (zoneObj__resolvedKey == null || !zoneObj__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ZoneDao targetDao = daoSession.getZoneDao();
            Zone zoneObjNew = targetDao.load(__key);
            synchronized (this) {
                zoneObj = zoneObjNew;
                zoneObj__resolvedKey = __key;
            }
        }
        return zoneObj;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 806374319)
    public void setZoneObj(@NotNull Zone zoneObj) {
        if (zoneObj == null) {
            throw new DaoException(
                    "To-one property 'zoneId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.zoneObj = zoneObj;
            zoneId = zoneObj.getId();
            zoneObj__resolvedKey = zoneId;
        }
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1446127786)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getWorkSessionDao() : null;
    }


}
