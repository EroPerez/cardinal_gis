package cu.phibrain.plugins.cardinal.io.database.entity;

import org.greenrobot.greendao.query.WhereCondition;

import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.base.BaseRepo;
import cu.phibrain.plugins.cardinal.io.model.GroupOfLayer;
import cu.phibrain.plugins.cardinal.io.model.GroupOfLayerDao;


public class GroupOfLayerOperations extends BaseRepo {

    private static GroupOfLayerOperations mInstance = null;
    private GroupOfLayerDao dao;

    private GroupOfLayerOperations() {
        super();
        initEntityDao();
    }

    public static GroupOfLayerOperations getInstance() {
        if (mInstance == null) {
            mInstance = new GroupOfLayerOperations();
        }
        return mInstance;
    }

    @Override
    protected void initEntityDao() {
        dao = daoSession.getGroupOfLayerDao();
    }

    public void insertGroupList(List<GroupOfLayer> groupList) {
        dao.insertOrReplaceInTx(groupList);
    }


    public void insert(GroupOfLayer group) {
        dao.insertOrReplaceInTx(group);
    }

    /**
     * @return list of user entity from the table name UserEntity in the database
     */
    public List<GroupOfLayer> getGroupList() {
        return dao.queryBuilder()
                .list();
    }
    public List<GroupOfLayer> getGroupListByProject(Long project_id) {
        return dao.queryBuilder().where( GroupOfLayerDao.Properties.ProjectId.eq(project_id), new WhereCondition[0]).build().list();
    }
    public void delete(Long groupId) {
        dao.deleteByKey(groupId);
    }


    public void update(GroupOfLayer group) {
        dao.updateInTx(group);
    }

}

