package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.events.RouteSegmentEntityEventListener;
import cu.phibrain.plugins.cardinal.io.database.entity.model.RouteSegment;
import cu.phibrain.plugins.cardinal.io.database.entity.model.RouteSegmentDao;

public class RouteSegmentOperations extends BaseOperations<RouteSegment, RouteSegmentDao> {

    private static RouteSegmentOperations mInstance = null;


    private RouteSegmentOperations() {
        super();
        initEntityDao();
    }

    public static RouteSegmentOperations getInstance() {
        if (mInstance == null) {
            mInstance = new RouteSegmentOperations();
            mInstance.registerEventDispatcher(new RouteSegmentEntityEventListener());
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        setDao(daoSession.getRouteSegmentDao());
    }

}
