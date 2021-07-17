package cu.phibrain.plugins.cardinal.io.database.entity.events;


import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.RouteSegment;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.RouteSegmentOperations;

public class RouteSegmentEntityEventListener implements EntityEventListener<RouteSegment, RouteSegmentOperations>{
    @Override
    public void onBeforeEntityInsert(RouteSegment routeSegment, RouteSegmentOperations entityManager) {

    }

    @Override
    public void onAfterEntityInsert(RouteSegment routeSegment, RouteSegmentOperations entityManager) {

        MapObject origin = MapObjectOperations.getInstance().load(routeSegment.getOriginId());

        if(origin.getNodeGrade() == origin.getRouteSegments().size()) {
            origin.setIsCompleted(true);
        } else{
            origin.setIsCompleted(false);
        }
        MapObjectOperations.getInstance().update(origin);
    }

    @Override
    public void onBeforeEntityUpdate(RouteSegment routeSegment, RouteSegmentOperations entityManager) {

    }

    @Override
    public void onAfterEntityUpdate(RouteSegment routeSegment, RouteSegmentOperations entityManager) {

    }

    @Override
    public void onBeforeEntityDelete(RouteSegment routeSegment, RouteSegmentOperations entityManager) {

    }

    @Override
    public void onAfterEntityDelete(RouteSegment routeSegment, RouteSegmentOperations entityManager) {
        MapObject origin =  MapObjectOperations.getInstance().load(routeSegment.getOriginId());
        origin.setIsCompleted(false);
        MapObjectOperations.getInstance().update(origin);
    }
}
