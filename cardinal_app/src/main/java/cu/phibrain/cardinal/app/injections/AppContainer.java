package cu.phibrain.cardinal.app.injections;

import org.hortonmachine.dbs.datatypes.EGeometryType;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Networks;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Project;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkSession;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Worker;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.ProjectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.WorkSessionOperations;
import cu.phibrain.plugins.cardinal.io.utils.CardinalMetadataTableDefaultValues;
import eu.geopaparazzi.core.database.DaoMetadata;
import eu.geopaparazzi.core.database.objects.Metadata;

public class AppContainer {

    protected Project projectActive;
    protected WorkSession workSessionActive;
    protected MapObjecType mapObjecTypeActive;
    protected Networks networksActive;
    protected Long levelActive;
    protected MapObject mapObjectActive;
    protected Worker currentWorker;
    private MapObject edgeAddInMapObjSelect;

    public AppContainer() throws IOException {
//        refreshProject();
        setWorkSessionActive(null);
    }

    public void refreshProject() throws IOException {
        Long projectId = getProjectId();
        //Load Project Active
        if (projectActive == null || projectActive.getId() != projectId) {

            if (projectId != null) {
                projectActive = ProjectOperations.getInstance().load(projectId);
            }
        }
    }

    @Nullable
    private Long getProjectId() throws IOException {
        List<Metadata> projectMetadata = DaoMetadata.getProjectMetadata();
        if (!projectMetadata.isEmpty()) {
            for (final Metadata metaData : projectMetadata) {
                if (metaData.key.toLowerCase().equals(CardinalMetadataTableDefaultValues.PROJECT_ID.getFieldName().toLowerCase())) {
                    return Long.parseLong(metaData.value);
                }
            }
        }
        return null;
    }


    public Project getProjectActive() throws IOException {
        refreshProject();
        return projectActive;
    }

    public WorkSession getWorkSessionActive() {
        if (workSessionActive != null)
            workSessionActive = WorkSessionOperations.getInstance().load(workSessionActive.getId());

        return workSessionActive;
    }

    public void setWorkSessionActive(WorkSession workSessionActive) {
        this.workSessionActive = workSessionActive;
    }

    public Worker getCurrentWorker() {
        return currentWorker;
    }

    public void setCurrentWorker(Worker currentWorker) {
        this.currentWorker = currentWorker;
    }

    public MapObjecType getMapObjecTypeActive() {
        return mapObjecTypeActive;
    }

    public void setMapObjecTypeActive(MapObjecType mapObjecTypeActive) {
        this.mapObjecTypeActive = mapObjecTypeActive;
    }

    public Networks getNetworksActive() {
        return networksActive;
    }

    public void setNetworksActive(Networks networksActive) {
        this.networksActive = networksActive;
    }

    public MapObject getMapObjectActive() {
        if (mapObjectActive != null)
            mapObjectActive = MapObjectOperations.getInstance().load(mapObjectActive.getId());

        return mapObjectActive;
    }

    public void setCurrentMapObject(MapObject mapObjectActive) {
        this.mapObjectActive = mapObjectActive;
    }

    public Long getLevelActive() {
        return levelActive;
    }

    public void setLevelActive(Long levelActive) {
        this.levelActive = levelActive;
    }

    public MapObject getEdgeAddInMapObjSelect() {
        return edgeAddInMapObjSelect;
    }

    public void setPreviousMapObject(MapObject edgeAddInMapObjSelect) {
        this.edgeAddInMapObjSelect = edgeAddInMapObjSelect;
    }


}
