package cu.phibrain.plugins.cardinal.io.exceptions;

/**
 * Created by Michel on 8/09/20.
 */

public class DownloadError extends Exception {
    public DownloadError(String message) {
        super(message);
    }

    public DownloadError(String message, Throwable cause) {
        super(message, cause);
    }

    public DownloadError(Throwable cause) {
        super(cause);
    }
}
