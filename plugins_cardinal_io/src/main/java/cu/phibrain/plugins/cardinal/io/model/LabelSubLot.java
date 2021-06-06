package cu.phibrain.plugins.cardinal.io.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

/**
 * Created by Ero on 04/05/2021.
 * Entity mapped to table "zone".
 */
@Entity(
        nameInDb = "MANAGER_LABEL_BATCHES",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class LabelSubLot {
    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("initial_label")
    @Expose
    private String firstLabel;

    @SerializedName("end_label")
    @Expose
    private String lastLabel;

    @SerializedName("batch_label")
    @Expose
    private long labelLotId;

    @ToOne(joinProperty = "labelLotId")
    private LabelBatches labelLot;

    @SerializedName("work_session")
    @Expose
    private long workerSessionId;

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

    @Generated(hash = 526289808)
    public LabelSubLot(Long id, String firstLabel, String lastLabel, long labelLotId,
                       long workerSessionId) {
        this.id = id;
        this.firstLabel = firstLabel;
        this.lastLabel = lastLabel;
        this.labelLotId = labelLotId;
        this.workerSessionId = workerSessionId;
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

    public String getFirstLabel() {
        return this.firstLabel;
    }

    public void setFirstLabel(String firstLabel) {
        this.firstLabel = firstLabel;
    }

    public String getLastLabel() {
        return this.lastLabel;
    }

    public void setLastLabel(String lastLabel) {
        this.lastLabel = lastLabel;
    }

    public long getLabelLotId() {
        return this.labelLotId;
    }

    public void setLabelLotId(long labelLotId) {
        this.labelLotId = labelLotId;
    }

    public long getWorkerSessionId() {
        return this.workerSessionId;
    }

    public void setWorkerSessionId(long workerSessionId) {
        this.workerSessionId = workerSessionId;
    }

    @Generated(hash = 453659498)
    private transient Long labelLot__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 19063868)
    public LabelBatches getLabelLot() {
        long __key = this.labelLotId;
        if (labelLot__resolvedKey == null || !labelLot__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LabelBatchesDao targetDao = daoSession.getLabelBatchesDao();
            LabelBatches labelLotNew = targetDao.load(__key);
            synchronized (this) {
                labelLot = labelLotNew;
                labelLot__resolvedKey = __key;
            }
        }
        return labelLot;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 582119160)
    public void setLabelLot(@NotNull LabelBatches labelLot) {
        if (labelLot == null) {
            throw new DaoException(
                    "To-one property 'labelLotId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.labelLot = labelLot;
            labelLotId = labelLot.getId();
            labelLot__resolvedKey = labelLotId;
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
    @Generated(hash = 596595756)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLabelSubLotDao() : null;
    }
}
