package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.SignalEvents;
import cu.phibrain.plugins.cardinal.io.model.SignalEventsDao;

public class SignalEventsOperations extends BaseRepo<SignalEvents, SignalEventsDao> {

    private static SignalEventsOperations mInstance = null;

    private SignalEventsOperations() {
        super();
        initEntityDao();
    }

    public static SignalEventsOperations getInstance() {
        if (mInstance == null) {
            mInstance = new SignalEventsOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getSignalEventsDao();
    }


}
