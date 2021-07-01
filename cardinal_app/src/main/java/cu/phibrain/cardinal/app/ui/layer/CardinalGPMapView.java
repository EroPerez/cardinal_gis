package cu.phibrain.cardinal.app.ui.layer;

import android.content.Context;

import org.oscim.layers.Layer;

import eu.geopaparazzi.map.GPMapView;
import eu.geopaparazzi.map.layers.interfaces.IGpLayer;

public class CardinalGPMapView extends GPMapView {
    public CardinalGPMapView(Context context) {
        super(context);
        CardinalLayerManager.INSTANCE.createGroups(this);
    }

//    public void reloadLayer(Class<? extends IGpLayer> layerClass, Long id) throws Exception {
//        for (Layer layer : map().layers()) {
//            if (layer.getClass().isAssignableFrom(layerClass) && ((CardinalLayer) layer).getID().equals(id)) {
//                ((CardinalLayer) layer).reloadData(id);
//                break;
//            }
//        }
//    }

}
