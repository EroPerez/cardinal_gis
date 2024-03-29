package cu.phibrain.cardinal.app.ui.activities;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.cardinal.app.R;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.cardinal.app.ui.adapter.WorkSessionAdapter;
import cu.phibrain.cardinal.app.ui.service.synchronize.CloudAccount;
import cu.phibrain.cardinal.app.ui.service.synchronize.CloudAccountAuthenticator;
import cu.phibrain.cardinal.app.ui.service.synchronize.CloudSyncManager;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Contract;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkSession;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Worker;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.ContractOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.WorkSessionOperations;
import cu.phibrain.plugins.cardinal.io.database.objects.ItemComparators;
import cu.phibrain.plugins.cardinal.io.network.NetworkUtilitiesCardinalOl;
import cu.phibrain.plugins.cardinal.io.network.api.AuthToken;
import eu.geopaparazzi.core.utilities.Constants;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.network.NetworkUtilities;
import eu.geopaparazzi.library.util.GPDialogs;

public class WorkSessionListActivity extends AppCompatActivity implements WorkSessionAdapter.OnClickCallback {

    private List<WorkSession> allWorkSessionList = new ArrayList<>();
    private List<WorkSession> visibleWorkSessionList = new ArrayList<>();

    private WorkSessionAdapter arrayAdapter;
    private EditText filterText;
    private ListView listView;


    private AppContainer appContainer;
    protected Contract mContract;
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_session_list);
        appContainer = ((CardinalApplication) getApplication()).getContainer();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        listView = findViewById(R.id.sessionList);

        filterText = findViewById(eu.geopaparazzi.core.R.id.search_box);
        filterText.addTextChangedListener(filterTextWatcher);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            mContract = ContractOperations
                    .getInstance()
                    .findOneBy(
                            appContainer.getProjectActive().getId(),
                            appContainer.getCurrentWorker().getId(),
                            true
                    );
        } catch (Exception e) {
            e.printStackTrace();
            GPLog.error(this, null, e);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        String filterStr = filterText.getText().toString();
        if (filterStr.length() > 0) {
            filterList(filterStr);
        } else {
            refreshList();
        }
    }

    protected void onDestroy() {
        filterText.removeTextChangedListener(filterTextWatcher);
        super.onDestroy();
    }


    private void refreshList() {
        if (GPLog.LOG_HEAVY)
            GPLog.addLogEntry(this, "refreshing sessions list"); //$NON-NLS-1$
        try {
            visibleWorkSessionList.clear();
            collectAllSession();
            visibleWorkSessionList.addAll(allWorkSessionList);
            Collections.sort(visibleWorkSessionList, new ItemComparators.WorkSessionComparator(true));
        } catch (Exception e) {
            GPLog.error(this, e.getLocalizedMessage(), e);
            e.printStackTrace();
        }

        redoAdapter();
    }

    private void collectAllSession() {
        allWorkSessionList.clear();
        if (mContract != null)
            allWorkSessionList.addAll(mContract.getWorkSessions());
    }


    private void filterList(String filterText) {
        if (GPLog.LOG_HEAVY)
            GPLog.addLogEntry(this, "filtering work session list"); //$NON-NLS-1$
        try {
            collectAllSession();
            visibleWorkSessionList.clear();
            filterText = filterText.toLowerCase();
            for (WorkSession session : allWorkSessionList) {
                String name = session.toString();
                String nameLower = name.toLowerCase();
                if (nameLower.contains(filterText)) {
                    visibleWorkSessionList.add(session);
                }
            }
        } catch (Exception e) {
            GPLog.error(this, e.getLocalizedMessage(), e);
            e.printStackTrace();
        }

        redoAdapter();
    }

    private void redoAdapter() {
        arrayAdapter = new WorkSessionAdapter(this, R.layout.activity_work_session_list, visibleWorkSessionList);
        arrayAdapter.setOnClickListener(this);
        listView.setAdapter(arrayAdapter);
    }

    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            // ignore
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // ignore
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            filterList(s.toString());
        }
    };

    @Override
    public void OnClickListener(WorkSession aSession, boolean isLogin) {

        if (isLogin) {
            aSession.setActive(true);
            aSession.setStartDate(new Date());
            WorkSessionOperations.getInstance().save(aSession);
            this.appContainer.setWorkSessionActive(aSession);
            Worker worker = this.appContainer.getCurrentWorker();

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            final String passwd = preferences.getString(Constants.PREF_KEY_PWD, "");
            final String server = preferences.getString(Constants.PREF_KEY_SERVER, "");
            final String serverTrailed = (!server.endsWith("/")) ? server + "/" : server;
            final String user = worker.getUsername();

            if (!NetworkUtilities.isNetworkAvailable(this)) {
                GPDialogs.infoDialog(this, getString(cu.phibrain.plugins.cardinal.io.R.string.available_only_with_network), null);
                return;
            }

            if (server.length() == 0 || user.length() == 0 || passwd.length() == 0) {
                GPDialogs.infoDialog(this, getString(cu.phibrain.plugins.cardinal.io.R.string.error_set_cloud_settings_cardinal), null);
                return;
            }

            ProgressDialog validatingDialog = ProgressDialog.show(
                    this,
                    worker.getFullName(),
                    getString(R.string.validating)
            );

            new AsyncTask<Boolean, Void, AuthToken>() {

                @Override
                protected AuthToken doInBackground(Boolean... booleans) {
                    try {
                        return NetworkUtilitiesCardinalOl.login(
                                serverTrailed,
                                user,
                                passwd
                        );
                    } catch (IOException e) {
                        e.printStackTrace();
                        GPLog.error(WorkSessionListActivity.this, null, e);
                        return null;
                    }
                }

                protected void onPostExecute(AuthToken authToken) { // on UI thread!
                    GPDialogs.dismissProgressDialog(validatingDialog);
                    try {
                        if (authToken != null) {
                            WorkSessionListActivity.this.createAppAccount(user, passwd, authToken);
                            WorkSessionListActivity.this.finish();
                        } else {
                            GPDialogs.infoDialog(
                                    WorkSessionListActivity.this,
                                    String.format(
                                            WorkSessionListActivity.this.getString(
                                                    R.string.worker_have_not_active
                                            ),
                                            user
                                    ),
                                    null
                            );
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        GPLog.error(WorkSessionListActivity.this, null, e);
                    }
                }
            }.execute();


        } else {
            GPDialogs.yesNoMessageDialog(WorkSessionListActivity.this, getString(R.string.do_you_want_to_logout_this_work_session),
                    () -> WorkSessionListActivity.this.runOnUiThread(() -> {
                        aSession.setActive(false);
                        aSession.setEndDate(new Date());
                        WorkSessionOperations.getInstance().save(aSession);
                        appContainer.setWorkSessionActive(null);
                        finish();

                    }), null
            );

        }
    }

    private void createAppAccount(String user, String passwd, AuthToken authToken) {

        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (!CloudSyncManager.getInstance().addAccount(user, passwd, authToken.toString())) {
            CloudAccountAuthenticator.init(this);
            CloudAccountAuthenticator.getInstance().getTokenForAccountCreateIfNeeded(
                    CloudAccount.ACCOUNT_TYPE, CloudAccount.AUTHTOKEN_TYPE
            );
        }
    }
}
