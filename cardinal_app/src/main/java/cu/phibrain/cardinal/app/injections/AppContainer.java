package cu.phibrain.cardinal.app.injections;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

import cu.phibrain.cardinal.app.helpers.NumberUtiles;
import cu.phibrain.plugins.cardinal.io.database.entity.model.IEntity;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Networks;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Project;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkSession;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Worker;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjecTypeOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.NetworksOperations;
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
    protected MapObject mapObjectActive;
    protected Worker currentWorker;
    private MapObject edgeAddInMapObjSelect;

    protected UserMode umode;

    public AppContainer() throws IOException {
//        refreshProject();
//        setWorkSessionActive(null);
        setMode(UserMode.NONE);
    }

    public void refreshProject() throws IOException {
        Long projectId = geId(CardinalMetadataTableDefaultValues.PROJECT_ID.getFieldName());
        //Load Project Active
        if (projectActive == null || projectActive.getId() != projectId) {

            if (projectId != null) {
                projectActive = ProjectOperations.getInstance().load(projectId);
            }
        }
    }

    @Nullable
    private Long geId(String key) throws IOException {
        List<Metadata> projectMetadata = DaoMetadata.getProjectMetadata();
        if (!projectMetadata.isEmpty()) {
            for (final Metadata metaData : projectMetadata) {
                if (metaData.key.toLowerCase().equals(key.toLowerCase())) {
                    return NumberUtiles.parseStringToLong(metaData.value, null);
                }
            }
        }
        return null;
    }


    private void setValue(String key, IEntity entity) {
        try {
            DaoMetadata.setValue(null,
                    key,
                    entity != null ? String.valueOf(entity.getId()) : ""
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Project getProjectActive() throws IOException {
        refreshProject();
        return projectActive;
    }

    private void refreshSession() throws IOException {
        Long sessionId = geId(CardinalMetadataTableDefaultValues.WORK_SESSION_ID.getFieldName());
        //Load Project Active
        if (workSessionActive == null || workSessionActive.getId() != sessionId) {

            if (sessionId != null) {
                workSessionActive = WorkSessionOperations.getInstance().load(sessionId);
                currentWorker = workSessionActive.getContractObj().getTheWorker();
            }
        }
    }


    public WorkSession getWorkSessionActive() {
        try {
            refreshSession();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return workSessionActive;
    }

    public void setWorkSessionActive(WorkSession workSessionActive) {
        this.workSessionActive = workSessionActive;

        setValue(
                CardinalMetadataTableDefaultValues.WORK_SESSION_ID.getFieldName(),
                this.workSessionActive
        );

    }

    public Worker getCurrentWorker() {
        try {
            refreshSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return currentWorker;
    }

    public void setCurrentWorker(Worker currentWorker) {
        this.currentWorker = currentWorker;
    }

    private void refreshMOT() throws IOException {
        Long motId = geId(CardinalMetadataTableDefaultValues.MAP_OBJECT_TYPE_ID.getFieldName());
        //Load Project Active
        if (mapObjecTypeActive == null || mapObjecTypeActive.getId() != motId) {

            if (motId != null) {
                mapObjecTypeActive = MapObjecTypeOperations.getInstance().load(motId);
            }
        }
    }


    public MapObjecType getMapObjecTypeActive() {
        try {
            refreshMOT();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapObjecTypeActive;
    }

    public void setMapObjecTypeActive(MapObjecType mapObjecTypeActive) {
        this.mapObjecTypeActive = mapObjecTypeActive;

        setValue(
                CardinalMetadataTableDefaultValues.MAP_OBJECT_TYPE_ID.getFieldName(),
                this.mapObjecTypeActive
        );

    }

    private void refreshNetwork() throws IOException {
        Long netId = geId(CardinalMetadataTableDefaultValues.NETWORK_ID.getFieldName());
        //Load Project Active
        if (networksActive == null || networksActive.getId() != netId) {

            if (netId != null) {
                networksActive = NetworksOperations.getInstance().load(netId);
            }
        }
    }

    public Networks getNetworksActive() {
        try {
            refreshNetwork();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return networksActive;
    }

    public void setNetworksActive(Networks networksActive) {
        this.networksActive = networksActive;

        setValue(
                CardinalMetadataTableDefaultValues.NETWORK_ID.getFieldName(),
                this.networksActive
        );
    }


    private void refreshCMO() throws IOException {
        Long Id = geId(CardinalMetadataTableDefaultValues.CURRENT_MAP_OBJECT_ID.getFieldName());
        //Load Project Active
        if (mapObjectActive == null || mapObjectActive.getId() != Id) {

            if (Id != null) {
                mapObjectActive = MapObjectOperations.getInstance().load(Id);
            }
        }
    }

    public MapObject getCurrentMapObject() {
        try {
            refreshCMO();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mapObjectActive;
    }

    public void setCurrentMapObject(MapObject mapObjectActive) {
        this.mapObjectActive = mapObjectActive;
        setValue(
                CardinalMetadataTableDefaultValues.CURRENT_MAP_OBJECT_ID.getFieldName(),
                this.mapObjectActive
        );
    }


    private void refreshPMO() throws IOException {
        Long Id = geId(CardinalMetadataTableDefaultValues.PREVIOUS_MAP_OBJECT_ID.getFieldName());
        //Load Project Active
        if (edgeAddInMapObjSelect == null || edgeAddInMapObjSelect.getId() != Id) {

            if (Id != null) {
                edgeAddInMapObjSelect = MapObjectOperations.getInstance().load(Id);
            }
        }
    }

    public MapObject getPreviousMapObject() {
        try {
            refreshPMO();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return edgeAddInMapObjSelect;
    }

    public void setPreviousMapObject(MapObject edgeAddInMapObjSelect) {
        this.edgeAddInMapObjSelect = edgeAddInMapObjSelect;
        setValue(
                CardinalMetadataTableDefaultValues.PREVIOUS_MAP_OBJECT_ID.getFieldName(),
                this.edgeAddInMapObjSelect
        );
    }


    public UserMode getMode() {
        return umode;
    }

    public void setMode(UserMode umode) {
        this.umode = umode;
    }
}
