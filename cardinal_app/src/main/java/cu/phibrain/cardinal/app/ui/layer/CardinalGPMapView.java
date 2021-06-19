package cu.phibrain.cardinal.app.ui.layer;

import android.content.Context;

import org.oscim.layers.Layer;
import org.oscim.map.Layers;

import eu.geopaparazzi.map.GPMapView;
import eu.geopaparazzi.map.layers.LayerManager;
import eu.geopaparazzi.map.layers.interfaces.IGpLayer;

public class CardinalGPMapView extends GPMapView {
    public CardinalGPMapView(Context context) {
        super(context);
        CardinalLayerManager.INSTANCE.createGroups(this);
    }

    public void reloadLayer(Class<? extends IGpLayer> layerClass, Long id) throws Exception {
        for (Layer layer : map().layers()) {
            if (layer.getClass().isAssignableFrom(layerClass) && ((MapObjectLayer) layer).getID().equals(id)) {
                ((MapObjectLayer) layer).reloadData(id);
                break;
            }
        }
    }

}
