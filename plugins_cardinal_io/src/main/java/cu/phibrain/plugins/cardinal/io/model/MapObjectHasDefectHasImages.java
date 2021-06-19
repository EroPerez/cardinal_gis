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
        nameInDb = "CARDINAL_MAP_OBJECT_HAS_DEFECT_HAS_IMAGES",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class MapObjectHasDefectHasImages implements Serializable {
    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("defect")
    @Expose
    private long defectId;

    @SerializedName("filename")
    @Expose
    private String image;

    @SerializedName("created_at")
    @Expose
    private Date createdAt;

    private final static long serialVersionUID = -4499872341492642958L;


    @Generated(hash = 916321420)
    public MapObjectHasDefectHasImages(Long id, long defectId, String image, Date createdAt) {
        this.id = id;
        this.defectId = defectId;
        this.image = image;
        this.createdAt = createdAt;
    }


    @Generated(hash = 576647378)
    public MapObjectHasDefectHasImages() {
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


    public long getDefectId() {
        return this.defectId;
    }


    public void setDefectId(long defectId) {
        this.defectId = defectId;
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
