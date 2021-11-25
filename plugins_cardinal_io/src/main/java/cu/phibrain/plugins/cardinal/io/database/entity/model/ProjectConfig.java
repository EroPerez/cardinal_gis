package cu.phibrain.plugins.cardinal.io.database.entity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;

import cu.phibrain.plugins.cardinal.io.database.entity.model.converter.ConfigTypeConverter;

/**
 * Created by Ero on 17/09/2020.
 * Entity mapped to table "project".
 */
@Entity(
        nameInDb = "MANAGER_PROJECT_CONFIG",
        // Whether an all properties constructor should be generated.
        // A no-args constructor is always required.
        generateConstructors = true,

        // Whether getters and setters for properties should be generated if missing.
        generateGettersSetters = true
)
public class ProjectConfig implements Serializable, IEntity {
    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("value")
    @Expose
    private String value;

    @SerializedName("project")
    @Expose
    private Long projectId;

    @Override
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getProjectId() {
        return this.projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public ConfigType getConfigType() {
        return this.configType;
    }

    public void setConfigType(ConfigType configType) {
        this.configType = configType;
    }

    public enum ConfigType {
        @SerializedName("0")
        MAP_OBJECT_OFFSET(0),
        @SerializedName("1")
        MAP_OBJECT_JOINT_OFFSET(1),
        @SerializedName("2")
        LINE_AND_POLYGON_VIEW_ZOOM(2),
        @SerializedName("3")
        MAP_OBJECT_MIN_IMAGE_TAKEN(3);

        private final int id;

        public int getId() {
            return id;
        }

        @Nullable
        public static ConfigType fromId(int id) {
            for (ConfigType type : ConfigType.values()) {
                if (type.getId() == id) {
                    return type;
                }
            }
            return null;
        }

        ConfigType(int id) {
            this.id = id;
        }
    }

    @Convert(converter = ConfigTypeConverter.class, columnType = Integer.class)
    @SerializedName("config_type")
    @Expose
    private ConfigType configType;

    private final static long serialVersionUID = -900043438657824243L;

    @Generated(hash = 1632291416)
    public ProjectConfig(Long id, String value, Long projectId, ConfigType configType) {
        this.id = id;
        this.value = value;
        this.projectId = projectId;
        this.configType = configType;
    }

    @Generated(hash = 1364120955)
    public ProjectConfig() {
    }
}
