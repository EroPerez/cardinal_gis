package cu.phibrain.plugins.cardinal.io.database.entity.events;

import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelSubLot;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefect;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectMetadata;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectTypeAttribute;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.LabelSubLotOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectHasDefectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectHasStateOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectImagesOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectMetadataOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.RouteSegmentOperations;


public class MapObjectEntityEventListener implements EntityEventListener<MapObject, MapObjectOperations> {

    @Override
    public void onBeforeEntityInsert(MapObject mapObject, MapObjectOperations entityManager) {

    }

    @Override
    public void onAfterEntityInsert(MapObject mapObject, MapObjectOperations entityManager) {
        //update labels state
        LabelSubLot labelSubLot = LabelSubLotOperations.getInstance().load(
                mapObject.getSessionId(),
                mapObject.getCode()
        );

        if (labelSubLot != null) {
            labelSubLot.setGeolocated(true);
            LabelSubLotOperations.getInstance().update(labelSubLot);
        }


        //Create all extra attribute
        MapObjecType mapObjectObjectType = mapObject.getObjectType();
        while (mapObjectObjectType != null) {
            for (MapObjectTypeAttribute attr :
                    mapObjectObjectType.getAttributes()) {

                String value = attr.getDefaultValue();
                if (value.isEmpty()) value = " ";

                MapObjectMetadataOperations.getInstance().insert(new MapObjectMetadata(mapObject.getId(), value, attr.getId()));

            }

            mapObjectObjectType = mapObjectObjectType.getParentObj();
        }
    }

    @Override
    public void onBeforeEntityUpdate(MapObject mapObject, MapObjectOperations entityManager) {

        MapObject oldMapObject = entityManager.load(mapObject.getId());

        if (oldMapObject.getCode() != mapObject.getCode()) {
            //update the state of old label assigned
            LabelSubLot oldlabelSubLot = LabelSubLotOperations.getInstance().load(
                    oldMapObject.getSessionId(),
                    oldMapObject.getCode()
            );

            if (oldlabelSubLot != null) {
                oldlabelSubLot.setGeolocated(false);
                LabelSubLotOperations.getInstance().update(oldlabelSubLot);
            }

            //update the state of new label assigned
            LabelSubLot labelSubLot = LabelSubLotOperations.getInstance().load(
                    mapObject.getSessionId(),
                    mapObject.getCode()
            );

            if (labelSubLot != null) {
                labelSubLot.setGeolocated(true);
                LabelSubLotOperations.getInstance().update(labelSubLot);
            }
        }
    }

    @Override
    public void onAfterEntityUpdate(MapObject mapObject, MapObjectOperations entityManager) {

        if (mapObject.getNodeGrade() == mapObject.getRouteSegments().size()) {
            mapObject.setIsCompleted(true);
        } else {
            mapObject.setIsCompleted(false);
        }
        entityManager.update(mapObject);

    }

    @Override
    public void onBeforeEntityDelete(MapObject mapObject, MapObjectOperations entityManager) {

    }

    @Override
    public void onAfterEntityDelete(MapObject mapObject, MapObjectOperations entityManager) {


        // Release the label code to be re - utilized in other map objects
        LabelSubLot labelSubLot = LabelSubLotOperations.getInstance().load(
                mapObject.getSessionId(),
                mapObject.getCode()
        );

        if (labelSubLot != null) {
            labelSubLot.setGeolocated(false);
            LabelSubLotOperations.getInstance().update(labelSubLot);
        }

        //Delete all related metadata
        MapObjectMetadataOperations.getInstance().deleteAll(mapObject.getMetadata());
        //Delete all related states
        MapObjectHasStateOperations.getInstance().deleteAll(mapObject.getStates());
        //Delete all related images
        MapObjectImagesOperations.getInstance().deleteAll(mapObject.getImages());
        // Delete all related route segments
        RouteSegmentOperations.getInstance().deleteAll(mapObject.getRouteSegments());

        //Delete all related defects and images of defects
        for (MapObjectHasDefect defect :
                mapObject.getDefects()) {
            MapObjectHasDefectOperations.getInstance().delete(defect);
        }


    }
}
