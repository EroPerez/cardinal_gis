package cu.phibrain.plugins.cardinal.io.database.entity;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.TopologicalRule;
import cu.phibrain.plugins.cardinal.io.model.TopologicalRuleDao;

public class TopologicalRuleOperations extends BaseRepo<TopologicalRule, TopologicalRuleDao> {

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
        dao = daoSession.getTopologicalRuleDao();
    }

}
