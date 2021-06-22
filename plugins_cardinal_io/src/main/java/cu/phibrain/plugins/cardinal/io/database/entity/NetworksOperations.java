package cu.phibrain.plugins.cardinal.io.database.entity;

import java.util.ArrayList;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.Layer;
import cu.phibrain.plugins.cardinal.io.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.model.Networks;
import cu.phibrain.plugins.cardinal.io.model.NetworksDao;


public class NetworksOperations extends BaseRepo<Networks, NetworksDao> {

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
        dao = daoSession.getNetworksDao();
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
        List<MapObjecType> mapObjceTypeList = new ArrayList<>();
        for (Layer layer : network.getLayers()) {
            for (MapObjecType mto : layer.getMapobjectypes()) {
                if (!mto.getIsAbstract())
                    mapObjceTypeList.add(mto);
            }
        }
        return mapObjceTypeList;
    }


}

