
package cu.phibrain.plugins.cardinal.io.database.entity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;


/**
 * Created by Ero on 17/02/2021.
 * Entity mapped to table "MapObjecTypeDefect".
 */
@Entity(
        nameInDb = "CARDINAL_MAP_OBJECT_TYPE_DEFECT",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class MapObjecTypeDefect implements Serializable, IEntity{

    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("icon")
    @Expose
    private byte[] icon;

    @SerializedName("map_object_type")
    @Expose
    private Long mapObjecTypeId;

    private final static long serialVersionUID = -8603297511613776193L;

    /**
     * No args constructor for use in serialization
     */
    public MapObjecTypeDefect() {
    }

    @Generated(hash = 2008552689)
    public MapObjecTypeDefect(Long id, String description, byte[] icon, Long mapObjecTypeId) {
        this.id = id;
        this.description = description;
        this.icon = icon;
        this.mapObjecTypeId = mapObjecTypeId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getIcon() {
        return this.icon;
    }

    public void setIcon(byte[] icon) {
        this.icon = icon;
    }

    public Long getMapObjecTypeId() {
        return this.mapObjecTypeId;
    }

    public void setMapObjecTypeId(Long mapObjecTypeId) {
        this.mapObjecTypeId = mapObjecTypeId;
    }
}
