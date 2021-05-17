package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.Zone;
import cu.phibrain.plugins.cardinal.io.model.ZoneDao;


public class ZoneOperations extends BaseRepo<Zone, ZoneDao> {

    private static ZoneOperations mInstance = null;
    private ZoneDao dao;

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
        dao = daoSession.getZoneDao();
    }

}
