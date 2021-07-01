package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import java.util.ArrayList;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.entity.model.Layer;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecTypeDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.TopologicalRule;

public class MapObjecTypeOperations extends BaseOperations<MapObjecType, MapObjecTypeDao> {

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
        setDao(daoSession.getMapObjecTypeDao());
    }


    public List<MapObjecType> topologicalMtoFirewall(MapObjecType mto, List<MapObjecType> objcTypeList) {
        if (objcTypeList == null)
            objcTypeList = new ArrayList<>();

        //Yo tambien me incluyo dice jorge que no
        //if(!mto.getIsAbstract())
            //objcTypeList.add(mto);

        List<TopologicalRule> rulers = mto.getTopoRule();
        for (TopologicalRule rule : rulers) {
            Layer layer_mto = LayerOperations.getInstance().load(rule.getTargetObj().getLayerId());
            if (!rule.getTargetObj().getIsAbstract() && layer_mto.getIsActive())
                objcTypeList.add(rule.getTargetObj());
        }

        if (mto.getParentObj() != null)
            topologicalMtoFirewall(mto.getParentObj(), objcTypeList);

        return objcTypeList;
    }

}
