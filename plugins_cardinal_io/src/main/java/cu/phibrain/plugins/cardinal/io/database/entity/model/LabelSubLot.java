package cu.phibrain.plugins.cardinal.io.database.entity.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;

/**
 * Created by Ero on 04/05/2021.
 * Entity mapped to table "zone".
 */
@Entity(
        nameInDb = "MANAGER_LABEL_SUB_LOT",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class LabelSubLot implements Serializable, IEntity{
    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("label")
    @Expose
    private long labelId;

    @ToOne(joinProperty = "labelId")
    private Label labelObj;

    @SerializedName("work_session")
    @Expose
    private long workerSessionId;

    @SerializedName("used")
    @Expose
    private Boolean geolocated;

    private final static long serialVersionUID = -9368657824243L;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1540473045)
    private transient LabelSubLotDao myDao;

    @Generated(hash = 1709235993)
    public LabelSubLot(Long id, long labelId, long workerSessionId, Boolean geolocated) {
        this.id = id;
        this.labelId = labelId;
        this.workerSessionId = workerSessionId;
        this.geolocated = geolocated;
    }

    @Generated(hash = 1568451650)
    public LabelSubLot() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getLabelId() {
        return this.labelId;
    }

    public void setLabelId(long labelId) {
        this.labelId = labelId;
    }

    public long getWorkerSessionId() {
        return this.workerSessionId;
    }

    public void setWorkerSessionId(long workerSessionId) {
        this.workerSessionId = workerSessionId;
    }

    public Boolean getGeolocated() {
        return this.geolocated;
    }

    public void setGeolocated(Boolean geolocated) {
        this.geolocated = geolocated;
    }

    @Generated(hash = 1468898257)
    private transient Long labelObj__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 985110689)
    public Label getLabelObj() {
        long __key = this.labelId;
        if (labelObj__resolvedKey == null || !labelObj__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LabelDao targetDao = daoSession.getLabelDao();
            Label labelObjNew = targetDao.load(__key);
            synchronized (this) {
                labelObj = labelObjNew;
                labelObj__resolvedKey = __key;
            }
        }
        return labelObj;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 166507149)
    public void setLabelObj(@NotNull Label labelObj) {
        if (labelObj == null) {
            throw new DaoException(
                    "To-one property 'labelId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.labelObj = labelObj;
            labelId = labelObj.getId();
            labelObj__resolvedKey = labelId;
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

    @Override
    public String toString() {
        return labelObj.getCode();
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 596595756)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLabelSubLotDao() : null;
    }
}
