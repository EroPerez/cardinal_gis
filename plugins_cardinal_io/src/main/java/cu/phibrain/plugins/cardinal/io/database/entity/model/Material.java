package cu.phibrain.plugins.cardinal.io.database.entity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.Date;

@Entity(
        nameInDb = "MANAGER_MATERIAL",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class Material implements Serializable, IEntity{
    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("worker_session")
    @Expose
    public Long workerSessionId;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("unit")
    @Expose
    private String unit;

    @SerializedName("delivery_quantity")
    @Expose
    int deliveryQuantity;

    @SerializedName("delivery_date")
    @Expose
    Date deliveryDate;

    @SerializedName("pick_back_quantity")
    @Expose
    int pickBackQuantity;

    @SerializedName("pick_back_date")
    @Expose
    Date pickBackDate;

    @SerializedName("norm")
    @Expose
    double norm;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("use")
    @Expose
    public Boolean use;

    private final static long serialVersionUID = -94600434386243L;

    @Generated(hash = 1276089886)
    public Material(Long id, Long workerSessionId, String name, String unit,
                    int deliveryQuantity, Date deliveryDate, int pickBackQuantity, Date pickBackDate,
                    double norm, String description, Boolean use) {
        this.id = id;
        this.workerSessionId = workerSessionId;
        this.name = name;
        this.unit = unit;
        this.deliveryQuantity = deliveryQuantity;
        this.deliveryDate = deliveryDate;
        this.pickBackQuantity = pickBackQuantity;
        this.pickBackDate = pickBackDate;
        this.norm = norm;
        this.description = description;
        this.use = use;
    }

    @Generated(hash = 1176792654)
    public Material() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkerSessionId() {
        return this.workerSessionId;
    }

    public void setWorkerSessionId(Long workerSessionId) {
        this.workerSessionId = workerSessionId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getDeliveryQuantity() {
        return this.deliveryQuantity;
    }

    public void setDeliveryQuantity(int deliveryQuantity) {
        this.deliveryQuantity = deliveryQuantity;
    }

    public Date getDeliveryDate() {
        return this.deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public int getPickBackQuantity() {
        return this.pickBackQuantity;
    }

    public void setPickBackQuantity(int pickBackQuantity) {
        this.pickBackQuantity = pickBackQuantity;
    }

    public Date getPickBackDate() {
        return this.pickBackDate;
    }

    public void setPickBackDate(Date pickBackDate) {
        this.pickBackDate = pickBackDate;
    }

    public double getNorm() {
        return this.norm;
    }

    public void setNorm(double norm) {
        this.norm = norm;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getUse() {
        return this.use;
    }

    public void setUse(Boolean use) {
        this.use = use;
    }

}
