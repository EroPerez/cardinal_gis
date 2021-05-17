package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.Networks;
import cu.phibrain.plugins.cardinal.io.model.NetworksDao;


public class NetworksOperations extends BaseRepo<Networks, NetworksDao> {

    private static NetworksOperations mInstance = null;

    private NetworksOperations() {
        super();
        initEntityDao();
    }

    public static NetworksOperations getInstance() {
        if (mInstance == null) {
            mInstance = new NetworksOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getNetworksDao();
    }

}

