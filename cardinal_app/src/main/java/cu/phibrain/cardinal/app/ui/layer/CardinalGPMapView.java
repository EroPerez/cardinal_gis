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

    public Layer getLayer(Class<? extends IGpLayer> layerClass, Long id){
        for (Layer layer : map().layers()) {
            if (layer.getClass().isAssignableFrom(layerClass) && ((ICardinalLayer) layer).getLayerId().equals(id)) {
                return layer;
            }
        }
        return null;
    }

    public Layer getLayer(Class<? extends IGpLayer> layerClass){
        for (Layer layer : map().layers()) {
            if (layer.getClass().isAssignableFrom(layerClass)) {
                return layer;
            }
        }
        return null;
    }


    public void reloadLayer( Long id) throws Exception {
        for (Layer layer : map().layers()) {
            if (layer.getClass().isAssignableFrom(CardinalPointLayer.class) && ((ICardinalLayer) layer).getLayerId().equals(id)) {
                ((CardinalPointLayer) layer).reloadData();
                break;
            }
        }
    }

}
