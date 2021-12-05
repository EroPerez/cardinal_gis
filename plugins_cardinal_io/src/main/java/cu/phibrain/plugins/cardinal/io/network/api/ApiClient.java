package cu.phibrain.plugins.cardinal.io.network.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cu.phibrain.plugins.cardinal.io.network.api.adapter.Base64ImageTypeAdapter;
import cu.phibrain.plugins.cardinal.io.network.api.adapter.GPGeoPointTypeAdapter;
import cu.phibrain.plugins.cardinal.io.network.api.adapter.ISO8601DateAdapter;
import cu.phibrain.plugins.cardinal.io.network.api.interceptors.ErrorInterceptor;
import eu.geopaparazzi.map.GPGeoPoint;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * The class holding login data.
 *
 * @author Erodis Pérez Michel  (eperezm1986@gmail.com)
 */
public class ApiClient {

    private static ApiEndpointInterface API_SERVICE;

    private static Retrofit retrofit;

    public static ApiEndpointInterface getApiService(String baseUrl) {

        // Creamos un interceptor y le indicamos el log level a usar
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Asociamos el interceptor a las peticiones
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(240, TimeUnit.SECONDS)
                .readTimeout(240, TimeUnit.SECONDS)
                .writeTimeout(240, TimeUnit.SECONDS);
        httpClient.addInterceptor(logging)
               // .addInterceptor(new RequestGzipInterceptor())
               // .addInterceptor(new ResponseGzipInterceptor())
                .addInterceptor(new ErrorInterceptor());

        Type listType = new TypeToken<List<GPGeoPoint>>() {
        }.getType();

        Type byteArrayType = new TypeToken<byte[]>() {
        }.getType();

        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .serializeNulls()
                .registerTypeAdapter(listType, new GPGeoPointTypeAdapter())
                .registerTypeAdapter(byteArrayType, new Base64ImageTypeAdapter())
                .registerTypeAdapter(Date.class, new ISO8601DateAdapter())
                .create();

        if (API_SERVICE == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient.build()) // <-- usamos el log level
                    .build();
            API_SERVICE = retrofit.create(ApiEndpointInterface.class);
        }

        return API_SERVICE;
    }

    public static Retrofit retrofit() {
        return retrofit;
    }

}
