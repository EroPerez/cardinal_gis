package cu.phibrain.plugins.cardinal.io.network;

import cu.phibrain.plugins.cardinal.io.network.api.APIError;
import cu.phibrain.plugins.cardinal.io.network.api.AuthToken;

public interface OnLoginCallback {
    void onLoginSuccess(AuthToken authToken);
    void onLoginFailure(APIError error);
}
