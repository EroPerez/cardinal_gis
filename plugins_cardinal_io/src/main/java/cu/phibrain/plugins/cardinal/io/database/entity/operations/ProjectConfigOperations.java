package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.ProjectConfig;
import cu.phibrain.plugins.cardinal.io.database.entity.model.ProjectConfigDao;

public class ProjectConfigOperations extends BaseOperations<ProjectConfig, ProjectConfigDao> {

    private static ProjectConfigOperations mInstance = null;

    private ProjectConfigOperations() {
        super();
        initEntityDao();
    }

    public static ProjectConfigOperations getInstance() {
        if (mInstance == null) {
            mInstance = new ProjectConfigOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        setDao(daoSession.getProjectConfigDao());
    }
}
