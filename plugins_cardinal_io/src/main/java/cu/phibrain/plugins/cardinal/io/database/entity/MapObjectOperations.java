package cu.phibrain.plugins.cardinal.io.database.entity;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.model.MapObjecTypeDao;
import cu.phibrain.plugins.cardinal.io.model.MapObject;
import cu.phibrain.plugins.cardinal.io.model.MapObjectDao;
import cu.phibrain.plugins.cardinal.io.model.TopologicalRule;

public class MapObjectOperations extends BaseRepo<MapObject, MapObjectDao> {

    private static MapObjectOperations mInstance = null;

    private MapObjectOperations() {
        super();
        initEntityDao();
    }

    public static MapObjectOperations getInstance() {
        if (mInstance == null) {
            mInstance = new MapObjectOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getMapObjectDao();
    }

    public List<MapObjecType> topologicalMtoFirewall(MapObject mapObject){
        List<MapObjecType> objcTypeList = new ArrayList<>();
        for (TopologicalRule rule: mapObject.getObjectType().getTopoRule()) {
            MapObjecType target =  rule.getTargetObj();
            if (target.getIsAbstract()){
                objcTypeList.addAll(MapObjecTypeOperations.getInstance().searchChildMto(objcTypeList, target));
            }
            else{
                objcTypeList.add(target);
            }
        }
        return objcTypeList;
    }



}
