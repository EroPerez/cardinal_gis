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
import java.util.List;

@Entity(
        nameInDb = "MANAGER_CONTRACT",

        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class Contract implements Serializable {

    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    private long workerId;

    @ToOne(joinProperty = "workerId")
    @SerializedName("worker")
    @Expose
    private Worker theWorker;

    @SerializedName("project")
    @Expose
    private long projectId;

    @SerializedName("active")
    @Expose
    private boolean active;

    @ToMany(referencedJoinProperty = "contractId")
    @SerializedName("work_sessions")
    @Expose
    private List<WorkSession> workSessions;

    private final static long serialVersionUID = 6686928259217578488L;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1270984375)
    private transient ContractDao myDao;

    @Generated(hash = 195394159)
    private transient Long theWorker__resolvedKey;


    public Contract() {
    }


    @Generated(hash = 2001389220)
    public Contract(Long id, long workerId, long projectId, boolean active) {
        this.id = id;
        this.workerId = workerId;
        this.projectId = projectId;
        this.active = active;
    }


    public Long getId() {
        return this.id;
    }


    public void setId(Long id) {
        this.id = id;
    }


    public long getWorkerId() {
        return this.workerId;
    }


    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }


    public long getProjectId() {
        return this.projectId;
    }


    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }


    public boolean getActive() {
        return this.active;
    }


    public void setActive(boolean active) {
        this.active = active;
    }


    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 180290860)
    public Worker getTheWorker() {
        long __key = this.workerId;
        if (theWorker__resolvedKey == null || !theWorker__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WorkerDao targetDao = daoSession.getWorkerDao();
            Worker theWorkerNew = targetDao.load(__key);
            synchronized (this) {
                theWorker = theWorkerNew;
                theWorker__resolvedKey = __key;
            }
        }
        return theWorker;
    }


    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1413306734)
    public void setTheWorker(@NotNull Worker theWorker) {
        if (theWorker == null) {
            throw new DaoException(
                    "To-one property 'workerId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.theWorker = theWorker;
            workerId = theWorker.getId();
            theWorker__resolvedKey = workerId;
        }
    }


    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1735009199)
    public List<WorkSession> getWorkSessions() {
        if (workSessions == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WorkSessionDao targetDao = daoSession.getWorkSessionDao();
            List<WorkSession> workSessionsNew = targetDao._queryContract_WorkSessions(id);
            synchronized (this) {
                if (workSessions == null) {
                    workSessions = workSessionsNew;
                }
            }
        }
        return workSessions;
    }


    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1993332557)
    public synchronized void resetWorkSessions() {
        workSessions = null;
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
    @Generated(hash = 2108039080)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getContractDao() : null;
    }

}
