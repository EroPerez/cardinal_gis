
package cu.phibrain.plugins.cardinal.io.database.entity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

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

    @SerializedName("map_object_type")
    @Expose
    private Long mapObjecTypeId;

    @SerializedName("default_value")
    @Expose
    private String defaultValue;

    @SerializedName("description")
    @Expose
    private String description;

    private final static long serialVersionUID = -376582677171152532L;

@Generated(hash = 2090174285)
public MapObjectTypeAttribute(Long id, Integer atype, String name, Long mapObjecTypeId,
        String defaultValue, String description) {
    this.id = id;
    this.atype = atype;
    this.name = name;
    this.mapObjecTypeId = mapObjecTypeId;
    this.defaultValue = defaultValue;
    this.description = description;
}

@Generated(hash = 407216157)
public MapObjectTypeAttribute() {
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

public Long getMapObjecTypeId() {
    return this.mapObjecTypeId;
}

public void setMapObjecTypeId(Long mapObjecTypeId) {
    this.mapObjecTypeId = mapObjecTypeId;
}

public String getDefaultValue() {
    return this.defaultValue;
}

public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
}

public String getDescription() {
    return this.description;
}

public void setDescription(String description) {
    this.description = description;
}


}
