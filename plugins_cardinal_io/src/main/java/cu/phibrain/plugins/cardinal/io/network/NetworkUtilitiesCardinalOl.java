package cu.phibrain.plugins.cardinal.io.network;

import android.util.Log;

import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import cu.phibrain.plugins.cardinal.io.exceptions.DownloadError;
import cu.phibrain.plugins.cardinal.io.exceptions.ServerError;
import cu.phibrain.plugins.cardinal.io.network.api.APIError;
import cu.phibrain.plugins.cardinal.io.network.api.ApiClient;
import cu.phibrain.plugins.cardinal.io.network.api.AuthToken;
import cu.phibrain.plugins.cardinal.io.network.api.Envolve;
import cu.phibrain.plugins.cardinal.io.network.api.ErrorUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author Erodis Pérez Michel  (eperezm1986@gmail.com)
 */
public class NetworkUtilitiesCardinalOl {
    private static final String TAG = "NETWORKUTILITIESCARDINAL";

    /**
     * Send authentication data via HTTP POST using Django style
     *
     * @param server   the base url to which to send to.
     * @param user     the user or <code>null</code>.
     * @param password the password or <code>null</code>.
     * @return AuthToken   the logged token
     * @throws Exception if something goes wrong.
     */
    public static AuthToken sendGetAuthToken(String server, String user, String password) throws Exception {
        Response<AuthToken> response = ApiClient.getApiService(server).postAuthToken(new LoginModel(user, password)).execute();
        if (response.isSuccessful()) {
            return response.body();
        }
        throw new ServerError(response.message(), response.code());
    }

    public static void sendGetAuthToken(String server, String user, String password, @NotNull OnLoginCallback loginCallback) {

        ApiClient.getApiService(server).postAuthToken(new LoginModel(user, password)).enqueue(new Callback<AuthToken>() {
            /**
             * Invoked for a received HTTP response.
             *
             * <p>Note: An HTTP response may still indicate an application-level failure such as a 404 or 500.
             * Call {@link Response#isSuccessful()} to determine if the response indicates success.
             *
             * @param call
             * @param response
             */
            @Override
            public void onResponse(Call<AuthToken> call, Response<AuthToken> response) {
                if (response.isSuccessful()) {
                    // use response data and do some fancy stuff :)
                    loginCallback.onLoginSuccess(response.body());
                } else {
                    // parse the response body …
                    APIError error = ErrorUtils.parseError(response);

                    loginCallback.onLoginFailure(error);

                    // … or just log the issue like we’re doing :)
                    Log.d("APIError", String.valueOf(error));

                }
            }

            /**
             * Invoked when a network exception occurred talking to the server or when an unexpected exception
             * occurred creating the request or processing the response.
             *
             * @param call
             * @param t
             */
            @Override
            public void onFailure(Call<AuthToken> call, Throwable t) {
                APIError error = new APIError(500, t.getLocalizedMessage());
                loginCallback.onLoginFailure(error);
                Log.d("APIError", String.valueOf(error));
            }
        });
    }


    /**
     * Send via HTTP GET a request to obtain project data list
     *
     * @param server  the base url to which to send to.
     * @param token   the auth token login credential
     * @param filters the filters to apply to query project
     * @return List<WebDataProjectModel>   List of remote projects to import
     * @throws Exception if something goes wrong.
     */

    public static List<WebDataProjectModel> sendGetProjectDataList(String server, AuthToken token, Map<String, String> filters) throws Exception {
        Response<Envolve<List<WebDataProjectModel>>> response = ApiClient.getApiService(server).getProjectList(token.toString(), filters).execute();

        if (response.isSuccessful()) {
            return response.body().getResults();
        }
        throw new ServerError(response.message(), response.code());

    }


    /**
     * Send via HTTP GET a request to obtain a project data
     *
     * @param server the base url to which to send to.
     * @param token  the auth token login credential
     * @param id     the selected project ID
     * @return Project   A current remote selected projects to import
     * @throws Exception if something goes wrong.
     */

    public static Project sendGetProjectData(String server, AuthToken token, int id) throws Exception {
        try {
            Response<Project> response = ApiClient.getApiService(server).getProject(token.toString(), id).execute();

            if (response.isSuccessful()) {
                return response.body();
            }
            throw new ServerError(response.message(), response.code());
        } catch (IOException e) {

            // handle error
            throw new DownloadError(e.getMessage(), e);
        }

    }


    /**
     * Send via HTTP GET a request to obtain a project data
     *
     * @param server  the base url to which to send to.
     * @param token   the auth token login credential
     * @param filters the filters to apply
     * @return List<Supplier>    A current remote selected suppliers to import
     * @throws Exception if something goes wrong.
     */

    public static List<Supplier> sendGetSuppliers(String server, AuthToken token, Map<String, String> filters) throws Exception {
        Response<Envolve<List<Supplier>>> response = ApiClient.getApiService(server).getSupplierList(token.toString(), filters).execute();

        if (response.isSuccessful()) {
            return response.body().getResults();
        }
        throw new ServerError(response.message(), response.code());

    }


    /**
     * Send via HTTP GET a request to obtain a project data
     *
     * @param server  the base url to which to send to.
     * @param token   the auth token login credential
     * @param filters the filters to apply
     * @return List<LabelMaterial>  A current remote selected LabelMaterials to import
     * @throws Exception if something goes wrong.
     */

    public static List<LabelMaterial> sendGetLabelMaterials(String server, AuthToken token, Map<String, String> filters) throws Exception {
        Response<Envolve<List<LabelMaterial>>> response = ApiClient.getApiService(server).getLabelMaterialList(token.toString(), filters).execute();

        if (response.isSuccessful()) {
            return response.body().getResults();
        }
        throw new ServerError(response.message(), response.code());

    }


    public static boolean sendPut2WorkSession(String server, AuthToken token, WorkSession session) {
        try {
            Response<WorkSession> response = ApiClient.getApiService(server).updateWorkSession(token.toString(), session.getId(), session).execute();
            if (response.isSuccessful()) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return true;
        }

        return false;
    }

    public static boolean sendPut2MapObject(String server, AuthToken token, MapObject mapObject) {
        try {
            Response<MapObject> response = ApiClient.getApiService(server).updateMapObject(token.toString(), mapObject.getRemoteId(), mapObject).execute();
            if (response.isSuccessful()) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return true;
        }

        return false;
    }

    public static MapObject sendPostMapObject(String server, AuthToken token, MapObject mapObject) {
        try {
            Response<MapObject> response = ApiClient.getApiService(server).createMapObject(token.toString(), mapObject).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }

        return null;
    }


    public static boolean sendPut2SignalEvents(String server, AuthToken token, SignalEvents events) {
        try {
            Response<SignalEvents> response = ApiClient.getApiService(server).updateSignalEvents(token.toString(), events.getRemoteId(), events).execute();
            if (response.isSuccessful()) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return true;
        }

        return false;
    }

    public static SignalEvents sendPostSignalEvents(String server, AuthToken token, SignalEvents events) {
        try {
            Response<SignalEvents> response = ApiClient.getApiService(server).createSignalEvents(token.toString(), events).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean sendPut2WorkerRoute(String server, AuthToken token, WorkerRoute route) {
        try {

            Response<WorkerRoute> response = ApiClient.getApiService(server).updateWorkerRoute(token.toString(), route.getId(), route).execute();
            if (response.isSuccessful()) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }


    public static boolean sendPostWorkerRoute(String server, AuthToken token, WorkerRoute route) {
        try {

            Response<WorkerRoute> response = ApiClient.getApiService(server).createWorkerRoute(token.toString(), route).execute();
            if (response.isSuccessful()) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }


    public static boolean sendPut2RouteSegment(String server, AuthToken token, RouteSegment route) {
        try {

            Response<RouteSegment> response = ApiClient.getApiService(server).updateRouteSegment(token.toString(), route.getRemoteId(), route).execute();
            if (response.isSuccessful()) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    public static RouteSegment sendPostRouteSegment(String server, AuthToken token, RouteSegment route) {
        try {

            Response<RouteSegment> response = ApiClient.getApiService(server).createRouteSegment(token.toString(), route).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean sendPut2MapObjectHasState(String server, AuthToken token, MapObjectHasState state) {
        try {

            Response<MapObjectHasState> response = ApiClient.getApiService(server).updateMapObjectHasState(token.toString(), state.getRemoteId(), state).execute();
            if (response.isSuccessful()) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    public static MapObjectHasState sendPostMapObjectHasState(String server, AuthToken token, MapObjectHasState state) {
        try {

            Response<MapObjectHasState> response = ApiClient.getApiService(server).createMapObjectHasState(token.toString(), state).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean sendPut2MapObjectImages(String server, AuthToken token, MapObjectImages objectImages) {
        try {

            Response<MapObjectImages> response = ApiClient.getApiService(server).updateMapObjectImages(token.toString(), objectImages.getRemoteId(), objectImages).execute();
            if (response.isSuccessful()) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    public static MapObjectImages sendPostMapObjectImages(String server, AuthToken token, MapObjectImages objectImages) {
        try {

            Response<MapObjectImages> response = ApiClient.getApiService(server).createMapObjectImages(token.toString(), objectImages).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean sendPut2MapObjectMetadata(String server, AuthToken token, MapObjectMetadata objectMetadata) {
        try {

            Response<MapObjectMetadata> response = ApiClient.getApiService(server).updateMapObjectMetadata(token.toString(), objectMetadata.getRemoteId(), objectMetadata).execute();
            if (response.isSuccessful()) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    public static MapObjectMetadata sendPostMapObjectMetadata(String server, AuthToken token, MapObjectMetadata objectMetadata) {
        try {

            Response<MapObjectMetadata> response = ApiClient.getApiService(server).createMapObjectMetadata(token.toString(), objectMetadata).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean sendPut2MapObjectHasDefect(String server, AuthToken token, MapObjectHasDefect defect) {
        try {

            Response<MapObjectHasDefect> response = ApiClient.getApiService(server).updateMapObjectHasDefect(token.toString(), defect.getRemoteId(), defect).execute();
            if (response.isSuccessful()) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }

    public static MapObjectHasDefect sendPostMapObjectHasDefect(String server, AuthToken token, MapObjectHasDefect defect) {
        try {

            Response<MapObjectHasDefect> response = ApiClient.getApiService(server).createMapObjectHasDefect(token.toString(), defect).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static MapObjectHasDefectHasImages sendPostMapObjectHasDefectHasImages(String server, AuthToken token, MapObjectHasDefectHasImages images) {
        try {

            Response<MapObjectHasDefectHasImages> response = ApiClient.getApiService(server).createMapObjectHasDefectHasImages(token.toString(), images).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean sendPut2MapObjectHasDefectHasImages(String server, AuthToken token, MapObjectHasDefectHasImages images) {
        try {

            Response<MapObjectHasDefectHasImages> response = ApiClient.getApiService(server).updateMapObjectHasDefectHasImages(token.toString(), images.getRemoteId(), images).execute();
            if (response.isSuccessful()) {
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return true;
        }
        return false;
    }
}
