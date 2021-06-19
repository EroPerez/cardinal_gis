package cu.phibrain.plugins.cardinal.io.database.base;

import org.greenrobot.greendao.AbstractDao;

import java.util.ArrayList;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.model.ContractDao;
import cu.phibrain.plugins.cardinal.io.model.LabelBatchesDao;
import cu.phibrain.plugins.cardinal.io.model.LabelMaterialDao;
import cu.phibrain.plugins.cardinal.io.model.LabelSubLotDao;
import cu.phibrain.plugins.cardinal.io.model.LayerDao;
import cu.phibrain.plugins.cardinal.io.model.MapObjecTypeDao;
import cu.phibrain.plugins.cardinal.io.model.MapObjecTypeDefectDao;
import cu.phibrain.plugins.cardinal.io.model.MapObjecTypeStateDao;
import cu.phibrain.plugins.cardinal.io.model.MapObjectDao;
import cu.phibrain.plugins.cardinal.io.model.MapObjectHasDefectDao;
import cu.phibrain.plugins.cardinal.io.model.MapObjectHasDefectHasImagesDao;
import cu.phibrain.plugins.cardinal.io.model.MapObjectHasStateDao;
import cu.phibrain.plugins.cardinal.io.model.MapObjectImagesDao;
import cu.phibrain.plugins.cardinal.io.model.MapObjectMetadataDao;
import cu.phibrain.plugins.cardinal.io.model.MapObjectTypeAttributeDao;
import cu.phibrain.plugins.cardinal.io.model.MaterialDao;
import cu.phibrain.plugins.cardinal.io.model.NetworksDao;
import cu.phibrain.plugins.cardinal.io.model.ProjectDao;
import cu.phibrain.plugins.cardinal.io.model.RouteSegmentDao;
import cu.phibrain.plugins.cardinal.io.model.SignalEventsDao;
import cu.phibrain.plugins.cardinal.io.model.StockDao;
import cu.phibrain.plugins.cardinal.io.model.SupplierDao;
import cu.phibrain.plugins.cardinal.io.model.TopologicalRuleDao;
import cu.phibrain.plugins.cardinal.io.model.WorkSessionDao;
import cu.phibrain.plugins.cardinal.io.model.WorkerDao;
import cu.phibrain.plugins.cardinal.io.model.WorkerRouteDao;
import cu.phibrain.plugins.cardinal.io.model.ZoneDao;


/**
 * Can be Auto Generated using Annotation Processing
 * Getting all the classes with @UserEntity annotation and adding it to the list
 */
public class DaoHelper {

    public static List<Class<? extends AbstractDao<?, ?>>> getAllDaos() {
        List<Class<? extends AbstractDao<?, ?>>> daos = new ArrayList<>();
        daos.add(ProjectDao.class);
        daos.add(ContractDao.class);
        daos.add(StockDao.class);
        daos.add(WorkerDao.class);
        daos.add(ZoneDao.class);
        daos.add(NetworksDao.class);
        daos.add(LayerDao.class);
        daos.add(MapObjecTypeDao.class);
        daos.add(MapObjecTypeDefectDao.class);
        daos.add(MapObjecTypeStateDao.class);
        daos.add(MapObjectTypeAttributeDao.class);
        daos.add(SupplierDao.class);
        daos.add(LabelBatchesDao.class);
        daos.add(LabelMaterialDao.class);
        daos.add(LabelSubLotDao.class);
        daos.add(MapObjectHasDefectHasImagesDao.class);
        daos.add(MapObjectHasDefectDao.class);
        daos.add(MapObjectHasStateDao.class);
        daos.add(MapObjectImagesDao.class);
        daos.add(MapObjectMetadataDao.class);
        daos.add(MapObjectDao.class);
        daos.add(MapObjectMetadataDao.class);
        daos.add(MaterialDao.class);
        daos.add(RouteSegmentDao.class);
        daos.add(SignalEventsDao.class);
        daos.add(TopologicalRuleDao.class);
        daos.add(WorkerRouteDao.class);
        daos.add(WorkSessionDao.class);


        return daos;
    }
}
