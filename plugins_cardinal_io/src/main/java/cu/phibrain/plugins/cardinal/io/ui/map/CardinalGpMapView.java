package cu.phibrain.plugins.cardinal.io.ui.map;

import android.content.Context;
import android.preference.PreferenceManager;

import org.oscim.backend.canvas.Color;
import org.oscim.renderer.MapRenderer;

import cu.phibrain.plugins.cardinal.io.ui.layer.CardinalLayerManager;
import eu.geopaparazzi.map.GPMapPosition;
import eu.geopaparazzi.map.GPMapView;
import eu.geopaparazzi.map.layers.LayerManager;

public class CardinalGpMapView extends GPMapView {

    public CardinalGpMapView(Context context) {
        super(context);
        CardinalLayerManager.INSTANCE.createGroups(this);
    }
}
