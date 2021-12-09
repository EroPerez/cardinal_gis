package cu.phibrain.cardinal.app.ui.service.synchronize;

import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import eu.geopaparazzi.core.utilities.Constants;

public class CloudAuthenticator extends AbstractAccountAuthenticator {

    private final String TAG = CloudAuthenticator.class.getSimpleName();
    private final Context mContext;
    private IAuthenticatorServer mAuthenticatorServer;
    private SharedPreferences preferences;

    public CloudAuthenticator(Context context) {
        super(context);

        this.mContext = context;
        preferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
        final String serverUrl = preferences.getString(Constants.PREF_KEY_SERVER, "");
        mAuthenticatorServer = new CloudAuthToken(serverUrl);

        Log.d(TAG, "serverUrl: " + serverUrl);
    }

    @Override
    public Bundle editProperties(AccountAuthenticatorResponse response, String accountType) {
        return null;
    }

    @Override
    public Bundle addAccount(AccountAuthenticatorResponse response, String accountType,
                             String authTokenType, String[] requiredFeatures, Bundle options)
            throws NetworkErrorException {

        Log.d(TAG, "add Account");
        final Bundle bundle = new Bundle();
        return bundle;
    }

    @Override
    public Bundle confirmCredentials(AccountAuthenticatorResponse response, Account account,
                                     Bundle options) throws NetworkErrorException {
        if (options != null && options.containsKey(AccountManager.KEY_PASSWORD)) {
            final String password = options.getString(AccountManager.KEY_PASSWORD);
            final String verified = mAuthenticatorServer.getAuthToken(account.name, password,
                    CloudAccount.AUTHTOKEN_TYPE);
            final Bundle result = new Bundle();
            boolean confirmed = verified != null;
            result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, confirmed);
            return result;
        }

        final Bundle bundle = new Bundle();
        return bundle;
    }

    @Override
    public Bundle getAuthToken(AccountAuthenticatorResponse response, Account account,
                               String authTokenType, Bundle options) throws NetworkErrorException {

        Log.d(TAG, "getAuthToken: " + account.name);

        if (!authTokenType.equals(CloudAccount.AUTHTOKEN_TYPE)) {
            final Bundle bundle = new Bundle();
            bundle.putString(AccountManager.KEY_ERROR_MESSAGE, "invalid authTokenType");
            return bundle;
        }

        final AccountManager am = AccountManager.get(mContext);
        String authToken = am.peekAuthToken(account, authTokenType);

        if (TextUtils.isEmpty(authToken)) {
            final String password = am.getPassword(account);
            if (password != null) {
                authToken = mAuthenticatorServer.getAuthToken(account.name, password,
                        authTokenType);
            }
        }

        if (!TextUtils.isEmpty(authToken)) {
            final Bundle result = new Bundle();
            result.putString(AccountManager.KEY_ACCOUNT_NAME, account.name);
            result.putString(AccountManager.KEY_ACCOUNT_TYPE, account.type);
            result.putString(AccountManager.KEY_AUTHTOKEN, authToken);

            return result;
        }


        final Bundle bundle = new Bundle();
        return bundle;
    }

    @Override
    public String getAuthTokenLabel(String authTokenType) {
        if (CloudAccount.AUTHTOKEN_TYPE.equals(authTokenType))
            return CloudAccount.AUTHTOKEN_TYPE;

        return authTokenType + " (Label)";
    }

    @Override
    public Bundle updateCredentials(AccountAuthenticatorResponse response, Account account,
                                    String authTokenType, Bundle options) throws NetworkErrorException {

        final Bundle bundle = new Bundle();

        return bundle;
    }

    @Override
    public Bundle hasFeatures(AccountAuthenticatorResponse response, Account account,
                              String[] features) throws NetworkErrorException {
        final Bundle result = new Bundle();
        result.putBoolean(AccountManager.KEY_BOOLEAN_RESULT, false);
        return result;
    }
}
