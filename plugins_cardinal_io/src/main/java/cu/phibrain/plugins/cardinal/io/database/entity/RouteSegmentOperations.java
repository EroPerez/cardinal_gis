package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.RouteSegment;
import cu.phibrain.plugins.cardinal.io.model.RouteSegmentDao;

public class RouteSegmentOperations extends BaseRepo<RouteSegment, RouteSegmentDao> {

    private static RouteSegmentOperations mInstance = null;


    private RouteSegmentOperations() {
        super();
        initEntityDao();
    }

    public static RouteSegmentOperations getInstance() {
        if (mInstance == null) {
            mInstance = new RouteSegmentOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getRouteSegmentDao();
    }

}
