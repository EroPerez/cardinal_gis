package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.Project;
import cu.phibrain.plugins.cardinal.io.database.entity.model.ProjectDao;

public class ProjectOperations extends BaseOperations<Project, ProjectDao> {

    private static ProjectOperations mInstance = null;


    private ProjectOperations() {
        super();
        initEntityDao();
    }

    public static ProjectOperations getInstance() {
        if (mInstance == null) {
            mInstance = new ProjectOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        setDao(daoSession.getProjectDao());
    }

}
