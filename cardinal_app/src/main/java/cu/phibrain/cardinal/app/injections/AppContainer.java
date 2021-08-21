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
import cu.phibrain.plugins.cardinal.io.database.entity.model.RouteSegment;
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
    private RouteSegment RouteSegmentActive;

    protected UserMode umode;

    public AppContainer() {
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


    public Project getProjectActive() {
        try {
            refreshProject();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

        if (Id != null) {
            if (this.mapObjectActive != null)
                MapObjectOperations.getInstance().detach(this.mapObjectActive);

            mapObjectActive = MapObjectOperations.getInstance().load(Id);
        }

    }

    public MapObject getCurrentMapObject() {
        try {
            refreshCMO();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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


    public Boolean IsCurrentActiveLayerTopological() {
        if (this.mapObjectActive != null) {
            return this.mapObjectActive.belongToTopoLayer();
        }

        return false;
    }


    public UserMode getMode() {
        return umode;
    }

    public void setMode(UserMode umode) {
        this.umode = umode;
    }

    public RouteSegment getRouteSegmentActive() {
        return RouteSegmentActive;
    }

    public void setRouteSegmentActive(RouteSegment routeSegmentActive) {
        RouteSegmentActive = routeSegmentActive;
    }
}
