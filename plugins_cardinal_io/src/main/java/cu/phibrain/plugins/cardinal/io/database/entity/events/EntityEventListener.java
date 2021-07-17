package cu.phibrain.plugins.cardinal.io.database.entity.events;

import cu.phibrain.plugins.cardinal.io.database.entity.model.IEntity;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.BaseOperations;

public interface EntityEventListener<Entity extends IEntity, Operation extends BaseOperations> {
    /**
     * Event dispatched before entity it is inserted into database. Take care that entity it not yet
     * in database when this event being dispatch.
     *
     * @param entity
     * @param entityManager
     */
    void onBeforeEntityInsert(Entity entity, Operation entityManager);

    /**
     * Event dispatched after entity it is inserted into database.
     *
     * @param entity
     * @param entityManager
     */
    void onAfterEntityInsert(Entity entity, Operation entityManager);

    /**
     * Event dispatched before entity it is updated in database.
     *
     * @param entity
     * @param entityManager
     */
    void onBeforeEntityUpdate(Entity entity, Operation entityManager);

    /**
     * Event dispatched after entity it is updated in database.
     *
     * @param entity
     * @param entityManager
     */
    void onAfterEntityUpdate(Entity entity, Operation entityManager);


    /**
     * Event dispatched before entity it is delete from database.
     *
     * @param entity
     * @param entityManager
     */
    void onBeforeEntityDelete(Entity entity, Operation entityManager);

    /**
     * Event dispatched after entity it is delete from database. Take into account that entity it
     * not more in database when this event being dispatch.
     *
     * @param entity
     * @param entityManager
     */
    void onAfterEntityDelete(Entity entity, Operation entityManager);
}
