package cu.phibrain.plugins.cardinal.io.database.entity;

import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.Worker;
import cu.phibrain.plugins.cardinal.io.model.WorkerDao;


public class WorkerOperations extends BaseRepo {

    private static WorkerOperations mInstance = null;
    private WorkerDao dao;

    private WorkerOperations() {
        super();
        initEntityDao();
    }

    public static WorkerOperations getInstance() {
        if (mInstance == null) {
            mInstance = new WorkerOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getWorkerDao();
    }

    public void insertWorkerList(List<Worker> workerList) {
        dao.insertOrReplaceInTx(workerList);
    }


    public void insertWorker(Worker worker) {
        dao.insertOrReplaceInTx(worker);
    }

    /**
     * @return list of user entity from the table name Entity in the database
     */
    public List<Worker> getWorkerList() {
        return dao.queryBuilder()
                .list();
    }

    public void delete(Long projectId) {
        dao.deleteByKey(projectId);
    }


    public void update(Worker project) {
        dao.updateInTx(project);
    }
}
