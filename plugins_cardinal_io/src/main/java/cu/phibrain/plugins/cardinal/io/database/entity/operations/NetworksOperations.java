package cu.phibrain.plugins.cardinal.io.database.entity.operations;

import org.locationtech.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.entity.model.Layer;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Networks;
import cu.phibrain.plugins.cardinal.io.database.entity.model.NetworksDao;


public class NetworksOperations extends BaseOperations<Networks, NetworksDao> {

    private static NetworksOperations mInstance = null;

    private NetworksOperations() {
        super();
        initEntityDao();
    }

    public static NetworksOperations getInstance() {
        if (mInstance == null) {
            mInstance = new NetworksOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        setDao(daoSession.getNetworksDao());
    }

    //
    // Get MapObjects Type not Abstract

    /**
     * More efficiente way of filter a collection using all your cpu power with java 8 Stream
     *
     * @param network
     * @return List<MapObjecType>
     */
    public List<MapObjecType> getMapObjectTypes(Networks network) {
        List<MapObjecType> mapObjcTypeList = new ArrayList<>();
        for (Layer layer : network.getLayers()) {
            if(layer.getIsActive()) {
                for (MapObjecType mto : layer.getMapobjectypes()) {
                    if (!mto.getIsAbstract())
                        mapObjcTypeList.add(mto);
                }
            }
        }
        return mapObjcTypeList;
    }

    /**
     * More efficiente way of filter a collection using all your cpu power with java 8 Stream
     *
     * @param network
     * @param geomType
     * @return List<MapObjecType>
     */
    public List<MapObjecType> getMapObjectTypes(Networks network, MapObjecType.GeomType geomType) {
        List<MapObjecType> mapObjcTypeList = new ArrayList<>();
        for (Layer layer : network.getLayers()) {
            if(layer.getIsActive()) {
                for (MapObjecType mto : layer.getMapobjectypes()) {
                    if (!mto.getIsAbstract() && mto.getGeomType() == geomType)
                        mapObjcTypeList.add(mto);
                }
            }
        }
        return mapObjcTypeList;
    }

}

