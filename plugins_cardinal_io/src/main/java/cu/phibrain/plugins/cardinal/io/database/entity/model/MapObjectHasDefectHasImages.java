package cu.phibrain.plugins.cardinal.io.database.entity.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.Date;

@Entity(
        nameInDb = "CARDINAL_MAP_OBJECT_HAS_DEFECT_HAS_IMAGES",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class MapObjectHasDefectHasImages implements Serializable, IEntity {
    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("defect")
    @Expose
    private long defectId;

    @SerializedName("filename")
    @Expose
    private byte[] image;

    @SerializedName("created_at")
    @Expose
    private Date createdAt;

    @SerializedName("longitude")
    @Expose
    double lon;
    @SerializedName("latitude")
    @Expose
    double lat;
    @SerializedName("elevation")
    @Expose
    double elevation;

    @SerializedName("azimuth")
    @Expose
    double azimuth;

    private final static long serialVersionUID = -4499872341492642958L;

    @Generated(hash = 724935018)
    public MapObjectHasDefectHasImages(Long id, long defectId, byte[] image, Date createdAt,
                                       double lon, double lat, double elevation, double azimuth) {
        this.id = id;
        this.defectId = defectId;
        this.image = image;
        this.createdAt = createdAt;
        this.lon = lon;
        this.lat = lat;
        this.elevation = elevation;
        this.azimuth = azimuth;
    }

    @Generated(hash = 576647378)
    public MapObjectHasDefectHasImages() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getDefectId() {
        return this.defectId;
    }

    public void setDefectId(long defectId) {
        this.defectId = defectId;
    }

    public byte[] getImage() {
        return this.image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public double getLon() {
        return this.lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return this.lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getElevation() {
        return this.elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    public double getAzimuth() {
        return this.azimuth;
    }

    public void setAzimuth(double azimuth) {
        this.azimuth = azimuth;
    }


}
