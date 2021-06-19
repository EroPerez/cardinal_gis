package cu.phibrain.cardinal.app.ui.layer;

import android.content.Context;

import eu.geopaparazzi.map.GPMapView;
import eu.geopaparazzi.map.layers.LayerManager;

public class CardinalGPMapView extends GPMapView {
    public CardinalGPMapView(Context context) {
        super(context);
        CardinalLayerManager.INSTANCE.createGroups(this);
    }

}
