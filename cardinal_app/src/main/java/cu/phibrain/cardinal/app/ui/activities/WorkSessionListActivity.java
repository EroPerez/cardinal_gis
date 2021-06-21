package cu.phibrain.cardinal.app.ui.activities;

import android.content.SharedPreferences;
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
import java.util.List;

import cu.phibrain.cardinal.app.CardinalApplication;
import cu.phibrain.cardinal.app.R;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.cardinal.app.ui.adapter.WorkSessionAdapter;
import cu.phibrain.plugins.cardinal.io.database.entity.ContractOperations;
import cu.phibrain.plugins.cardinal.io.database.objects.ItemComparators;
import cu.phibrain.plugins.cardinal.io.model.Contract;
import cu.phibrain.plugins.cardinal.io.model.WorkSession;
import eu.geopaparazzi.library.database.GPLog;

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
        appContainer = ((CardinalApplication) getApplication()).appContainer;

        Toolbar toolbar = findViewById(eu.geopaparazzi.core.R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        listView = findViewById(R.id.sessionList);

        filterText = findViewById(eu.geopaparazzi.core.R.id.search_box);
        filterText.addTextChangedListener(filterTextWatcher);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        try {
            mContract = ContractOperations.getInstance().findOneBy(appContainer.getProjectActive().getId(), appContainer.getCurrentWorker().getId());
        } catch (IOException e) {
            e.printStackTrace();
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
            GPLog.addLogEntry(this, "refreshing notes list"); //$NON-NLS-1$
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
        List<WorkSession> tmpNotesList = mContract.getWorkSessions();
        allWorkSessionList.addAll(tmpNotesList);
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
    public void OnClickListener(WorkSession aSession) {
        appContainer.setWorkSessionActive(aSession);
        finish();
    }
}
