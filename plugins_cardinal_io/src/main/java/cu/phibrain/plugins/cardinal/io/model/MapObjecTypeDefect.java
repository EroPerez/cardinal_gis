
package cu.phibrain.plugins.cardinal.io.model;

import java.io.Serializable;

import android.util.Base64;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Entity;


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
public class MapObjecTypeDefect implements Serializable {

    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("map_object_type")
    @Expose
    private Long mapObjecTypeId;

    private final static long serialVersionUID = -8603297511613776193L;

    /**
     * No args constructor for use in serialization
     */
    public MapObjecTypeDefect() {
    }


    public byte[] getIconAsByteArray() {
        return Base64.decode(icon, 0);
    }
}
