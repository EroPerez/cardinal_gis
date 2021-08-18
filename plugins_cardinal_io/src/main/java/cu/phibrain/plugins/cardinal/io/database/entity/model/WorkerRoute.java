
package cu.phibrain.plugins.cardinal.io.database.entity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;
import java.util.Date;

@Entity(
        nameInDb = "MANAGER_WORKER_ROUTE",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class WorkerRoute implements Serializable, IExportable {

    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose(serialize = false)
    private Long id;

    Long remoteId;

    @SerializedName("worker_session")
    @Expose
    private Long workerSessionId;

    @SerializedName("latitude")
    @Expose
    @Transient
    private Double latitude;

    @SerializedName("longitude")
    @Expose
    @Transient
    private Double longitude;

    @SerializedName("altitude")
    @Expose
    @Transient
    private Double altitude;

    @SerializedName("created_at")
    @Expose
    @Transient
    private Date createdAt;

    @Expose(serialize = false, deserialize = false)
    private Long gpsLogsTableId;

    @Expose(serialize = false, deserialize = false)
    private Date updatedAt;

    @Expose(serialize = false, deserialize = false)
    private Boolean isSync;

    private final static long serialVersionUID = 3887642485388034854L;

    private Date SyncDate;

    private long SyncPointCount;

    public WorkerRoute(Long id, Long workerSessionId, Double latitude, Double longitude, Double altitude, Date createdAt) {
        this.id = id;
        this.workerSessionId = workerSessionId;
        this.altitude = altitude;
        this.longitude = longitude;
        this.latitude = latitude;
        this.createdAt = createdAt;
        SyncPointCount = 0;
    }


    @Generated(hash = 1839833483)
    public WorkerRoute(Long id, Long remoteId, Long workerSessionId, Long gpsLogsTableId, Date updatedAt, Boolean isSync,
                       Date SyncDate, long SyncPointCount) {
        this.id = id;
        this.remoteId = remoteId;
        this.workerSessionId = workerSessionId;
        this.gpsLogsTableId = gpsLogsTableId;
        this.updatedAt = updatedAt;
        this.isSync = isSync;
        this.SyncDate = SyncDate;
        this.SyncPointCount = SyncPointCount;
    }


    @Generated(hash = 369035617)
    public WorkerRoute() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkerSessionId() {
        return this.workerSessionId;
    }

    public void setWorkerSessionId(Long workerSessionId) {
        this.workerSessionId = workerSessionId;
    }

    public Double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Double getAltitude() {
        return this.altitude;
    }

    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    public Long getGpsLogsTableId() {
        return this.gpsLogsTableId;
    }

    public void setGpsLogsTableId(Long gpsLogsTableId) {
        this.gpsLogsTableId = gpsLogsTableId;
    }


    @Override
    public Date getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public void setUpdatedAt(Date updated) {
        this.updatedAt = updated;
    }

    @Override
    public Boolean getIsSync() {
        return isSync;
    }

    @Override
    public void setIsSync(Boolean isSync) {
        this.isSync = isSync;
    }

    @Override
    public void setSyncDate(Date SyncDate) {

    }

    @Override
    public Date getSyncDate() {
        return this.SyncDate;
    }

    @Override
    public Boolean mustExport() {
        return true;
    }

    @Override
    public IExportable toRemoteObject() {
        return null;
    }

    @Override
    public Long getRemoteId() {
        return remoteId;
    }

    @Override
    public void setRemoteId(Long remoteId) {
        this.remoteId = remoteId;
    }


    public long getSyncPointCount() {
        return this.SyncPointCount;
    }


    public void setSyncPointCount(long SyncPointCount) {
        this.SyncPointCount = SyncPointCount;
    }
}
