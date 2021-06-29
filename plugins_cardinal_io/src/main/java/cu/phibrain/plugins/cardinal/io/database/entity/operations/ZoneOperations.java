package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.Zone;
import cu.phibrain.plugins.cardinal.io.database.entity.model.ZoneDao;


public class ZoneOperations extends BaseOperations<Zone, ZoneDao> {

    private static ZoneOperations mInstance = null;

    private ZoneOperations() {
        super();
        initEntityDao();
    }

    public static ZoneOperations getInstance() {
        if (mInstance == null) {
            mInstance = new ZoneOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        setDao(daoSession.getZoneDao());
    }

}
