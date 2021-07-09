package cu.phibrain.plugins.cardinal.io.database.entity.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.Date;

@Entity(
        nameInDb = "CARDINAL_MAP_OBJECT_IMAGES",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class MapObjectImages implements Serializable {
    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("map_object")
    @Expose
    private long mapObjectId;

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


    private final static long serialVersionUID = -4499234149200958L;

    public MapObjectImages(long mapObjectId, byte[] image, Date createdAt, double lon,
                           double lat, double elevation, double azimuth) {
        this.mapObjectId = mapObjectId;
        this.image = image;
        this.createdAt = createdAt;
        this.lon = lon;
        this.lat = lat;
        this.elevation = elevation;
        this.azimuth = azimuth;
    }

    @Generated(hash = 1387825441)
    public MapObjectImages(Long id, long mapObjectId, byte[] image, Date createdAt, double lon,
                           double lat, double elevation, double azimuth) {
        this.id = id;
        this.mapObjectId = mapObjectId;
        this.image = image;
        this.createdAt = createdAt;
        this.lon = lon;
        this.lat = lat;
        this.elevation = elevation;
        this.azimuth = azimuth;
    }

    @Generated(hash = 212149383)
    public MapObjectImages() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getMapObjectId() {
        return this.mapObjectId;
    }

    public void setMapObjectId(long mapObjectId) {
        this.mapObjectId = mapObjectId;
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
