package cu.phibrain.plugins.cardinal.io.network.api.adapter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import java.lang.reflect.Type;
import java.sql.Time;
import java.text.ParseException;
import java.util.Date;

public class ISO8601TimeAdapter implements JsonSerializer<Time>, JsonDeserializer<Time> {
    private String datePattern;

    public ISO8601TimeAdapter() {
        datePattern = "yyyy-MM-dd'T'HH:mm:ssTDZ";
    }

    @Override
    public Time deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (!(json instanceof JsonPrimitive)) {
            throw new JsonParseException("The date should be a string value");
        }
        Date date = deserializeToDate(json);
        if (type == Time.class) {
            return new Time(date.getTime());
        }
        throw new IllegalArgumentException(getClass() + " cannot deserialize to " + type);
    }

    private Date deserializeToDate(JsonElement json) {
        try {
            return ISO8601DateParser.parse(json.getAsString());
        } catch (ParseException e) {
            throw new JsonSyntaxException(json.getAsString(), e);
        }
    }

    @Override
    public JsonElement serialize(Time time, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(ISO8601DateParser.format(time, datePattern));
    }
}
