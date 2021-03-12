package cu.phibrain.plugins.cardinal.io;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.base.DaoSessionManager;
import cu.phibrain.plugins.cardinal.io.database.entity.ContractOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.GroupOfLayerOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.LayerOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.MapObjecTypeDefectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.MapObjecTypeOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.MapObjecTypeStateOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.MapObjectTypeAttributeOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.ProjectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.StockOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.WorkerOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.ZoneOperations;
import cu.phibrain.plugins.cardinal.io.exceptions.DownloadError;
import cu.phibrain.plugins.cardinal.io.model.GroupOfLayer;
import cu.phibrain.plugins.cardinal.io.model.Layer;
import cu.phibrain.plugins.cardinal.io.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.model.Project;
import cu.phibrain.plugins.cardinal.io.model.Stock;
import cu.phibrain.plugins.cardinal.io.model.WebDataProjectModel;
import cu.phibrain.plugins.cardinal.io.model.Worker;
import cu.phibrain.plugins.cardinal.io.model.Zone;
import cu.phibrain.plugins.cardinal.io.network.NetworkUtilitiesCardinalOl;
import cu.phibrain.plugins.cardinal.io.network.api.AuthToken;
import eu.geopaparazzi.core.database.DaoMetadata;
import eu.geopaparazzi.library.R;
import eu.geopaparazzi.library.core.ResourcesManager;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.database.TableDescriptions;
import eu.geopaparazzi.library.util.Utilities;


/**
 * Singleton to handle cloud up- and download.
 *
 * @author Erodis Pérez Michel  (eperezm1986@gmail.com)
 */
@SuppressWarnings("nls")
public enum WebDataProjectManager {
    /**
     * Singleton instance.
     */
    INSTANCE;

    /**
     * The relative path appended to the server url to compose the get layers info url.
     */
    public static String GET_PROJECTS_INFO = "projects/flat_list/";

    /**
     * The relative path appended to the server url to compose the download data url.
     */
    public static String DOWNLOAD_PROJECT_DATA = "projects/";

    public static String UPLOAD_PROJECT_DATA = "projects/";

    /**
     * Relative URL endpoint to upload data and continue edition, maintaining the server lock on the layers
     */
//    public static String UPLOAD_AND_CONTINUE_DATA = "sync/commit/";

    public static String LOGIN_URL = "api-auth/login/";


    /**
     * Uploads a project folder as zip to the given server via POST.
     *
     * @param context the {@link Context} to use.
     * @param fileToUpload  the file to upload.
     * @param server  the server to which to upload.
     * @param user    the username for authentication.
     * @param passwd  the password for authentication.
     * @return the return message.
     */
//    public String uploadData(Context context, File fileToUpload, String server, String user, String passwd) throws SyncError {
//        return uploadData(context, fileToUpload, server, user, passwd, UPLOAD_DATA);
//    }

    /**
     * Uploads a project folder as zip to the given server via POST.
     *
     * @param context      the {@link Context} to use.
     * @param fileToUpload the file to upload.
     * @param server       the server to which to upload.
     * @param user         the username for authentication.
     * @param passwd       the password for authentication.
     * @param action       {@link #UPLOAD_PROJECT_DATA} or {@link #UPLOAD_AND_CONTINUE_DATA}
     * @return the return message.
     */
//    public String uploadData(Context context, File fileToUpload, String server, String user, String passwd, String action) throws SyncError {
//        try {
//            String loginUrl = addActionPath(server, LOGIN_URL);
//            if (UPLOAD_AND_CONTINUE_DATA.equals(action)) {
//                server = addActionPath(server, UPLOAD_AND_CONTINUE_DATA);
//            }
//            else {
//                server = addActionPath(server, UPLOAD_DATA);
//            }
//            String result = NetworkUtilitiesCardinalOl.sendFilePost(context, server, fileToUpload, user, passwd, loginUrl);
//            if (GPLog.LOG) {
//                GPLog.addLogEntry(this, result);
//            }
//            return result;
//        }
//        catch (ServerError e) {
//            GPLog.error(this, null, e);
//            throw e;
//        }
//        catch (Exception e) {
//            GPLog.error(this, null, e);
//            throw new SyncError(e);
//        }
//    }
    private String addActionPath(String server, String path) {
        if (server.endsWith("/")) {
            return server + path;
        } else {
            return server + "/" + path;
        }
    }

    /**
     * Downloads a project from the given server via GET.
     *
     * @param context    the {@link Context} to use.
     * @param server     the server from which to download.
     * @param user       the username for authentication.
     * @param passwd     the password for authentication.
     * @param webproject the project to download.
     * @return The path to the downloaded file
     */
    public String downloadProject(Context context, String server, String user, String passwd, WebDataProjectModel webproject, String outputFileName) throws DownloadError {
        try {
            //File outputDir = ResourcesManager.getInstance(context).getApplicationSupporterDir();
            File outputDir = ResourcesManager.getInstance(context).getMainStorageDir();
            File downloadedProjectFile = new File(outputDir, outputFileName);
            String newDbFileName = downloadedProjectFile.getPath();
            Log.i("WebDataProjectManager", "New project path: " + newDbFileName);
            if (downloadedProjectFile.exists()) {
                String wontOverwrite = context.getString(R.string.the_file_exists_wont_overwrite) + " " + downloadedProjectFile.getName();
                throw new DownloadError(wontOverwrite);
            }

            server = addActionPath(server, "");
            //First login into cardinal cloud service
            AuthToken token = NetworkUtilitiesCardinalOl.sendGetAuthToken(server, user, passwd);
            Project project = NetworkUtilitiesCardinalOl.sendGetProjectData(server, token, webproject.id);

            List<Worker> workers = project.getWorkers();
            List<Stock> stocks = project.getStocks();
            List<Zone> zones = project.getZone();
            List<GroupOfLayer> groupOfLayers = project.getGroupoflayers();

            DaoSessionManager.getInstance().setContext(context);
            DaoSessionManager.getInstance().setDatabaseName(newDbFileName);
            DaoSessionManager.getInstance().resetDaoSession();

            ProjectOperations.getInstance().insertProject(project);

            // update project metadata
            SQLiteDatabase db = DaoSessionManager.getInstance().getSQLiteDatabase();

            // create table
            DaoMetadata.createTables(db);
            String uniqueDeviceId = Utilities.getUniqueDeviceId(context);
            DaoMetadata.initProjectMetadata(db, project.getName(), project.getDescription(), null, user, uniqueDeviceId);
            DaoMetadata.setValue(db, TableDescriptions.MetadataTableDefaultValues.KEY_CREATIONTS.getFieldName(), String.valueOf(project.getCreatedAt().getTime()));
            DaoMetadata.insertNewItem(db, TableDescriptions.MetadataTableDefaultValues.PROJECT_ID.getFieldName(),
                    TableDescriptions.MetadataTableDefaultValues.PROJECT_ID.getFieldLabel(), String.valueOf(project.getId()));

            WorkerOperations.getInstance().insertWorkerList(workers);
            ContractOperations.getInstance().insertContractList(project.getId(), workers, true);
            StockOperations.getInstance().insertStockList(stocks);
            ZoneOperations.getInstance().insertZoneList(zones);
            GroupOfLayerOperations.getInstance().insertGroupList(groupOfLayers);

            // Save all layers in groups
            List<Layer> layerList = new ArrayList<>();
            for (GroupOfLayer group :
                    groupOfLayers) {
                layerList.addAll(group.getLayers());
            }
            LayerOperations.getInstance().insertLayerList(layerList);

            // Get and save all mapobjectypes in layer
            List<MapObjecType> objecTypeList = new ArrayList<>();
            for (Layer layer :
                    layerList) {
                objecTypeList.addAll(layer.getMapobjectypes());
            }

            MapObjecTypeOperations.getInstance().insertMapObjecTypesList(objecTypeList);

            // Get and save all extra mapobject type properties
            for (MapObjecType mapObjecType :
                    objecTypeList) {
                MapObjectTypeAttributeOperations.getInstance().insertMapObjectTypeAttributeList(mapObjecType.getAttributes());
                MapObjecTypeStateOperations.getInstance().insertMapObjecTypeStateList(mapObjecType.getStates());
                MapObjecTypeDefectOperations.getInstance().insertMapObjecTypeDefectList(mapObjecType.getDefects());
            }


            long fileLength = downloadedProjectFile.length();
            if (fileLength == 0) {
                throw new DownloadError("Error downloading project from cloud server.");
            }

            return downloadedProjectFile.getCanonicalPath();
        } catch (DownloadError e) {
            GPLog.error(this, null, e);
            throw e;
        } catch (Exception e) {
            GPLog.error(this, null, e);
            throw new DownloadError(e);
        }
    }

    /**
     * Downloads the data projects list from the given server via GET.
     *
     * @param context the {@link Context} to use.
     * @param server  the server from which to download.
     * @param user    the username for authentication.
     * @param passwd  the password for authentication.
     * @return the project list.
     * @throws Exception if something goes wrong.
     */
    public List<WebDataProjectModel> downloadDataProjectsList(Context context, String server, String user, String passwd) throws Exception {

        server = addActionPath(server, "");

        //First login into cardinal cloud service
        AuthToken token = NetworkUtilitiesCardinalOl.sendGetAuthToken(server, user, passwd);
        //And then get project list
        List<WebDataProjectModel> webDataList = NetworkUtilitiesCardinalOl.sendGetProjectDataList(server, token, new HashMap<String, String>());
        return webDataList;
    }

}
