package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.Project;
import cu.phibrain.plugins.cardinal.io.model.ProjectDao;

public class ProjectOperations extends BaseRepo<Project, ProjectDao> {

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
        dao = daoSession.getProjectDao();
    }

}
