package cu.phibrain.cardinal.app.ui.layer;

import org.oscim.core.MapPosition;

import eu.geopaparazzi.map.GPMapPosition;

public class CardinalGPMapPosition extends GPMapPosition {

    MapPosition mapPosition;

    public CardinalGPMapPosition(MapPosition mapPosition) {
        super(mapPosition);
        this.mapPosition = mapPosition;
    }

    public MapPosition getPosition(){return this.mapPosition;}
}
