package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.SignalEvents;
import cu.phibrain.plugins.cardinal.io.database.entity.model.SignalEventsDao;

public class SignalEventsOperations extends BaseOperations<SignalEvents, SignalEventsDao> {

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
        setDao(daoSession.getSignalEventsDao());
    }


}
