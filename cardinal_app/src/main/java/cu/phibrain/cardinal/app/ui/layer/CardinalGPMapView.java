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

//    public void reloadLayer(MapObjecType objecType) throws Exception {
//
//        switch (objecType.getGeomType())
//        {
//            case POLYLINE:
//                reloadLayer(CardinalLineLayer.class);
//                break;
//            case POLYGON:
//                reloadLayer(CardinalPolygonLayer.class);
//                break;
//            default:
//                reloadLayer(CardinalPointLayer.class);
//                break;
//        }
//    }

//    public void reloadLayer(Class<? extends IGpLayer> layerClass, Long id) throws Exception {
//        for (Layer layer : map().layers()) {
//            if (layer.getClass().isAssignableFrom(layerClass) && ((CardinalLayer) layer).getID().equals(id)) {
//                ((CardinalLayer) layer).reloadData(id);
//                break;
//            }
//        }
//    }

}
