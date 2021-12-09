package cu.phibrain.cardinal.app.ui.service.synchronize;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import cu.phibrain.plugins.cardinal.io.database.entity.model.LoginModel;
import eu.geopaparazzi.library.database.GPLog;

public class CloudAccountAuthenticator {
    private final String TAG = CloudAccountAuthenticator.class.getSimpleName();

    private static CloudAccountAuthenticator ourInstance = null;

    private Activity ctxActivity;
    public AccountManager mAccountManager;
    private String mToken = null;
    private AccountAuthenticatorListener listener;

    public void setListener(AccountAuthenticatorListener listener) {
        this.listener = listener;
    }


    public static CloudAccountAuthenticator init(Activity context) {

        if (ourInstance == null)
            ourInstance = new CloudAccountAuthenticator(context);

        return ourInstance;
    }

    public static CloudAccountAuthenticator getInstance() {
        if (ourInstance == null)
            throw new RuntimeException("call CloudAccountAuthenticator.init first!");

        return ourInstance;
    }

    private CloudAccountAuthenticator(Activity context) {
        ctxActivity = context;
        mAccountManager = AccountManager.get(ctxActivity);
        if (context instanceof AccountAuthenticatorListener)
            listener = (AccountAuthenticatorListener) context;
        else
            listener = null;
    }

    /**
     * Get an auth token for the account.
     * If not exist - add it and then return its auth token.
     * If one exist - return its auth token.
     * If more than one exists - show a picker and return the select account's auth token.
     *
     * @param accountType
     * @param authTokenType
     */
    public void getTokenForAccountCreateIfNeeded(String accountType, String authTokenType) {

        final AccountManagerFuture<Bundle> future = mAccountManager.getAuthTokenByFeatures(accountType, authTokenType, null, ctxActivity, null, null,
                new AccountManagerCallback<Bundle>() {
                    @Override
                    public void run(AccountManagerFuture<Bundle> future) {
                        Bundle bnd = null;
                        try {

                            bnd = future.getResult();
                            mToken = bnd.getString(AccountManager.KEY_AUTHTOKEN);
                            Log.d(TAG, "GetTokenForAccount Bundle is " + bnd);

                            if (listener != null) {
                                LoginModel accountInfo = new LoginModel(
                                        bnd.getString(AccountManager.KEY_ACCOUNT_NAME),
                                        bnd.getString(AccountManager.KEY_PASSWORD)
                                );
                                accountInfo.setToken(mToken);
                                listener.OnAuthenticated(accountInfo);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            GPLog.error(this, null, e);
                        }
                    }
                }
                , null);

    }

    /**
     * Invalidates the auth token for the account
     *
     * @param accountType
     * @param authTokenType
     */
    public void invalidateAuthToken(final String accountType, final String authTokenType) {
        final Account account = mAccountManager.getAccountsByType(accountType)[0];

        ctxActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // Get token
                String authtoken = mAccountManager.peekAuthToken(account, authTokenType);

                // invalidate the token since it may have expired.
                mAccountManager.invalidateAuthToken(account.type, authtoken);
                mAccountManager.setPassword(account, null);

                if (listener != null) {
                    LoginModel accountInfo = new LoginModel();
                    accountInfo.setUsername(account.name);
                    accountInfo.setToken(authtoken);

                    listener.OnLoggedOut(accountInfo);
                }

            }
        });

    }


    public String getToken() {
        return mToken;
    }

    public interface AccountAuthenticatorListener {
        void OnAuthenticated(LoginModel account);

        void OnLoggedOut(LoginModel account);
    }

}
