package cu.phibrain.plugins.cardinal.io.database.entity;

import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.Zone;
import cu.phibrain.plugins.cardinal.io.model.ZoneDao;


public class ZoneOperations extends BaseRepo {

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

    public void insertZoneList(List<Zone> zoneList) {
        dao.insertOrReplaceInTx(zoneList);
    }


    public void insertZone(Zone zone) {
        dao.insertOrReplaceInTx(zone);
    }

    /**
     * @return list of user entity from the table name Entity in the database
     */
    public List<Zone> getZoneList() {
        return dao.queryBuilder()
                .list();
    }

    public void delete(Long zoneId) {
        dao.deleteByKey(zoneId);
    }


    public void update(Zone zone) {
        dao.updateInTx(zone);
    }
}
