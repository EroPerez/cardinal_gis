package cu.phibrain.plugins.cardinal.io.database.entity.events;

import android.util.Log;

import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelSubLot;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefect;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectMetadata;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectTypeAttribute;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Stock;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.LabelSubLotOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectHasDefectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectHasStateOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectImagesOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectMetadataOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.RouteSegmentOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.StockOperations;


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
            Log.d("MOAEntityI", "label: " + labelSubLot.toString());
        }
        // update stock state
        Stock stock = StockOperations.getInstance().load(mapObject.getStockCodeId());
        if (stock != null) {
            stock.setLocated(true);
            StockOperations.getInstance().update(stock);
            Log.d("MOAEntityI", "Stock: " + stock.getCode());
        }

        //Create all extra attribute
        MapObjecType mapObjectObjectType = mapObject.getObjectType();
        while (mapObjectObjectType != null) {
            for (MapObjectTypeAttribute attr :
                    mapObjectObjectType.getAttributes()) {

                String value = ""+attr.getDefaultValue();
                if (value.isEmpty()) value = " ";

                MapObjectMetadataOperations.getInstance().insert(new MapObjectMetadata(mapObject.getId(), value, attr.getId()));

            }

            mapObjectObjectType = mapObjectObjectType.getParentObj();
        }
    }

    @Override
    public void onBeforeEntityUpdate(MapObject mapObject, MapObjectOperations entityManager) {
        entityManager.detach(mapObject);

        MapObject oldMapObject = entityManager.load(mapObject.getId());
        Log.d("MOBEntityI", "mapObject: " + mapObject.toString());
        Log.d("MOBEntityI", "oldMapObject: " + oldMapObject.toString());

        if (oldMapObject.getCode() != mapObject.getCode()) {
            //update the state of old label assigned
            LabelSubLot oldlabelSubLot = LabelSubLotOperations.getInstance().load(
                    oldMapObject.getSessionId(),
                    oldMapObject.getCode()
            );

            if (oldlabelSubLot != null) {
                oldlabelSubLot.setGeolocated(false);
                LabelSubLotOperations.getInstance().update(oldlabelSubLot);
                Log.d("MOBEntityI", "label: " + oldlabelSubLot.toString());
            }

            //update the state of new label assigned
            LabelSubLot labelSubLot = LabelSubLotOperations.getInstance().load(
                    mapObject.getSessionId(),
                    mapObject.getCode()
            );

            if (labelSubLot != null) {
                labelSubLot.setGeolocated(true);
                LabelSubLotOperations.getInstance().update(labelSubLot);
                Log.d("MOBEntityI", "label: " + labelSubLot.toString());
            }

        }
        if (oldMapObject.getStockCodeId() != mapObject.getStockCodeId()) {

            //update the state of old stock assigned
            Stock oldStock = StockOperations.getInstance().load(oldMapObject.getStockCodeId());
            if (oldStock != null) {
                oldStock.setLocated(false);
                StockOperations.getInstance().update(oldStock);
                Log.d("MOAEntityI", "oldStock: " + oldStock.toString());
            }
            //update the state of new stock assigned
            Stock stock = StockOperations.getInstance().load(mapObject.getStockCodeId());
            if (stock != null) {
                stock.setLocated(true);
                StockOperations.getInstance().update(stock);
                Log.d("MOAEntityI", "Stock: " + stock.toString());
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
        mapObject.update();

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
            LabelSubLotOperations.getInstance().update();
        }

        //Release stock code to be relocated again
        Stock stock = StockOperations.getInstance().load(mapObject.getStockCodeId());
        // Stock stock = mapObject.getStockCode();
        if (stock != null)
        {
            stock.setLocated(false);
            StockOperations.getInstance().update(stock);
        }

        //Delete all related metadata
        MapObjectMetadataOperations.getInstance().deleteAll(mapObject.getMetadata());
        //Delete all related states
        MapObjectHasStateOperations.getInstance().deleteAll(mapObject.getStates());
        //Delete all related images
        MapObjectImagesOperations.getInstance().deleteAll(mapObject.getImages());
        // Delete all related route segments
        RouteSegmentOperations.getInstance().deleteAll(entityManager.getRouteSegments(mapObject.getId()));

        //Delete all related defects and images of defects
        for (MapObjectHasDefect defect :
                mapObject.getDefects()) {
            MapObjectHasDefectOperations.getInstance().delete(defect);
        }


    }
}
