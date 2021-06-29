package cu.phibrain.plugins.cardinal.io.database.base;

import org.greenrobot.greendao.AbstractDao;

import java.util.ArrayList;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.entity.model.ContractDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelBatchesDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelMaterialDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelSubLotDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LayerDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecTypeDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecTypeDefectDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecTypeStateDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefectDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefectHasImagesDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasStateDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectImagesDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectMetadataDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectTypeAttributeDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MaterialDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.NetworksDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.ProjectDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.RouteSegmentDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.SignalEventsDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.StockDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.SupplierDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.TopologicalRuleDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkSessionDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkerDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkerRouteDao;
import cu.phibrain.plugins.cardinal.io.database.entity.model.ZoneDao;


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
