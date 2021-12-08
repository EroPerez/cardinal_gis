package cu.phibrain.cardinal.app.ui.service.synchronize;

import cu.phibrain.plugins.cardinal.io.database.entity.model.LoginModel;
import cu.phibrain.plugins.cardinal.io.network.NetworkUtilitiesCardinalOl;
import cu.phibrain.plugins.cardinal.io.network.api.AuthToken;
import eu.geopaparazzi.library.database.GPLog;

public class CloudAuthToken implements IAuthenticatorServer {

    private final String TAG = CloudAuthToken.class.getSimpleName();

    private LoginModel accountInfo = null;
    private String server;

    public CloudAuthToken(String server) {
        this.server = server;
    }

    @Override
    public String getAuthToken(String userName, String password, String tokenType) {
        accountInfo = new LoginModel(userName, password);

        try {

            AuthToken token = NetworkUtilitiesCardinalOl.login(this.server, userName, password);

            if (token != null) {

                accountInfo.setToken(token.getKey());

                if (tokenType.equalsIgnoreCase(CloudAccount.AUTHTOKEN_TYPE)) {
                    return token.toString();
                }

            }
        } catch (Exception e) {
            GPLog.error(this, null, e);
            e.printStackTrace();
        }

        return null;
    }
}
