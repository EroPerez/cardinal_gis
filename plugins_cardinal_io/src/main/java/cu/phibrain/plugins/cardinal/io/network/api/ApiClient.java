package cu.phibrain.plugins.cardinal.io.network.api;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * The class holding login data.
 *
 * @author Erodis Pérez Michel  (eperezm1986@gmail.com)
 */
public class ApiClient {

    private static ApiEndpointInterface API_SERVICE;

    public static ApiEndpointInterface getApiService(String baseUrl) {

        // Creamos un interceptor y le indicamos el log level a usar
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        // Asociamos el interceptor a las peticiones
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(logging);

//        Gson gson = new GsonBuilder()
//                .registerTypeAdapter(Date.class, new ISO8601DateAdapter())
//                // .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//                .create();

       if (API_SERVICE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(httpClient.build()) // <-- usamos el log level
                    .build();
            API_SERVICE = retrofit.create(ApiEndpointInterface.class);
        }

        return API_SERVICE;
    }

}
