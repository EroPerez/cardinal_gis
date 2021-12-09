package cu.phibrain.cardinal.app.ui.service.synchronize;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.concurrent.TimeUnit;

import cu.phibrain.cardinal.app.R;

public class CloudSyncManager {
    private static CloudSyncManager mInstance;

    private static final String TAG = CloudSyncManager.class.getSimpleName();
    private long SYNC_FREQUENCY;
    private final String authority;
    private final String type;

    private Account account;
    private Context context;

    public CloudSyncManager(final Context context) {
        this.context = context;
        account = getAccount();
        SYNC_FREQUENCY = TimeUnit.HOURS.toSeconds(1);  // 1 hour (in seconds)
        type = CloudAccount.ACCOUNT_TYPE;
        authority = this.getContext().getString(R.string.content_authority);
    }

    public static CloudSyncManager init(Context context) {
        if (mInstance == null) mInstance = new CloudSyncManager(context);
        return mInstance;
    }

    public static CloudSyncManager getInstance() {
        if (mInstance == null)
            throw new RuntimeException("call CloudSyncManager.init first!");
        return mInstance;
    }

    // Use this method to get an instance of Account.
    @Nullable
    private Account getAccount() {

        AccountManager accountManager = AccountManager.get(this.getContext());
        Account[] accounts = accountManager.getAccountsByType(this.type);
        if (accounts.length > 0) {
            return accounts[0];
        }
        return null;
    }

    public void setSyncFrecuency(long frecuency) {
        this.SYNC_FREQUENCY = frecuency;
        Log.d(TAG, "Authority: " + authority);
        account = getAccount();
        if (account != null) {
            if (SYNC_FREQUENCY == 0) {
                final AccountManager accountManager = (AccountManager) getContext()
                        .getSystemService(Context.ACCOUNT_SERVICE);

                if (!accountManager.addAccountExplicitly(account, null, null)) {
                    account = accountManager.getAccountsByType(type)[0];
                }
                ContentResolver.cancelSync(account, authority);
            } else {
                this.beginPeriodicSync(account);
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    private void beginPeriodicSync(Account account) {
        Log.d(TAG, "beginPeriodicSync() called with: updateConfigInterval = [" +
                SYNC_FREQUENCY + "]");

//        final AccountManager accountManager = (AccountManager) getContext()
//                .getSystemService(Context.ACCOUNT_SERVICE);
//
//        if (!accountManager.addAccountExplicitly(account, null, null)) {
//            account = accountManager.getAccountsByType(type)[0];
//        }

        Log.d(TAG, "beginPeriodicSync() accountl = [" + account.name + "]");

        // Inform the system that this account supports sync
        setAccountSyncable();
        // Inform the system that this account is eligible for auto sync when the network is up
        ContentResolver.setSyncAutomatically(account, authority, true);
        /*
         * Recommend a schedule for automatic synchronization. The system may modify this based
         * on other scheduled syncs and network utilization.
         */
        ContentResolver.addPeriodicSync(
                account,
                authority,
                Bundle.EMPTY,
                SYNC_FREQUENCY
        );

    }

    /**
     * Helper method to trigger an immediate sync ("refresh").
     * <p>
     * <p>This should only be used when we need to preempt the normal sync schedule. Typically, this
     * means the user has pressed the "refresh" button.
     * <p>
     * Note that SYNC_EXTRAS_MANUAL will cause an immediate sync, without any optimization to
     * preserve battery life. If you know new data is available (perhaps via a GCM notification),
     * but the user is not actively waiting for that data, you should omit this flag; this will give
     * the OS additional freedom in scheduling your sync request.
     */
    public void syncImmediately() {
        Log.d(TAG, "syncImmediately() called.");
        Bundle settingsBundle = new Bundle();
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        settingsBundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);

        ContentResolver.requestSync(account, authority, settingsBundle);
    }

    private void setAccountSyncable() {
        if (ContentResolver.getIsSyncable(account, authority) == 0) {
            ContentResolver.setIsSyncable(account, authority, 1);
        }
    }

    public Context getContext() {
        return context;
    }

    public boolean addAccount(String user, String passwd, String authToken) {
        //Create account if needed
        // Create the account type and default account
        Account newAccount = new Account(user, CloudAccount.ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager = AccountManager.get(this.context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            for (Account oldAccount : accountManager.getAccounts()) {
                if (!newAccount.equals(oldAccount)) {
                    accountManager.removeAccountExplicitly(oldAccount);
                }

            }
        }
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, passwd, null)) {
            // Ojo: hay que setear el token explicitamente si la cuenta no existe,
            // no basta con mandarlo al authenticator
            accountManager.setAuthToken(newAccount, CloudAccount.ACCOUNT_TYPE, authToken);
            return true;
        } else accountManager.setPassword(newAccount, passwd);

        return false;
    }
}
