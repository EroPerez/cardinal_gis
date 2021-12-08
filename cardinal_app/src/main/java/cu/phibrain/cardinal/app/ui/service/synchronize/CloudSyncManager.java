package cu.phibrain.cardinal.app.ui.service.synchronize;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

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
        SYNC_FREQUENCY = 60;  // 1 minute (in seconds)
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
        if (account != null) {
            if (SYNC_FREQUENCY == 0) {
                final AccountManager accountManager = (AccountManager) getContext()
                        .getSystemService(Context.ACCOUNT_SERVICE);

                if (!accountManager.addAccountExplicitly(account, null, null)) {
                    account = accountManager.getAccountsByType(type)[0];
                }
                ContentResolver.cancelSync(account, authority);
            } else {
                this.beginPeriodicSync();
            }
        }
    }

    @SuppressWarnings("MissingPermission")
    public void beginPeriodicSync() {
        Log.d(TAG, "beginPeriodicSync() called with: updateConfigInterval = [" +
                SYNC_FREQUENCY + "]");

        final AccountManager accountManager = (AccountManager) getContext()
                .getSystemService(Context.ACCOUNT_SERVICE);

        if (!accountManager.addAccountExplicitly(account, null, null)) {
            account = accountManager.getAccountsByType(type)[0];
        }

        Log.d(TAG, "beginPeriodicSync() accountl = [" +
                account.name + "]");

        setAccountSyncable();

        ContentResolver.addPeriodicSync(account, authority, Bundle.EMPTY, SYNC_FREQUENCY);


        ContentResolver.setSyncAutomatically(account, authority, true);

    }

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
}
