package cu.phibrain.plugins.cardinal.io.database.entity.events;

import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefect;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefectHasImages;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectHasDefectHasImagesOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectHasDefectOperations;

public class MapObjectHasDefectEventListener implements EntityEventListener<MapObjectHasDefect, MapObjectHasDefectOperations> {

    /**
     * Event dispatched before entity it is inserted into database. Take care that entity it not yet
     * in database when this event being dispatch.
     *
     * @param mapObjectHasDefect
     * @param entityManager
     */
    @Override
    public void onBeforeEntityInsert(MapObjectHasDefect mapObjectHasDefect, MapObjectHasDefectOperations entityManager) {

    }

    /**
     * Event dispatched after entity it is inserted into database.
     *
     * @param mapObjectHasDefect
     * @param entityManager
     */
    @Override
    public void onAfterEntityInsert(MapObjectHasDefect mapObjectHasDefect, MapObjectHasDefectOperations entityManager) {

    }

    /**
     * Event dispatched before entity it is updated in database.
     *
     * @param mapObjectHasDefect
     * @param entityManager
     */
    @Override
    public void onBeforeEntityUpdate(MapObjectHasDefect mapObjectHasDefect, MapObjectHasDefectOperations entityManager) {

    }

    /**
     * Event dispatched after entity it is updated in database.
     *
     * @param mapObjectHasDefect
     * @param entityManager
     */
    @Override
    public void onAfterEntityUpdate(MapObjectHasDefect mapObjectHasDefect, MapObjectHasDefectOperations entityManager) {

    }

    /**
     * Event dispatched before entity it is delete from database.
     *
     * @param mapObjectHasDefect
     * @param entityManager
     */
    @Override
    public void onBeforeEntityDelete(MapObjectHasDefect mapObjectHasDefect, MapObjectHasDefectOperations entityManager) {

    }

    /**
     * Event dispatched after entity it is delete from database. Take into account that entity it
     * not more in database when this event being dispatch.
     *
     * @param mapObjectHasDefect
     * @param entityManager
     */
    @Override
    public void onAfterEntityDelete(MapObjectHasDefect mapObjectHasDefect, MapObjectHasDefectOperations entityManager) {
        List<MapObjectHasDefectHasImages> images = MapObjectHasDefectHasImagesOperations.getInstance().getImages(mapObjectHasDefect.getId());
        MapObjectHasDefectHasImagesOperations.getInstance().deleteAll(images);
    }
}
