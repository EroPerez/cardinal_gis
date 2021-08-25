package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.Devices;
import cu.phibrain.plugins.cardinal.io.database.entity.model.DevicesDao;

public class DevicesOperations extends BaseOperations<Devices, DevicesDao> {

    private static DevicesOperations mInstance = null;

    private DevicesOperations() {
        super();
        initEntityDao();
    }

    public static DevicesOperations getInstance() {
        if (mInstance == null) {
            mInstance = new DevicesOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        setDao(daoSession.getDevicesDao());
    }


}
