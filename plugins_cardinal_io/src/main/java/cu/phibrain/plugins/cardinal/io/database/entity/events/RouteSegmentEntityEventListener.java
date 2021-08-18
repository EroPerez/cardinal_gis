package cu.phibrain.plugins.cardinal.io.database.entity.events;


import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.RouteSegment;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.RouteSegmentOperations;

public class RouteSegmentEntityEventListener implements EntityEventListener<RouteSegment, RouteSegmentOperations> {
    @Override
    public void onBeforeEntityInsert(RouteSegment routeSegment, RouteSegmentOperations entityManager) {

    }

    @Override
    public void onAfterEntityInsert(RouteSegment routeSegment, RouteSegmentOperations entityManager) {

        try {
            MapObject origin = routeSegment.getOriginObj();
            List<RouteSegment> routeSegmentsInOut = MapObjectOperations.getInstance().getRouteSegments(routeSegment.getOriginId());

            if (routeSegmentsInOut.size() >= origin.getNodeGrade()) {
                origin.setIsCompleted(true);
            } else {
                origin.setIsCompleted(false);
            }
            origin.update();


            MapObject destiny = routeSegment.getDestinyObj();

            List<RouteSegment> routeSegmentsOutIn = MapObjectOperations.getInstance().getRouteSegments(routeSegment.getDestinyId());

            if (routeSegmentsOutIn.size() >= destiny.getNodeGrade()) {
                destiny.setIsCompleted(true);
            } else {
                destiny.setIsCompleted(false);
            }
            destiny.update();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        MapObject origin = MapObjectOperations.getInstance().load(routeSegment.getOriginId());
        origin.setIsCompleted(false);
        MapObjectOperations.getInstance().update(origin);

        MapObject destiny = MapObjectOperations.getInstance().load(routeSegment.getDestinyId());
        destiny.setIsCompleted(false);
        MapObjectOperations.getInstance().update(destiny);
    }
}
