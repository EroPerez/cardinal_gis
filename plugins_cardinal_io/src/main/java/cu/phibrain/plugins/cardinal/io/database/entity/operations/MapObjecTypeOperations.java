package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.entity.model.Layer;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecTypeDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecTypeDefect;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecTypeState;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectTypeAttribute;
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


    public List<MapObjecType> topologicalMtoFirewall(MapObjecType mto, List<MapObjecType> mapObjecTypeList) {
        if (mapObjecTypeList == null)
            mapObjecTypeList = new ArrayList<>();

        List<TopologicalRule> rulers = mto.getTopoRule();
        for (TopologicalRule rule : rulers) {
            MapObjecType targetObj = rule.getTargetObj();
            Layer layer = targetObj.getLayerObj();
            if (!targetObj.getIsAbstract()
                    && layer.getEnabled()
                    && !mapObjecTypeList.contains(targetObj)) {

                mapObjecTypeList.add(targetObj);
            } else if (targetObj.getIsAbstract()) {
                List<MapObjecType> offspring = this.getOffspring(targetObj.getId());
                for (MapObjecType child : offspring) {
                    Layer childLayer = child.getLayerObj();
                    if (!mapObjecTypeList.contains(child) && childLayer.getEnabled()) {
                        mapObjecTypeList.add(child);
                    }
                }
            }
        }

        if (mto.getParentObj() != null)
            topologicalMtoFirewall(mto.getParentObj(), mapObjecTypeList);

        return mapObjecTypeList;
    }


    public List<MapObjecTypeDefect> getDefects(Long mapObjecTypeId) {
        List<MapObjecTypeDefect> defects = new ArrayList<>();
        MapObjecType mapObjectObjectType = load(mapObjecTypeId);

        while (mapObjectObjectType != null) {

            defects.addAll(mapObjectObjectType.getDefects());

            mapObjectObjectType = mapObjectObjectType.getParentObj();
        }
        return defects;
    }

    public List<MapObjecTypeState> getStates(Long mapObjecTypeId) {
        List<MapObjecTypeState> states = new ArrayList<>();
        MapObjecType mapObjectObjectType = load(mapObjecTypeId);

        while (mapObjectObjectType != null) {

            states.addAll(mapObjectObjectType.getStates());

            mapObjectObjectType = mapObjectObjectType.getParentObj();
        }
        return states;
    }

    public List<MapObjectTypeAttribute> getAttrs(Long mapObjecTypeId) {
        List<MapObjectTypeAttribute> attributes = new ArrayList<>();
        MapObjecType mapObjectObjectType = load(mapObjecTypeId);

        while (mapObjectObjectType != null) {

            attributes.addAll(mapObjectObjectType.getAttributes());

            mapObjectObjectType = mapObjectObjectType.getParentObj();
        }
        return attributes;
    }

    public List<MapObjecType> getOffspring(Long mapObjecTypeId) {
        QueryBuilder<MapObjecType> queryBuilder = this.queryBuilder()
                .where(MapObjecTypeDao.Properties.ParentId.eq(mapObjecTypeId));

        return queryBuilder.list();
    }

}
