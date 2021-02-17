package cu.phibrain.plugins.cardinal.io;

import android.content.Context;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.exceptions.DownloadError;
import cu.phibrain.plugins.cardinal.io.model.WebDataProjectModel;
import cu.phibrain.plugins.cardinal.io.network.NetworkUtilitiesCardinalOl;
import cu.phibrain.plugins.cardinal.io.network.api.AuthToken;
import eu.geopaparazzi.library.R;
import eu.geopaparazzi.library.core.ResourcesManager;
import eu.geopaparazzi.library.database.GPLog;


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
     * @param context the {@link Context} to use.
     * @param server  the server from which to download.
     * @param user    the username for authentication.
     * @param passwd  the password for authentication.
     * @return The path to the downloaded file
     */
    public String downloadData(Context context, String server, String user, String passwd, String postJson, String outputFileName) throws DownloadError {
        String downloadedProjectFileName = "no information available";
        try {
            File outputDir = ResourcesManager.getInstance(context).getApplicationSupporterDir();
            File downloadeddataFile = new File(outputDir, outputFileName);
            if (downloadeddataFile.exists()) {
                String wontOverwrite = context.getString(R.string.the_file_exists_wont_overwrite) + " " + downloadeddataFile.getName();
                throw new DownloadError(wontOverwrite);
            }
            String loginUrl = addActionPath(server, LOGIN_URL);
            server = addActionPath(server, DOWNLOAD_PROJECT_DATA);
            NetworkUtilitiesCardinalOl.sendPostForFile(context, server, postJson, user, passwd, downloadeddataFile, loginUrl);

            long fileLength = downloadeddataFile.length();
            if (fileLength == 0) {
                throw new DownloadError("Error in downloading file.");
            }

            return downloadeddataFile.getCanonicalPath();
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
