package cu.phibrain.plugins.cardinal.io.database.entity;

import java.util.ArrayList;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.Contract;
import cu.phibrain.plugins.cardinal.io.model.ContractDao;
import cu.phibrain.plugins.cardinal.io.model.Worker;

public class ContractOperations extends BaseRepo<Contract, ContractDao> {

    private static ContractOperations mInstance = null;

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
        dao = daoSession.getContractDao();
    }

    public void insertAll(Long projectId, List<Worker> workers, boolean enable) {
        List<Contract> contracts = new ArrayList<>();
        for (Worker entity : workers) {
            Contract contract = new Contract();
            contract.setActive(enable);
            contract.setProjectId(projectId);
            contract.setWorkerId(entity.getId());
            contracts.add(contract);
        }

        this.insertAll(contracts);

    }

    public Contract findOneBy(long projectId, long userId) {

        return this.queryBuilder().where(ContractDao.Properties.ProjectId.eq(projectId), ContractDao.Properties.WorkerId.eq(userId)).unique();

    }


}
