package cu.phibrain.plugins.cardinal.io.network;

import android.content.Context;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cu.phibrain.plugins.cardinal.io.exceptions.ServerError;
import cu.phibrain.plugins.cardinal.io.model.LoginModel;
import cu.phibrain.plugins.cardinal.io.model.WebDataProjectModel;
import cu.phibrain.plugins.cardinal.io.network.api.ApiClient;
import cu.phibrain.plugins.cardinal.io.network.api.AuthToken;
import cu.phibrain.plugins.cardinal.io.network.api.Envolve;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.network.NetworkUtilities;
import retrofit2.Response;

/**
 * @author Erodis Pérez Michel  (eperezm1986@gmail.com)
 */
public class NetworkUtilitiesCardinalOl {
    private static final String TAG = "NETWORKUTILITIESCARDINAL";

    public static final long maxBufferSize = 4096;
    public static final String SLASH = "/";

    private static String normalizeUrl(String url) {
        return normalizeUrl(url, false);
    }

    private static String normalizeUrl(String url, boolean addSlash) {
        if ((!url.startsWith("http://")) && (!url.startsWith("https://"))) {
            url = "http://" + url;
        }
        if (addSlash && !url.endsWith(SLASH)) {
            url = url + SLASH;
        }
        return url;
    }

    private static HttpURLConnection makeNewConnection(String fileUrl) throws Exception {
        URL url = new URL(normalizeUrl(fileUrl));
        return (HttpURLConnection) url.openConnection();
    }

    private static void setCsrfHeader(CookieManager session, HttpURLConnection connection) throws IOException {
        String csrfToken = null;
        for (HttpCookie c : session.getCookieStore().getCookies()) {
            if (c.getName().equals("csrftoken") && c.getDomain().equals(connection.getURL().getHost())) {
                csrfToken = c.getValue();
            }
        }
        if (csrfToken == null) {
            String message = "The session is not correctly authenticated.";
            IOException ioException = new IOException(message);
            GPLog.error(TAG, message, ioException);
            throw ioException;
        }
        connection.setRequestProperty("X-CSRFToken", csrfToken);
    }

    private static String encodeFormData(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }

    public static CookieManager getAuthenticatedSession(String loginUrl, String user, String password) throws IOException {
        HttpURLConnection conn = null;
        CookieManager manager;
        if (CookieHandler.getDefault() != null && (CookieHandler.getDefault() instanceof CookieManager)) {
            manager = (CookieManager) CookieHandler.getDefault();
        } else {
            //manager = new CookieManager();
            //CookieHandler previousDefault = CookieHandler.getDefault();
            manager = new CookieManager(null, CookiePolicy.ACCEPT_ALL);
            //CookieManager manager = new CookieManager(null, CookiePolicy.ACCEPT_ORIGINAL_SERVER);
            CookieHandler.setDefault(manager);
        }
        try {
            if (user != null && password != null && user.trim().length() > 0 && password.trim().length() > 0) {
                conn = makeNewConnection(loginUrl);
                conn.setRequestMethod("POST");
                conn.connect();
                if (conn.getResponseCode() != 200) {
                    String message = "Authentication failed. Check loginUrl. Response code: " + conn.getResponseCode() + ". Response message: " + conn.getResponseMessage();
                    IOException ioException = new IOException(message);
                    GPLog.error(TAG, message, ioException);
                    throw ioException;
                }
                String csrfToken = null;
                URL domainUrl = new URL(loginUrl);
                CookieStore store = manager.getCookieStore();
                for (HttpCookie c : store.getCookies()) {
                    if (c.getName().equals("csrftoken") && c.getDomain().equals(domainUrl.getHost())) {
                        csrfToken = c.getValue();
                    } else if (c.getName().equals("sessionid") && c.getDomain().equals(domainUrl.getHost())) {
                        store.remove(domainUrl.toURI(), c);
                    }
                }
                if (csrfToken == null) {
                    String message = "Authentication failed.";
                    IOException ioException = new IOException(message);
                    GPLog.error(TAG, message, ioException);
                    throw ioException;
                }

                conn = makeNewConnection(loginUrl);
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");

                HashMap<String, String> auth = new HashMap<String, String>();
                auth.put("username", user);
                auth.put("password", password);
                auth.put("csrfmiddlewaretoken", csrfToken);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(encodeFormData(auth));
                writer.flush();
                writer.close();
                os.close();
                conn.connect();
                if (conn.getResponseCode() != 200) {
                    String message = "Authentication failed. Response code: " + conn.getResponseCode() + ". Response message: " + conn.getResponseMessage();
                    IOException ioException = new IOException(message);
                    GPLog.error(TAG, message, ioException);
                    throw ioException;
                }
            }
        } catch (IOException ex) {
            throw ex;
        } catch (Exception e) {
            String message = "Authentication failed.";
            IOException ioException = new IOException(message, e);
            GPLog.error(TAG, message, ioException);
            throw ioException;
        }
        return manager;
    }

    /**
     * Send a file via HTTP POST using Django style authentication
     *
     * @param context    the context to use.
     * @param urlStr     the url to which to send to.
     * @param string     the string to send as post body.
     * @param user       the user or <code>null</code>.
     * @param password   the password or <code>null</code>.
     * @param outputFile the file to save to.
     * @param loginUrl   the login URL
     * @throws Exception if something goes wrong.
     */
    public static void sendPostForFile(Context context,
                                       String urlStr,
                                       String string,
                                       String user,
                                       String password,
                                       File outputFile,
                                       String loginUrl) throws Exception {

        loginUrl = normalizeUrl(loginUrl, true);
        CookieManager manager = getAuthenticatedSession(loginUrl, user, password);

        BufferedOutputStream wr = null;
        HttpURLConnection conn = null;
        try {
            urlStr = normalizeUrl(urlStr, true);
            conn = makeNewConnection(urlStr);
            setCsrfHeader(manager, conn);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // conn.setChunkedStreamingMode(0);
            conn.setUseCaches(false);
            if (user != null && password != null && user.trim().length() > 0 && password.trim().length() > 0) {
                conn.setRequestProperty("Authorization", NetworkUtilities.getB64Auth(user, password));
            }
            conn.connect();

            // Make server believe we are form data...
            wr = new BufferedOutputStream(conn.getOutputStream());
            byte[] bytes = string.getBytes();
            wr.write(bytes);
            wr.flush();

            int responseCode = conn.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = null;
                FileOutputStream out = null;
                long bytesCount = 0;
                try {
                    in = conn.getInputStream();
                    out = new FileOutputStream(outputFile);

                    byte[] buffer = new byte[(int) maxBufferSize];
                    int bytesRead = in.read(buffer, 0, (int) maxBufferSize);
                    while (bytesRead > 0) {
                        out.write(buffer, 0, bytesRead);
                        bytesRead = in.read(buffer, 0, (int) maxBufferSize);
                        bytesCount += bytesRead;
                    }
                    out.flush();
                } finally {
                    if (in != null)
                        in.close();
                    if (out != null)
                        out.close();
                }
                if (bytesCount == 0) {
                    throw new RuntimeException("Error downloading the data. Buffer was empty.");
                }
            } else {
                throw new RuntimeException("Error downloading the data. Got error code: " + responseCode);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (conn != null)
                conn.disconnect();
        }
    }

    /**
     * Send a GET request, previously authenticating on the provided loginUrl.
     * Necessary to properly authenticate with CSRF protected frameworks such
     * as Django.
     *
     * @param urlStr            the url.
     * @param requestParameters request parameters or <code>null</code>.
     * @param user              user or <code>null</code>.
     * @param password          password or <code>null</code>.
     * @param loginUrl          The URL used to authenticate
     * @return the fetched text.
     * @throws Exception if something goes wrong.
     */
    public static String sendGetRequest(String urlStr,
                                        String requestParameters,
                                        String user,
                                        String password,
                                        String loginUrl) throws Exception {

        loginUrl = normalizeUrl(loginUrl, true);
        CookieManager manager = getAuthenticatedSession(loginUrl, user, password);
        HttpURLConnection conn = null;
        BufferedReader in = null;
        try {
            conn = makeNewConnection(urlStr);
            conn.connect();

            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            if (in != null)
                in.close();
            if (conn != null)
                conn.disconnect();
        }
    }

    /**
     * Send a file via HTTP POST using Django style authentication
     *
     * @param context  the context to use.
     * @param urlStr   the server url to POST to.
     * @param file     the file to send.
     * @param user     the user or <code>null</code>.
     * @param password the password or <code>null</code>.
     * @param loginUrl the login URL
     * @return the return string from the POST.
     * @throws Exception if something goes wrong.
     */
    public static String sendFilePost(Context context,
                                      String urlStr,
                                      File file,
                                      String user,
                                      String password,
                                      String loginUrl) throws Exception {
        loginUrl = normalizeUrl(loginUrl, true);
        CookieManager manager = getAuthenticatedSession(loginUrl, user, password);
        BufferedOutputStream wr = null;
        FileInputStream fis = null;
        HttpURLConnection conn = null;
        InputStream connResponseStream = null;
        try {
            long fileSize = file.length();
            fis = new FileInputStream(file);
            urlStr = normalizeUrl(urlStr, true);
            urlStr = urlStr + "?name=" + file.getName();
            conn = makeNewConnection(urlStr);
            conn.setRequestMethod("POST");
            setCsrfHeader(manager, conn);
            conn.setDoOutput(true);
            conn.setDoInput(true);

            conn.setRequestProperty("Content-Type", "application/octet-stream");
            conn.setRequestProperty("Content-Length", "" + fileSize);
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                conn.setFixedLengthStreamingMode(fileSize);
            } else {
                conn.setFixedLengthStreamingMode((int) fileSize);
            }
            conn.connect();

            wr = new BufferedOutputStream(conn.getOutputStream());
            long bufferSize = Math.min(fileSize, maxBufferSize);

            if (GPLog.LOG)
                GPLog.addLogEntry(TAG, "BUFFER USED: " + bufferSize);
            byte[] buffer = new byte[(int) bufferSize];
            int bytesRead = fis.read(buffer, 0, (int) bufferSize);
            long totalBytesWritten = 0;
            while (bytesRead > 0) {
                wr.write(buffer, 0, (int) bufferSize);
                totalBytesWritten = totalBytesWritten + bufferSize;
                if (totalBytesWritten >= fileSize)
                    break;

                bufferSize = Math.min(fileSize - totalBytesWritten, maxBufferSize);
                bytesRead = fis.read(buffer, 0, (int) bufferSize);
            }
            wr.flush();

            int responseCode = conn.getResponseCode();
            if (responseCode < 400) {
                connResponseStream = conn.getInputStream();
            } else {
                connResponseStream = conn.getErrorStream();
            }
            StringBuilder responseBuilder = new StringBuilder();
            if (connResponseStream != null) {
                Reader reader = null;
                try {
                    int charCount = 0;
                    int maxChars = 1048576; // Avoid downloading response bodies bigger than 1 MB
                    reader = new BufferedReader(new InputStreamReader(connResponseStream, "UTF-8"));
                    int c = 0;
                    while ((c = reader.read()) != -1 && charCount < maxChars) {
                        responseBuilder.append((char) c);
                        charCount++;
                    }
                } finally {
                    if (reader != null) {
                        reader.close();
                    }
                }
            }
            if (responseCode != 200 && responseCode != 204) {
                if (GPLog.LOG)
                    GPLog.addLogEntry(TAG, "Error uploading data. Code " + responseCode);
                throw new ServerError(responseBuilder.toString(), responseCode);
            }
            return responseBuilder.toString();
        } finally {
            if (wr != null)
                wr.close();
            if (fis != null)
                fis.close();
            if (connResponseStream != null) {
                connResponseStream.close();
            }
            if (conn != null)
                conn.disconnect();
        }
    }


    /**
     * Send authentication data via HTTP POST using Django style
     *
     * @param server   the base url to which to send to.
     * @param user     the user or <code>null</code>.
     * @param password the password or <code>null</code>.
     * @return AuthToken   the logged token
     * @throws Exception if something goes wrong.
     */
    public static AuthToken sendGetAuthToken(String server, String user, String passwd) throws Exception {
        Response<AuthToken> response = ApiClient.getApiService(server).postAuthToken(new LoginModel(user, passwd)).execute();
        if (response.isSuccessful()) {
            return response.body();
        }
        throw new ServerError(response.message(), response.code());
    }

    /**
     * Send via HTTP GET a request to obtain project data list
     *
     * @param server the base url to which to send to.
     * @param token  the auth token login credential
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


}
