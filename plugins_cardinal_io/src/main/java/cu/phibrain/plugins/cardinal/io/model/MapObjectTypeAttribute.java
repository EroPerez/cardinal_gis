
package cu.phibrain.plugins.cardinal.io.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Ero on 17/02/2021.
 * Entity mapped to table "MapobjectypeAttribute".
 */
@Entity(
        nameInDb = "CARDINAL_MAP_OBJECT_TYPE_ATTRIBUTE",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)

public class MapObjectTypeAttribute implements Serializable {

    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("atype")
    @Expose
    private Integer atype;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("map_object_type")
    @Expose
    private Long mapObjecTypeId;

    private final static long serialVersionUID = -376582677171152532L;

    /**
     * No args constructor for use in serialization
     */
    public MapObjectTypeAttribute() {
    }

    @Generated(hash = 439013546)
    public MapObjectTypeAttribute(Long id, Integer atype, String name, String value,
                                  Long mapObjecTypeId) {
        this.id = id;
        this.atype = atype;
        this.name = name;
        this.value = value;
        this.mapObjecTypeId = mapObjecTypeId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getAtype() {
        return this.atype;
    }

    public void setAtype(Integer atype) {
        this.atype = atype;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getMapObjecTypeId() {
        return this.mapObjecTypeId;
    }

    public void setMapObjecTypeId(Long mapObjecTypeId) {
        this.mapObjecTypeId = mapObjecTypeId;
    }

}
