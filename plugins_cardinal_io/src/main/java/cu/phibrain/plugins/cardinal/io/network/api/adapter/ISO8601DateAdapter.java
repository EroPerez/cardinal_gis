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
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Date;

public class ISO8601DateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
    private String datePattern;

    public ISO8601DateAdapter() {
        datePattern = "yyyy-MM-dd'T'HH:mm:ssZ";
    }

    @Override
    public Date deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        if (!(json instanceof JsonPrimitive)) {
            throw new JsonParseException("The date should be a string value");
        }

       // Log.d("PickoCar", "adapter_raw_date: " + json.getAsString());

        Date date = deserializeToDate(json);
        if (type == Date.class) {
            return date;
        } else if (type == Timestamp.class) {
            return new Timestamp(date.getTime());
        } else if (type == java.sql.Date.class) {
            return new java.sql.Date(date.getTime());
        } else {
            throw new IllegalArgumentException(getClass() + " cannot deserialize to " + type);
        }
    }

    private Date deserializeToDate(JsonElement json) {
        try {
            return ISO8601DateParser.parse(json.getAsString());
        } catch (ParseException e) {
            throw new JsonSyntaxException(json.getAsString(), e);
        }
    }

    @Override
    public JsonElement serialize(Date date, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(ISO8601DateParser.format(date, datePattern));
    }
}
