package cu.phibrain.cardinal.app.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import cu.phibrain.cardinal.app.ui.service.synchronize.CloudAuthenticator;

public class CloudAuthenticatorService extends Service {
    public CloudAuthenticatorService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new CloudAuthenticator(this).getIBinder();
    }
}
