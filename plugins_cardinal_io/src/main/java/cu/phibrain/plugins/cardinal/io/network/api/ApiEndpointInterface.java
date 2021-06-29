package cu.phibrain.plugins.cardinal.io.network.api;

import java.util.List;
import java.util.Map;

import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelMaterial;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LoginModel;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Project;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Supplier;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WebDataProjectModel;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkSession;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by Michel on 12/9/2020.
 */

public interface ApiEndpointInterface {

    //Autentications with user an password package in LoginModel
    @Headers({
            "Accept: application/json"
    })
    @POST("api-auth/login/")
    Call<AuthToken> postAuthToken(@Body LoginModel data);

    // Bearer token-string-here
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("projects/flat_list/")
    Call<Envolve<List<WebDataProjectModel>>> getProjectList(@Header("Authorization") String authorization, @QueryMap Map<String, String> filters);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("projects/export/{id}/")
    Call<Project> getProject(@Header("Authorization") String authorization, @Path("id") int id);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("supplier/")
    Call<Envolve<List<Supplier>>> getSupplierList(@Header("Authorization") String authorization, @QueryMap Map<String, String> filters);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("label-material/")
    Call<Envolve<List<LabelMaterial>>> getLabelMaterialList(@Header("Authorization") String authorization, @QueryMap Map<String, String> filters);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @GET("work-session/")
    Call<Envolve<List<WorkSession>>> getWorkSessionlList(@Header("Authorization") String authorization, @QueryMap Map<String, String> filters);

}
