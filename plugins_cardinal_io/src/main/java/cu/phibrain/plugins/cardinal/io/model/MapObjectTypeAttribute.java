
package cu.phibrain.plugins.cardinal.io.model;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Entity;

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
    private Object value;

    @SerializedName("map_object_type")
    @Expose
    private Long mapObjecTypeId;

    private final static long serialVersionUID = -376582677171152532L;

    /**
     * No args constructor for use in serialization
     */
    public MapObjectTypeAttribute() {
    }

}
