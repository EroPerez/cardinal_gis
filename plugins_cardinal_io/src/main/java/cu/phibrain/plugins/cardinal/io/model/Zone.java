
package cu.phibrain.plugins.cardinal.io.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.model.converter.GPGeoPointListConverter;
import eu.geopaparazzi.map.GPGeoPoint;


/**
 * Created by Ero on 17/09/2020.
 * Entity mapped to table "zone".
 */
@Entity(
        nameInDb = "MANAGER_ZONE",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class Zone implements Serializable {

    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("name")
    @Expose
    private String name;

    @Convert(converter = GPGeoPointListConverter.class, columnType = String.class)
    @SerializedName("boundingBox")
    @Expose
    private List<GPGeoPoint> boundingBox;

    @SerializedName("project")
    @Expose
    private Long projectId;


    private final static long serialVersionUID = 7550532611376296856L;

    /**
     * No args constructor for use in serialization
     */
    public Zone() {
    }

    @Generated(hash = 709990726)
    public Zone(Long id, String name, List<GPGeoPoint> boundingBox, Long projectId) {
        this.id = id;
        this.name = name;
        this.boundingBox = boundingBox;
        this.projectId = projectId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getProjectId() {
        return this.projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public List<GPGeoPoint> getBoundingBox() {
        return this.boundingBox;
    }

    public void setBoundingBox(List<GPGeoPoint> boundingBox) {
        this.boundingBox = boundingBox;
    }

}
