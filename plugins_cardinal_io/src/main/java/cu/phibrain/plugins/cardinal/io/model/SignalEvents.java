package cu.phibrain.plugins.cardinal.io.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.Date;

import cu.phibrain.plugins.cardinal.io.model.converter.SignalTypesConverter;

/**
 * Created by Ero on 04/05/2021.
 * Entity mapped to table "zone".
 */
@Entity(
        nameInDb = "CARDINAL_SIGNAL_EVENTS",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class SignalEvents implements Serializable {
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

    @SerializedName("level")
    @Expose
    private long level;

    public enum SignalTypes {
        @SerializedName("0")
        GPS(0),
        @SerializedName("1")
        GSM(1),
        @SerializedName("2")
        POWER(2),
        @SerializedName("3")
        STORAGE(3);

        private final int id;

        public int getId() {
            return id;
        }

        @Nullable
        public static SignalTypes fromId(int id) {
            for (SignalTypes type : SignalTypes.values()) {
                if (type.getId() == id) {
                    return type;
                }
            }
            return null;
        }

        SignalTypes(int id) {
            this.id = id;
        }
    }

    @Convert(converter = SignalTypesConverter.class, columnType = Integer.class)
    @SerializedName("types")
    @Expose
    SignalTypes types;

    @SerializedName("work_session")
    @Expose
    private long sessionId;

    @ToOne(joinProperty = "sessionId")
    private WorkSession session;


    @SerializedName("gps_latitude")
    @Expose
    private double gpsLattitude;

    @SerializedName("gps_longitude")
    @Expose
    private double gpsLongitude;

    private final static long serialVersionUID = 66866789217578488L;

    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1709751359)
    private transient SignalEventsDao myDao;

    @Generated(hash = 834067561)
    public SignalEvents(Long id, Date startDate, Date endDate, long level, SignalTypes types,
                        long sessionId, double gpsLattitude, double gpsLongitude) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.level = level;
        this.types = types;
        this.sessionId = sessionId;
        this.gpsLattitude = gpsLattitude;
        this.gpsLongitude = gpsLongitude;
    }

    @Generated(hash = 500815275)
    public SignalEvents() {
    }

    public SignalEvents(SignalTypes types, Date startDate, Date endDate, long level, long sessionId,
                        double gpsLat, double gpsLon) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.level = level;
        this.types = types;
        this.sessionId = sessionId;
        this.gpsLattitude = gpsLat;
        this.gpsLongitude = gpsLon;
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

    public long getLevel() {
        return this.level;
    }

    public void setLevel(long level) {
        this.level = level;
    }

    public SignalTypes getTypes() {
        return this.types;
    }

    public void setTypes(SignalTypes types) {
        this.types = types;
    }

    public long getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    @Generated(hash = 274049648)
    private transient Long session__resolvedKey;

    /**
     * To-one relationship, resolved on first access.
     */
    @Generated(hash = 412766052)
    public WorkSession getSession() {
        long __key = this.sessionId;
        if (session__resolvedKey == null || !session__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            WorkSessionDao targetDao = daoSession.getWorkSessionDao();
            WorkSession sessionNew = targetDao.load(__key);
            synchronized (this) {
                session = sessionNew;
                session__resolvedKey = __key;
            }
        }
        return session;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1329887016)
    public void setSession(@NotNull WorkSession session) {
        if (session == null) {
            throw new DaoException(
                    "To-one property 'sessionId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.session = session;
            sessionId = session.getId();
            session__resolvedKey = sessionId;
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

    public double getGpsLattitude() {
        return this.gpsLattitude;
    }

    public void setGpsLattitude(double gpsLattitude) {
        this.gpsLattitude = gpsLattitude;
    }

    public double getGpsLongitude() {
        return this.gpsLongitude;
    }

    public void setGpsLongitude(double gpsLongitude) {
        this.gpsLongitude = gpsLongitude;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 961848201)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSignalEventsDao() : null;
    }
}
