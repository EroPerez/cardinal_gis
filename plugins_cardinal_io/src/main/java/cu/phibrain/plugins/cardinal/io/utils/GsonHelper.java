package cu.phibrain.plugins.cardinal.io.utils;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Created by Michel on 23/10/18.
 */

public class GsonHelper {

    /**
     * Method used to convert string into pojo (Model class object)
     * @param response
     * @param pojo
     * @param <T>
     * @return
     */
    public static <T> T createPojoFromString(String response, Class<T> pojo) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        return gsonBuilder.create().fromJson(response, pojo);
    }

    /**
     * Method to convert pojo (Model obect) into json string
     * @param context
     * @param data
     * @param <T>
     * @return
     */
    public static <T>String createJSONStringFromPojo(Context context, T data) {
        Gson gson = new Gson();
        return gson.toJson(data);

    }
}
