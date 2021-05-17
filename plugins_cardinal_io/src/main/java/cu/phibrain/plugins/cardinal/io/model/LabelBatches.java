package cu.phibrain.plugins.cardinal.io.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;

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
    Supplier supplier;

    @SerializedName("sheet_material")
    @Expose
    private long labelMaterialId;

    @ToOne(joinProperty = "labelMaterialId")
    LabelMaterial labelMaterial;

    @SerializedName("project")
    @Expose
    private long projectId;

    private final static long serialVersionUID = 38657824243L;

/** Used to resolve relations */
@Generated(hash = 2040040024)
private transient DaoSession daoSession;

/** Used for active entity operations. */
@Generated(hash = 1665187314)
private transient LabelBatchesDao myDao;

@Generated(hash = 2023781983)
public LabelBatches(Long id, String firstLabel, String lastLabel, double price,
        Date addedDate, long supplierId, long labelMaterialId, long projectId) {
    this.id = id;
    this.firstLabel = firstLabel;
    this.lastLabel = lastLabel;
    this.price = price;
    this.addedDate = addedDate;
    this.supplierId = supplierId;
    this.labelMaterialId = labelMaterialId;
    this.projectId = projectId;
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

@Generated(hash = 138112684)
private transient Long supplier__resolvedKey;

/** To-one relationship, resolved on first access. */
@Generated(hash = 111163280)
public Supplier getSupplier() {
    long __key = this.supplierId;
    if (supplier__resolvedKey == null || !supplier__resolvedKey.equals(__key)) {
        final DaoSession daoSession = this.daoSession;
        if (daoSession == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        SupplierDao targetDao = daoSession.getSupplierDao();
        Supplier supplierNew = targetDao.load(__key);
        synchronized (this) {
            supplier = supplierNew;
            supplier__resolvedKey = __key;
        }
    }
    return supplier;
}

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 42879468)
public void setSupplier(@NotNull Supplier supplier) {
    if (supplier == null) {
        throw new DaoException(
                "To-one property 'supplierId' has not-null constraint; cannot set to-one to null");
    }
    synchronized (this) {
        this.supplier = supplier;
        supplierId = supplier.getId();
        supplier__resolvedKey = supplierId;
    }
}

@Generated(hash = 1369517329)
private transient Long labelMaterial__resolvedKey;

/** To-one relationship, resolved on first access. */
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

/** called by internal mechanisms, do not call yourself. */
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

/** called by internal mechanisms, do not call yourself. */
@Generated(hash = 217283870)
public void __setDaoSession(DaoSession daoSession) {
    this.daoSession = daoSession;
    myDao = daoSession != null ? daoSession.getLabelBatchesDao() : null;
}
}
