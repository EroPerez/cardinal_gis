package cu.phibrain.plugins.cardinal.io.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;


/**
 * Created by Michel on 23/10/18.
 */

public class GsonHelper {

    /**
     * Method used to convert string into pojo (Model class object)
     *
     * @param <T>
     * @param response
     * @param pojo
     * @return
     */
    public static <T> T createPojoFromString(String response, Type pojo) {
        GsonBuilder gsonBuilder = new GsonBuilder();

        return gsonBuilder.create().fromJson(response, pojo);
    }

    /**
     * Method to convert pojo (Model object) into json string
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> String createJSONStringFromPojo(T data) {
        Gson gson = new Gson();
        return gson.toJson(data);

    }
}
