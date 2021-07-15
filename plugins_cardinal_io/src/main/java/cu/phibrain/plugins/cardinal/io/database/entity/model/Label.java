package cu.phibrain.plugins.cardinal.io.database.entity.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.ToOne;

import java.io.Serializable;

@Entity(
        nameInDb = "MANAGER_LABEL",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class Label implements Serializable, IEntity{
    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("lot")
    @Expose
    private Long lotId;

    @ToOne(joinProperty = "lotId")
    LabelBatches lotObj;

    private final static long serialVersionUID = 6686924243L;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 40777009)
    private transient LabelDao myDao;

    @Generated(hash = 915655212)
    public Label(Long id, String code, Long lotId) {
        this.id = id;
        this.code = code;
        this.lotId = lotId;
    }

    @Generated(hash = 2137109701)
    public Label() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getLotId() {
        return this.lotId;
    }

    public void setLotId(Long lotId) {
        this.lotId = lotId;
    }

    @Generated(hash = 1151388711)
    private transient Long lotObj__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 483109320)
    public LabelBatches getLotObj() {
        Long __key = this.lotId;
        if (lotObj__resolvedKey == null || !lotObj__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            LabelBatchesDao targetDao = daoSession.getLabelBatchesDao();
            LabelBatches lotObjNew = targetDao.load(__key);
            synchronized (this) {
                lotObj = lotObjNew;
                lotObj__resolvedKey = __key;
            }
        }
        return lotObj;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1874355374)
    public void setLotObj(LabelBatches lotObj) {
        synchronized (this) {
            this.lotObj = lotObj;
            lotId = lotObj == null ? null : lotObj.getId();
            lotObj__resolvedKey = lotId;
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
    @Generated(hash = 692607636)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getLabelDao() : null;
    }
}
