package cu.phibrain.plugins.cardinal.io.network.api.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import eu.geopaparazzi.map.GPGeoPoint;

public class GPGeoPointTypeAdapter extends TypeAdapter<List<GPGeoPoint>> {
    private Gson gson = new Gson();

    @Override
    public void write(JsonWriter out, List<GPGeoPoint> gpGeoPoints) throws IOException {
        if (gpGeoPoints == null) {
            out.nullValue();
            return;
        }

        List<CardinalPoint> cardinalPoints = new ArrayList<>();
        Type listType = new TypeToken<List<CardinalPoint>>() {}.getType();

        for (GPGeoPoint geoPoint :
                gpGeoPoints) {
            cardinalPoints.add(CardinalPoint.buildFrom(geoPoint));
        }

        gson.toJson(cardinalPoints, listType, out);


    }

    @Override
    public List<GPGeoPoint> read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }
        String jsonCardinalPoints ="";
        while (in.hasNext()){
            JsonToken nextToken = in.peek();
            if(JsonToken.STRING.equals(nextToken)) {
                jsonCardinalPoints = in.nextString();
                break;
            }
        }
        Type listType = new TypeToken<List<CardinalPoint>>() {
        }.getType();

        List<CardinalPoint> cardinalPoints = gson.fromJson( jsonCardinalPoints, listType);
        List<GPGeoPoint> gpGeoPoints = new ArrayList<>();

        for (CardinalPoint point :
                cardinalPoints) {
            gpGeoPoints.add(point.toGPGeoPoint());
        }


        return gpGeoPoints;
    }

    private static class CardinalPoint {
        private double lat;
        private double lng;

        public CardinalPoint(double _lat, double _lng) {
            this.lat = _lat;
            this.lng = _lng;
        }

        public static CardinalPoint buildFrom(GPGeoPoint pt) {
            return new CardinalPoint(pt.getLatitude(), pt.getLongitude());
        }

        public GPGeoPoint toGPGeoPoint() {
            return new GPGeoPoint(this.lat, this.lng);
        }
    }
}
