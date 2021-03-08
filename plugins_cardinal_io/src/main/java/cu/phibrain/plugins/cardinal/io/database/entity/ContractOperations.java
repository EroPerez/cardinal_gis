package cu.phibrain.plugins.cardinal.io.database.entity;

import java.util.ArrayList;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.ProjectsWorkers;
import cu.phibrain.plugins.cardinal.io.model.ProjectsWorkersDao;
import cu.phibrain.plugins.cardinal.io.model.Worker;

public class ContractOperations extends BaseRepo {

    private static ContractOperations mInstance = null;
    private ProjectsWorkersDao dao;

    private ContractOperations() {
        super();
        initEntityDao();
    }

    public static ContractOperations getInstance() {
        if (mInstance == null) {
            mInstance = new ContractOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getProjectsWorkersDao();
    }

    public void insertContractList(List<ProjectsWorkers> projectList) {
        dao.insertOrReplaceInTx(projectList);
    }

    public void insertContractList(Long projectId, List<Worker> workers, boolean enable) {
        List<ProjectsWorkers> contracts = new ArrayList<>();
        for (Worker entity : workers) {
            ProjectsWorkers contract = new ProjectsWorkers();
            contract.setActive(enable);
            contract.setProjectId(projectId);
            contract.setWorkerId(entity.getId());
            contracts.add(contract);
        }

        this.insertContractList(contracts);

    }

    public void insertContract(ProjectsWorkers contract) {
        dao.insertOrReplaceInTx(contract);
    }

    /**
     * @return list of user entity from the table name Entity in the database
     */
    public List<ProjectsWorkers> getContractList() {
        return dao.queryBuilder()
                .list();
    }

    public void delete(Long contractId) {
        dao.deleteByKey(contractId);
    }


    public void update(ProjectsWorkers contract) {
        dao.updateInTx(contract);
    }

}
