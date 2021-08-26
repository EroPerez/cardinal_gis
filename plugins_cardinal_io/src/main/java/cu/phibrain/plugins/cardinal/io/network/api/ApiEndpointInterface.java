package cu.phibrain.plugins.cardinal.io.network.api;

import java.util.List;
import java.util.Map;

import cu.phibrain.plugins.cardinal.io.database.entity.model.Devices;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelMaterial;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LoginModel;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefect;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefectHasImages;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasState;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectImages;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectMetadata;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Project;
import cu.phibrain.plugins.cardinal.io.database.entity.model.RouteSegment;
import cu.phibrain.plugins.cardinal.io.database.entity.model.SignalEvents;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Supplier;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WebDataProjectModel;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkSession;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkerRoute;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    //Upload method
    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @PUT("work-session/{id}/")
    Call<WorkSession> updateWorkSession(@Header("Authorization") String authorization, @Path("id") long id, @Body WorkSession session);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @PUT("map-object/{id}/")
    Call<MapObject> updateMapObject(@Header("Authorization") String authorization, @Path("id") long id, @Body MapObject mapObject);

    @Headers({
            "Accept: application/json"
    })
    @POST("map-object/")
    Call<MapObject> createMapObject(@Header("Authorization") String authorization, @Body MapObject mapObject);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @DELETE("map-object/{id}/")
    Call<Object> deleteMapObject(@Header("Authorization") String authorization, @Path("id") long id);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @PUT("signal-events/{id}/")
    Call<SignalEvents> updateSignalEvents(@Header("Authorization") String authorization, @Path("id") long id, @Body SignalEvents event);


    @POST("signal-events/")
    Call<SignalEvents> createSignalEvents(@Header("Authorization") String authorization, @Body SignalEvents event);


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @PUT("worker-route/{id}/")
    Call<WorkerRoute> updateWorkerRoute(@Header("Authorization") String authorization, @Path("id") long id, @Body WorkerRoute route);

    @Headers({
            "Accept: application/json"
    })
    @POST("worker-route/")
    Call<WorkerRoute> createWorkerRoute(@Header("Authorization") String authorization, @Body WorkerRoute route);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @PUT("route-segment/{id}/")
    Call<RouteSegment> updateRouteSegment(@Header("Authorization") String authorization, @Path("id") long id, @Body RouteSegment route);

    @Headers({
            "Accept: application/json"
    })
    @POST("route-segment/")
    Call<RouteSegment> createRouteSegment(@Header("Authorization") String authorization, @Body RouteSegment route);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @DELETE("route-segment/{id}/")
    Call<Object> deleteRouteSegment(@Header("Authorization") String authorization, @Path("id") Long id);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @PUT("state-vertex/{id}/")
    Call<MapObjectHasState> updateMapObjectHasState(@Header("Authorization") String authorization, @Path("id") long id, @Body MapObjectHasState state);

    @Headers({
            "Accept: application/json"
    })
    @POST("state-vertex/")
    Call<MapObjectHasState> createMapObjectHasState(@Header("Authorization") String authorization, @Body MapObjectHasState state);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @DELETE("state-vertex/{id}/")
    Call<Object> deleteMapObjectHasState(@Header("Authorization") String authorization, @Path("id") long id);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @PUT("map-object-image/{id}/")
    Call<MapObjectImages> updateMapObjectImages(@Header("Authorization") String authorization, @Path("id") long id, @Body MapObjectImages objectImages);

    @Headers({
            "Accept: application/json"
    })
    @POST("map-object-image/")
    Call<MapObjectImages> createMapObjectImages(@Header("Authorization") String authorization, @Body MapObjectImages objectImages);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @DELETE("map-object-image/{id}/")
    Call<Object> deleteMapObjectImages(@Header("Authorization") String authorization, @Path("id") long id);

    @Headers({
            "Accept: application/json"
    })
    @POST("map-object-metadata/")
    Call<MapObjectMetadata> createMapObjectMetadata(@Header("Authorization") String authorization, @Body MapObjectMetadata objectMetadata);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @PUT("map-object-metadata/{id}/")
    Call<MapObjectMetadata> updateMapObjectMetadata(@Header("Authorization") String authorization, @Path("id") Long id, @Body MapObjectMetadata objectMetadata);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @DELETE("map-object-metadata/{id}/")
    Call<Object> deleteMapObjectMetadata(@Header("Authorization") String authorization, @Path("id") Long id);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @PUT("defect-vertex/{id}/")
    Call<MapObjectHasDefect> updateMapObjectHasDefect(@Header("Authorization") String authorization, @Path("id") Long id, @Body MapObjectHasDefect defect);

    @Headers({
            "Accept: application/json"
    })
    @POST("defect-vertex/")
    Call<MapObjectHasDefect> createMapObjectHasDefect(@Header("Authorization") String authorization, @Body MapObjectHasDefect defect);


    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @DELETE("defect-vertex/{id}/")
    Call<Object> deleteMapObjectHasDefect(@Header("Authorization") String authorization, @Path("id") Long id);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @PUT("map-object-defect-image/{id}/")
    Call<MapObjectHasDefectHasImages> updateMapObjectHasDefectHasImages(@Header("Authorization") String authorization, @Path("id") Long id, @Body MapObjectHasDefectHasImages images);

    @Headers({
            "Accept: application/json"
    })
    @POST("map-object-defect-image/")
    Call<MapObjectHasDefectHasImages> createMapObjectHasDefectHasImages(@Header("Authorization") String authorization, @Body MapObjectHasDefectHasImages images);

    @Headers({
            "Accept: application/json",
            "Content-Type: application/json"
    })
    @DELETE("map-object-defect-image/{id}/")
    Call<Object> deleteMapObjectHasDefectHasImage(@Header("Authorization") String authorization, @Path("id") Long id);

    @Headers({
            "Accept: application/json"
    })
    @POST("devices/")
    Call<Devices> createWorkerDevices(@Header("Authorization") String authorization, @Body Devices device);


    @Headers({
            "Accept: application/json"
    })
    @PUT("devices/{id}/")
    Call<Devices> updateWorkerDevices(@Header("Authorization") String authorization, @Path("id") String id, @Body Devices device);
}
