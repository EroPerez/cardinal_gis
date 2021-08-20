package cu.phibrain.cardinal.app.ui.layer.layerobjects;

import org.oscim.core.GeoPoint;
import org.oscim.layers.vector.geometries.CircleDrawable;
import org.oscim.layers.vector.geometries.Style;

import eu.geopaparazzi.map.layers.layerobjects.IGPDrawable;

public class GPCardinalCircleDrawable extends CircleDrawable implements IGPDrawable {
    private long id;

    public GPCardinalCircleDrawable(GeoPoint center, double radiusKm, long id) {
        super(center, radiusKm);
        this.id = id;
    }

    public GPCardinalCircleDrawable(GeoPoint center, double radiusKm, Style style, long id) {
        super(center, radiusKm, style);
        this.id = id;
    }

    public GPCardinalCircleDrawable(GeoPoint center, double radiusKm, int quadrantSegments, Style style, long id) {
        super(center, radiusKm, quadrantSegments, style);
        this.id = id;
    }


    @Override
    public long getId() {
        return id;
    }
}
