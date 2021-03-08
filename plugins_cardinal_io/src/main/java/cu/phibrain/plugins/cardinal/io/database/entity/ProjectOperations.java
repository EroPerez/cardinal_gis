package cu.phibrain.plugins.cardinal.io.database.entity;

import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.Project;
import cu.phibrain.plugins.cardinal.io.model.ProjectDao;

public class ProjectOperations extends BaseRepo {

    private static ProjectOperations mInstance = null;
    private ProjectDao dao;

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

    public void insertProjectList(List<Project> projectList) {
        dao.insertOrReplaceInTx(projectList);
    }


    public void insertProject(Project project) {
        dao.insertOrReplaceInTx(project);
    }

    /**
     * @return list of user entity from the table name UserEntity in the database
     */
    public List<Project> getProjectList() {
        return dao.queryBuilder()
                .list();
    }

    public void delete(Long projectId) {
        dao.deleteByKey(projectId);
    }


    public void update(Project project) {
        dao.updateInTx(project);
    }

}
