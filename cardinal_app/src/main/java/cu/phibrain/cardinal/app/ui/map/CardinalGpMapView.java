package cu.phibrain.cardinal.app.ui.map;

import android.content.Context;

import cu.phibrain.cardinal.app.ui.layer.CardinalLayerManager;
import eu.geopaparazzi.map.GPMapView;

public class CardinalGpMapView extends GPMapView {

    public CardinalGpMapView(Context context) {
        super(context);
        CardinalLayerManager.INSTANCE.createGroups(this);
    }
}
