
package cu.phibrain.plugins.cardinal.io.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

/**
 * Created by Ero on 17/09/2020.
 * Entity mapped to table "stock".
 */
@Entity(
        nameInDb = "MANAGER_STOCK",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class Stock implements Serializable {

    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("project")
    @Expose
    private Long projectId;

    private final static long serialVersionUID = -5375448097124347032L;

    /**
     * No args constructor for use in serialization
     */
    public Stock() {
    }

    @Generated(hash = 1958804235)
    public Stock(Long id, String code, Long projectId) {
        this.id = id;
        this.code = code;
        this.projectId = projectId;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getProjectId() {
        return this.projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
