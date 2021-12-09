package cu.phibrain.cardinal.app.ui.service.synchronize;

public interface IAuthenticatorServer {
    String getAuthToken(final String user, final String pass, String authType);
}
