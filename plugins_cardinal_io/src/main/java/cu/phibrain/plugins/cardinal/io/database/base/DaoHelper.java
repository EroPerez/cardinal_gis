package cu.phibrain.plugins.cardinal.io.database.base;

import org.greenrobot.greendao.AbstractDao;

import java.util.ArrayList;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.model.GroupOfLayerDao;
import cu.phibrain.plugins.cardinal.io.model.LayerDao;
import cu.phibrain.plugins.cardinal.io.model.MapObjecTypeDao;
import cu.phibrain.plugins.cardinal.io.model.MapObjecTypeDefectDao;
import cu.phibrain.plugins.cardinal.io.model.MapObjecTypeStateDao;
import cu.phibrain.plugins.cardinal.io.model.MapObjectTypeAttributeDao;
import cu.phibrain.plugins.cardinal.io.model.ProjectDao;
import cu.phibrain.plugins.cardinal.io.model.ProjectsWorkersDao;
import cu.phibrain.plugins.cardinal.io.model.StockDao;
import cu.phibrain.plugins.cardinal.io.model.WorkerDao;
import cu.phibrain.plugins.cardinal.io.model.ZoneDao;

/**
 * Can be Auto Generated using Annotation Processing
 * Getting all the classes with @UserEntity annotation and adding it to the list
 */
public class DaoHelper {

    public static List<Class<? extends AbstractDao<?, ?>>> getAllDaos() {
        List<Class<? extends AbstractDao<?, ?>>> daos = new ArrayList<>();
        daos.add(ProjectDao.class);
        daos.add(ProjectsWorkersDao.class);
        daos.add(StockDao.class);
        daos.add(WorkerDao.class);
        daos.add(ZoneDao.class);
        daos.add(GroupOfLayerDao.class);
        daos.add(LayerDao.class);
        daos.add(MapObjecTypeDao.class);
        daos.add(MapObjecTypeDefectDao.class);
        daos.add(MapObjecTypeStateDao.class);
        daos.add(MapObjectTypeAttributeDao.class);
        return daos;
    }
}
