package cu.phibrain.plugins.cardinal.io.model;


import android.util.Base64;

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
    private String image;

    @SerializedName("created_at")
    @Expose
    private Date createdAt;

    private final static long serialVersionUID = -4499234149200958L;

    @Generated(hash = 42442288)
    public MapObjectImages(Long id, long mapObjectId, String image, Date createdAt) {
        this.id = id;
        this.mapObjectId = mapObjectId;
        this.image = image;
        this.createdAt = createdAt;
    }

    @Generated(hash = 212149383)
    public MapObjectImages() {
    }

    public byte[] getImageAsByteArray() {
        return Base64.decode(this.image, 0);
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

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Date getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
