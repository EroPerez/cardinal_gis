package cu.phibrain.plugins.cardinal.io.network.api;

public class APIError {

    private int status_code;
    private String message;

    public APIError() {
    }

    public int status() {
        return status_code;
    }

    public String message() {
        return message;
    }
}
