package cu.phibrain.plugins.cardinal.io.database.entity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToMany;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;
import java.util.List;

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
public class LabelBatches {
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

    @SerializedName("price")
    @Expose
    private double price;

    @SerializedName("enroll_date")
    @Expose
    Date addedDate;

    @SerializedName("supplier")
    @Expose
    private long supplierId;

    @ToOne(joinProperty = "supplierId")
    Supplier supplierObj;

    @SerializedName("sheet_material")
    @Expose
    private long labelMaterialId;

    @ToOne(joinProperty = "labelMaterialId")
    LabelMaterial labelMaterial;

    @SerializedName("project")
    @Expose
    private long projectId;

    @ToMany(referencedJoinProperty = "lotId")
    @SerializedName("labels")
    @Expose
    private List<Label> labels;

    @SerializedName("digit_count")
    @Expose
    private Integer labelDigitCount;

    private final static long serialVersionUID = 38657824243L;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1665187314)
    private transient LabelBatchesDao myDao;

    @Generated(hash = 129754087)
    public LabelBatches(Long id, String firstLabel, String lastLabel, double price, Date addedDate,
            long supplierId, long labelMaterialId, long projectId, Integer labelDigitCount) {
        this.id = id;
        this.firstLabel = firstLabel;
        this.lastLabel = lastLabel;
        this.price = price;
        this.addedDate = addedDate;
        this.supplierId = supplierId;
        this.labelMaterialId = labelMaterialId;
        this.projectId = projectId;
        this.labelDigitCount = labelDigitCount;
    }

    @Generated(hash = 542786312)
    public LabelBatches() {
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

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getAddedDate() {
        return this.addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public long getSupplierId() {
        return this.supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public long getLabelMaterialId() {
        return this.labelMaterialId;
    }

    public void setLabelMaterialId(long labelMaterialId) {
        this.labelMaterialId = labelMaterialId;
    }

    public long getProjectId() {
        return this.projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    @Generated(hash = 1369517329)
    private transient Long labelMaterial__resolvedKey;

    @Generated(hash = 1162353279)
    private transient Long supplierObj__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 1877343169)
    public LabelMaterial getLabelMaterial() {
        long __key = this.labelMaterialId;
        if (labelMaterial__resolvedKey == null || !labelMaterial__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LabelMaterialDao targetDao = daoSession.getLabelMaterialDao();
            LabelMaterial labelMaterialNew = targetDao.load(__key);
            synchronized (this) {
                labelMaterial = labelMaterialNew;
                labelMaterial__resolvedKey = __key;
            }
        }
        return labelMaterial;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1552392471)
    public void setLabelMaterial(@NotNull LabelMaterial labelMaterial) {
        if (labelMaterial == null) {
            throw new DaoException(
                    "To-one property 'labelMaterialId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.labelMaterial = labelMaterial;
            labelMaterialId = labelMaterial.getId();
            labelMaterial__resolvedKey = labelMaterialId;
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
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 2030208375)
    public Supplier getSupplierObj() {
        long __key = this.supplierId;
        if (supplierObj__resolvedKey == null || !supplierObj__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            SupplierDao targetDao = daoSession.getSupplierDao();
            Supplier supplierObjNew = targetDao.load(__key);
            synchronized (this) {
                supplierObj = supplierObjNew;
                supplierObj__resolvedKey = __key;
            }
        }
        return supplierObj;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 121269031)
    public void setSupplierObj(@NotNull Supplier supplierObj) {
        if (supplierObj == null) {
            throw new DaoException(
                    "To-one property 'supplierId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.supplierObj = supplierObj;
            supplierId = supplierObj.getId();
            supplierObj__resolvedKey = supplierId;
        }
    }

    public Integer getLabelDigitCount() {
        return this.labelDigitCount;
    }

    public void setLabelDigitCount(Integer labelDigitCount) {
        this.labelDigitCount = labelDigitCount;
    }

    /**
     * To-many relationship, resolved on first access (and after reset).
     * Changes to to-many relations are not persisted, make changes to the target entity.
     */
    @Generated(hash = 1099965371)
    public List<Label> getLabels() {
        if (labels == null) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LabelDao targetDao = daoSession.getLabelDao();
            List<Label> labelsNew = targetDao._queryLabelBatches_Labels(id);
            synchronized (this) {
                if (labels == null) {
                    labels = labelsNew;
                }
            }
        }
        return labels;
    }

    /** Resets a to-many relationship, making the next get call to query for a fresh result. */
    @Generated(hash = 902294403)
    public synchronized void resetLabels() {
        labels = null;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 217283870)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLabelBatchesDao() : null;
    }
}
