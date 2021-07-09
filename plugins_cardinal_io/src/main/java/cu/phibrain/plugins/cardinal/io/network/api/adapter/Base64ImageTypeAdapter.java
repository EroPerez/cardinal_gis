package cu.phibrain.plugins.cardinal.io.network.api.adapter;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Type;

import cu.phibrain.plugins.cardinal.io.utils.ImageUtil;
import eu.geopaparazzi.library.images.ImageUtilities;

public class Base64ImageTypeAdapter extends TypeAdapter<byte[]> {
    private Gson gson = new Gson();

    /**
     * Writes one JSON value (an array, object, string, number, boolean or null)
     * for {@code value}.
     *
     * @param out
     * @param value the Java object to write. May be null.
     */
    @Override
    public void write(JsonWriter out, byte[] value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }

        Type type = new TypeToken<String>() {
        }.getType();

        String base64 = ImageUtil.convertToBase64(ImageUtilities.getImageFromImageData(value), "jpg");

        gson.toJson(base64, type, out);
    }

    /**
     * Reads one JSON value (an array, object, string, number, boolean or null)
     * and converts it to a Java object. Returns the converted object.
     *
     * @param in
     * @return the converted Java object. May be null.
     */
    @Override
    public byte[] read(JsonReader in) throws IOException {
        if (in.peek() == JsonToken.NULL) {
            in.nextNull();
            return null;
        }

        in.peek();
        String recordInBase64 = in.nextString();

        return ImageUtil.convertToBytesFromBase64(recordInBase64);
    }
}
