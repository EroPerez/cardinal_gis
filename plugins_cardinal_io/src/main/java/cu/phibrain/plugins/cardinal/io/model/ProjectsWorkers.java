package cu.phibrain.plugins.cardinal.io.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;

@Entity(nameInDb = "MANAGER_PROJECTS_WORKERS")
public class ProjectsWorkers implements Serializable {

    @Id(autoincrement = true)
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("worker")
    @Expose
    private Long workerId;

    @SerializedName("project")
    @Expose
    private Long projectId;

    private boolean active;

    private final static long serialVersionUID = 6686928259217578488L;

    public ProjectsWorkers() {
    }

    @Generated(hash = 1557040132)
    public ProjectsWorkers(Long id, Long workerId, Long projectId, boolean active) {
        this.id = id;
        this.workerId = workerId;
        this.projectId = projectId;
        this.active = active;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkerId() {
        return this.workerId;
    }

    public void setWorkerId(Long workerId) {
        this.workerId = workerId;
    }

    public Long getProjectId() {
        return this.projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public boolean getActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

}
