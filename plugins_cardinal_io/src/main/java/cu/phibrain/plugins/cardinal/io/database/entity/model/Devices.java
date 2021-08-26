package cu.phibrain.plugins.cardinal.io.database.entity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Ero on 24/09/2021.
 * Entity mapped to table "devices".
 */
@Entity(
        nameInDb = "MANAGER_DEVICES",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true,

        indexes = {
                @Index(value = "deviceId", unique = true)
        }
)
public class Devices implements Serializable, IEntity, IExportable {

    @Id(autoincrement = true)
    private Long id;

    @SerializedName("id")
    @Expose
    private String deviceId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("os_version")
    @Expose
    private String osVersion;

    @SerializedName("created_at")
    @Expose(serialize = false)
    private Date createdAt;

    @SerializedName("worker")
    @Expose
    private long workerId;

    @ToOne(joinProperty = "workerId")
    @Expose(serialize = false, deserialize = false)
    private Worker worker;

    @Expose(serialize = false, deserialize = false)
    private Date updatedAt;

    @Expose(serialize = false, deserialize = false)
    private Boolean isSync;

    @Expose(serialize = false, deserialize = false)
    private Date SyncDate;


    private final static long serialVersionUID = 88686924243L;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1904195395)
    private transient DevicesDao myDao;
    private Boolean deleted;

    @Generated(hash = 1242947508)
    public Devices(Long id, String deviceId, String name, String osVersion, Date createdAt,
            long workerId, Date updatedAt, Boolean isSync, Date SyncDate, Boolean deleted) {
        this.id = id;
        this.deviceId = deviceId;
        this.name = name;
        this.osVersion = osVersion;
        this.createdAt = createdAt;
        this.workerId = workerId;
        this.updatedAt = updatedAt;
        this.isSync = isSync;
        this.SyncDate = SyncDate;
        this.deleted = deleted;
    }

    public Devices(Long id, String deviceId, String name, String osVersion, Date createdAt,
                   long workerId) {
        this.id = id;
        this.deviceId = deviceId;
        this.name = name;
        this.osVersion = osVersion;
        this.createdAt = createdAt;
        this.workerId = workerId;
        this.updatedAt = null;
        this.isSync = false;
        this.SyncDate = null;
    }

    @Generated(hash = 597445211)
    public Devices() {
    }

    @Generated(hash = 1041702162)
    private transient Long worker__resolvedKey;

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOsVersion() {
        return this.osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public Date getUpdatedAt() {
        return this.updatedAt;
    }

    @Override
    public void setUpdatedAt(Date updated) {
        this.updatedAt = updated;
    }

    @Override
    public Boolean getIsSync() {
        return this.isSync;
    }

    @Override
    public void setIsSync(Boolean isSync) {
        this.isSync = isSync;
    }

    @Override
    public void setSyncDate(Date SyncDate) {
        this.SyncDate = SyncDate;
    }

    @Override
    public Date getSyncDate() {
        return this.SyncDate;
    }

    @Override
    public Boolean mustExport() {
        return !isSync || (updatedAt != null && SyncDate.before(updatedAt));
    }

    @Override
    public IExportable toRemoteObject() {
        return this;
    }

    @Override
    public Long getRemoteId() {
        return null;
    }

    @Override
    public void setRemoteId(Long remoteId) {

    }

    @Override
    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public Boolean getDeleted() {
        return this.deleted;
    }

    public long getWorkerId() {
        return this.workerId;
    }

    public void setWorkerId(long workerId) {
        this.workerId = workerId;
    }

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 264230996)
    public Worker getWorker() {
        long __key = this.workerId;
        if (worker__resolvedKey == null || !worker__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WorkerDao targetDao = daoSession.getWorkerDao();
            Worker workerNew = targetDao.load(__key);
            synchronized (this) {
                worker = workerNew;
                worker__resolvedKey = __key;
            }
        }
        return worker;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 820240920)
    public void setWorker(@NotNull Worker worker) {
        if (worker == null) {
            throw new DaoException(
                    "To-one property 'workerId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.worker = worker;
            workerId = worker.getId();
            worker__resolvedKey = workerId;
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

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 253756986)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getDevicesDao() : null;
    }
}
