
package cu.phibrain.plugins.cardinal.io.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;


/**
 * Created by Ero on 17/02/2021.
 * Entity mapped to table "MapObjecTypeState".
 */
@Entity(
        nameInDb = "CARDINAL_MAP_OBJECT_TYPE_STATE",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)

public class MapObjecTypeState implements Serializable {

    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("color")
    @Expose
    private String color;

    @SerializedName("map_object_type")
    @Expose
    private Long mapObjecTypeId;

    private final static long serialVersionUID = -1671574559262327338L;

    /**
     * No args constructor for use in serialization
     */
    public MapObjecTypeState() {
    }

    @Generated(hash = 263499671)
    public MapObjecTypeState(Long id, String description, String color, Long mapObjecTypeId) {
        this.id = id;
        this.description = description;
        this.color = color;
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

    public String getColor() {
        return this.color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Long getMapObjecTypeId() {
        return this.mapObjecTypeId;
    }

    public void setMapObjecTypeId(Long mapObjecTypeId) {
        this.mapObjecTypeId = mapObjecTypeId;
    }


}
