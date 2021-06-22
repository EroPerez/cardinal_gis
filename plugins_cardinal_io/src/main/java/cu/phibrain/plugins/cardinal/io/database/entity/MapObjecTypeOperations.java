package cu.phibrain.plugins.cardinal.io.database.entity;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.model.MapObjecTypeDao;
import cu.phibrain.plugins.cardinal.io.model.MapObject;
import cu.phibrain.plugins.cardinal.io.model.TopologicalRule;

public class MapObjecTypeOperations extends BaseRepo<MapObjecType, MapObjecTypeDao> {

    private static MapObjecTypeOperations mInstance = null;

    private MapObjecTypeOperations() {
        super();
        initEntityDao();
    }

    public static MapObjecTypeOperations getInstance() {
        if (mInstance == null) {
            mInstance = new MapObjecTypeOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getMapObjecTypeDao();
    }


    public List<MapObjecType> topologicalMtoFirewall(MapObjecType mto, List<MapObjecType> objcTypeList) {
        if (objcTypeList == null)
            objcTypeList = new ArrayList<>();

        //Yo tambien me incluyo
        if (!mto.getIsAbstract())
            objcTypeList.add(mto);

        List<TopologicalRule> rulers = mto.getTopoRule();
        for (TopologicalRule rule : rulers) {
            if (!rule.getTargetObj().getIsAbstract())
                objcTypeList.add(rule.getTargetObj());
        }
        if (mto.getParentObj() != null)
            topologicalMtoFirewall(mto.getParentObj(), objcTypeList);

        return objcTypeList;
    }

}
