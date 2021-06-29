package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import cu.phibrain.plugins.cardinal.io.database.entity.model.TopologicalRule;
import cu.phibrain.plugins.cardinal.io.database.entity.model.TopologicalRuleDao;

public class TopologicalRuleOperations extends BaseOperations<TopologicalRule, TopologicalRuleDao> {

    private static TopologicalRuleOperations mInstance = null;

    private TopologicalRuleOperations() {
        super();
        initEntityDao();
    }

    public static TopologicalRuleOperations getInstance() {
        if (mInstance == null) {
            mInstance = new TopologicalRuleOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        setDao(daoSession.getTopologicalRuleDao());
    }

}
