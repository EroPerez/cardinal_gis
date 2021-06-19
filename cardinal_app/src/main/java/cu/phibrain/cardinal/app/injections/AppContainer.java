package cu.phibrain.cardinal.app.injections;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.entity.ProjectOperations;
import cu.phibrain.plugins.cardinal.io.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.model.MapObject;
import cu.phibrain.plugins.cardinal.io.model.Networks;
import cu.phibrain.plugins.cardinal.io.model.Project;
import cu.phibrain.plugins.cardinal.io.model.WorkSession;
import cu.phibrain.plugins.cardinal.io.utils.CardinalMetadataTableDefaultValues;
import eu.geopaparazzi.core.database.DaoMetadata;
import eu.geopaparazzi.core.database.objects.Metadata;

public class AppContainer {

    public Project ProjectActive;
    public WorkSession WorkSessionActive;
    public MapObjecType MapObjecTypeActive;
    public Networks NetworksActive;
    public Long LevelActive;
    public MapObject MapObjectActive;

    public AppContainer() throws IOException {
        refreshProject();
    }

    public void refreshProject() throws IOException {

        //Load Project Active
        if(ProjectActive == null || ProjectActive.getId() != getProjectId()) {
            Long project_id = getProjectId();
            if (project_id != null) {
                ProjectActive = ProjectOperations.getInstance().load(project_id);
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



}
