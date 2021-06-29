package cu.phibrain.plugins.cardinal.io.importer;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.danlew.android.joda.JodaTimeAndroid;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.R;
import cu.phibrain.plugins.cardinal.io.WebDataProjectManager;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WebDataProjectModel;
import cu.phibrain.plugins.cardinal.io.utils.JodaTimeHelper;
import eu.geopaparazzi.core.GeopaparazziApplication;
import eu.geopaparazzi.library.core.ResourcesManager;
import eu.geopaparazzi.library.database.DatabaseUtilities;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.LibraryConstants;
import eu.geopaparazzi.library.util.StringAsyncTask;
import eu.geopaparazzi.library.util.TextRunnable;
import eu.geopaparazzi.library.util.TimeUtilities;

import static eu.geopaparazzi.library.util.LibraryConstants.PREFS_KEY_PWD;
import static eu.geopaparazzi.library.util.LibraryConstants.PREFS_KEY_URL;
import static eu.geopaparazzi.library.util.LibraryConstants.PREFS_KEY_USER;

/**
 * Web projects listing activity.
 *
 * @author Erodis Pérez Michel  (eperezm1986@gmail.com)
 */
public class CardinalProjectImporterActivity extends ListActivity {
    public static final int DOWNLOADDATA_RETURN_CODE = 667;

    protected static final String ASYNC_ERROR = "OK"; //$NON-NLS-1$
    protected static final String ASYNC_OK = "OK"; //$NON-NLS-1$

    private ArrayAdapter<WebDataProjectModel> arrayAdapter;
    private EditText filterText;

    private List<WebDataProjectModel> projectList = new ArrayList<>();
    private List<WebDataProjectModel> dataListToLoad = new ArrayList<>();

    private String user;
    private String pwd;
    private String url;

    private ProgressDialog downloadDataListDialog;
    //private ProgressDialog cloudProgressDialog;
    private StringAsyncTask stringAsyncTask;


    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.webdatalist);
        // It's important to initialize the ResourceZoneInfoProvider; otherwise
        // joda-time-android will not work.
        JodaTimeAndroid.init(this);

        Bundle extras = getIntent().getExtras();
        user = extras.getString(PREFS_KEY_USER);
        pwd = extras.getString(PREFS_KEY_PWD);
        url = extras.getString(PREFS_KEY_URL);

        filterText = (EditText) findViewById(R.id.search_box);
        filterText.addTextChangedListener(filterTextWatcher);


        downloadDataListDialog = ProgressDialog.show(this, getString(R.string.downloading),
                getString(R.string.downloading_layers_list_from_server));
        new AsyncTask<String, Void, String>() {

            protected String doInBackground(String... params) {
                cu.phibrain.plugins.cardinal.io.importer.CardinalProjectImporterActivity context = cu.phibrain.plugins.cardinal.io.importer.CardinalProjectImporterActivity.this;
                try {
                    projectList = WebDataProjectManager.INSTANCE.downloadDataProjectsList(context, url, user, pwd);
                    for (WebDataProjectModel wp : projectList) {
                        dataListToLoad.add(wp);
                    }
                    return ASYNC_OK; //$NON-NLS-1$
                } catch (Exception e) {
                    GPLog.error(this, null, e);
                    return ASYNC_ERROR;
                }
            }

            protected void onPostExecute(String response) { // on UI thread!
                GPDialogs.dismissProgressDialog(downloadDataListDialog);
                cu.phibrain.plugins.cardinal.io.importer.CardinalProjectImporterActivity context = cu.phibrain.plugins.cardinal.io.importer.CardinalProjectImporterActivity.this;
                if (response.equals(ASYNC_OK)) {
                    refreshList();
                } else {
                    GPDialogs.warningDialog(context, getString(R.string.error_data_list), null);
                }
            }

        }.execute((String) null);


        FloatingActionButton downloadButton = (FloatingActionButton) findViewById(R.id.downloadButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String defaultName = getDefaultName();

                GPDialogs.inputMessageDialog(cu.phibrain.plugins.cardinal.io.importer.CardinalProjectImporterActivity.this, getString(R.string.enter_a_name_for_downloaded_project), defaultName, new TextRunnable() {

                    @Override
                    public void run() {

                        for (WebDataProjectModel dataLayer : dataListToLoad) {
                            if (dataLayer.isSelected) {
                                downloadProject(dataLayer);
                                break;
                            }
                        }
                    }

                    private void downloadProject(final WebDataProjectModel projectModel) {
//                        cloudProgressDialog = ProgressDialog.show(cu.phibrain.plugins.cardinal.io.importer.CardinalProjectImporterActivity.this, getString(eu.geopaparazzi.library.R.string.downloading),
//                                getString(eu.geopaparazzi.library.R.string.downloading_project), true, false);

                        stringAsyncTask = new StringAsyncTask(cu.phibrain.plugins.cardinal.io.importer.CardinalProjectImporterActivity.this) {
                            protected String dbFile;

                            @Override
                            protected String doBackgroundWork() {
                                cu.phibrain.plugins.cardinal.io.importer.CardinalProjectImporterActivity context = cu.phibrain.plugins.cardinal.io.importer.CardinalProjectImporterActivity.this;
                                try {
                                    dbFile = WebDataProjectManager.INSTANCE.downloadProject(cu.phibrain.plugins.cardinal.io.importer.CardinalProjectImporterActivity.this, url, user, pwd, projectModel, theTextToRunOn);
                                    DatabaseUtilities.setNewDatabase(context, GeopaparazziApplication.getInstance(), dbFile);

                                    return ASYNC_OK; //$NON-NLS-1$
                                } catch (Exception e) {
                                    GPLog.error(this, null, e);
                                    return e.getLocalizedMessage();
                                }
                            }

                            @Override
                            protected void doUiPostWork(String response) {
                                //GPDialogs.dismissProgressDialog(cloudProgressDialog);
                                dispose();
                                cu.phibrain.plugins.cardinal.io.importer.CardinalProjectImporterActivity context = cu.phibrain.plugins.cardinal.io.importer.CardinalProjectImporterActivity.this;
                                if (ASYNC_OK.equals(response)) {
                                    String okMsg = getString(R.string.data_successfully_downloaded);
                                    GPDialogs.infoDialog(context, okMsg, new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = getIntent();
                                            intent.putExtra(LibraryConstants.DATABASE_ID, theTextToRunOn);
                                            cu.phibrain.plugins.cardinal.io.importer.CardinalProjectImporterActivity.this.setResult(DOWNLOADDATA_RETURN_CODE, intent);
                                            finish();
                                        }
                                    });
                                } else {
                                    GPDialogs.warningDialog(context, response, null);
                                }
                            }

                            @Override
                            protected void onCancelled() {
                                super.onCancelled();
                            }

                            @Override
                            protected void onCancelled(String s) {
                                super.onCancelled(s);
                            }
                        };

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                stringAsyncTask.setProgressDialog(getString(R.string.downloading), getString(R.string.downloading_data_from_server), false, null);
                                stringAsyncTask.execute();
                            }
                        });

                    }


                });
            }
        });

    }

    /**
     * Returns a human-friendly unique name. If a single layer was selected,
     * the name will be based on the name of this layer.
     *
     * @return
     */
    protected String getDefaultName() {
        File outputDir;
        try {
            outputDir = ResourcesManager.getInstance(this).getApplicationSupporterDir();
        } catch (Exception e) {
            outputDir = null;
        }
        String prefix, timestamp;
        ArrayList<WebDataProjectModel> selectedLayers = new ArrayList<WebDataProjectModel>();
        for (WebDataProjectModel layer : dataListToLoad) {
            if (layer.isSelected) {
                selectedLayers.add(layer);
            }
        }
        if (selectedLayers.size() == 1) {
            prefix = selectedLayers.get(0).name;
            // remove workspace
            if (prefix.contains(":")) {
                prefix = prefix.split(":", 2)[1];
            }

            // sanitize name
            prefix = prefix.replaceAll("[^a-zA-Z0-9]+", "");
        } else {
            prefix = "projects";
        }
        if (outputDir == null) {
            timestamp = TimeUtilities.INSTANCE.TIMESTAMPFORMATTER_LOCAL.format(new Date());
            return prefix + "_" + timestamp + LibraryConstants.GEOPAPARAZZI_DB_EXTENSION;
        } else {
            timestamp = TimeUtilities.INSTANCE.DATEONLY_FORMATTER.format(new Date()).replace("-", "");
            String baseName = prefix + "_" + timestamp;
            File f = new File(outputDir, baseName + LibraryConstants.GEOPAPARAZZI_DB_EXTENSION);
            int i = 1;
            while (f.exists()) {
                f = new File(outputDir, baseName + "_" + i + LibraryConstants.GEOPAPARAZZI_DB_EXTENSION);
                i++;
            }
            return f.getName();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    @Override
    protected void onPause() {
        GPDialogs.dismissProgressDialog(downloadDataListDialog);
        //GPDialogs.dismissProgressDialog(cloudProgressDialog);
        super.onPause();
    }

    protected void onDestroy() {
//        if (stringAsyncTask != null) stringAsyncTask.dispose();
        filterText.removeTextChangedListener(filterTextWatcher);
        super.onDestroy();
    }

    private void filterList(String filterText) {
        if (GPLog.LOG)
            GPLog.addLogEntry(this, "filter projects list"); //$NON-NLS-1$

        dataListToLoad.clear();
        for (WebDataProjectModel webDataLayer : projectList) {
            if (webDataLayer.matches(filterText)) {
                dataListToLoad.add(webDataLayer);
            }
        }

        refreshList();
    }

    private void refreshList() {
        if (GPLog.LOG)
            GPLog.addLogEntry(this, "refreshing projects list"); //$NON-NLS-1$
        arrayAdapter = new ArrayAdapter<WebDataProjectModel>(this, R.layout.webdatarow, dataListToLoad) {
            @Override
            public View getView(int position, View cView, ViewGroup parent) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = inflater.inflate(R.layout.webdatarow, null);

                final WebDataProjectModel projectModel = dataListToLoad.get(position);
                TextView nameText = (TextView) rowView.findViewById(R.id.titletext);
                TextView descriptionText = (TextView) rowView.findViewById(R.id.descriptiontext);
                TextView createdAtText = (TextView) rowView.findViewById(R.id.geomtypetext);
                TextView idText = (TextView) rowView.findViewById(R.id.sridtext);
                final RadioButton selectedBox = (RadioButton) rowView.findViewById(R.id.selectedCheck);
                selectedBox.setChecked(projectModel.isSelected);
                selectedBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        projectModel.isSelected = selectedBox.isChecked();
                        for (WebDataProjectModel dl : dataListToLoad) {
                            if (projectModel.id != dl.id)
                                dl.isSelected = false;

                        }
                    }
                });

                nameText.setText(projectModel.name);
                descriptionText.setText(android.text.Html.fromHtml(projectModel.description).toString().trim());
                createdAtText.setText(getString(R.string.geometry_type) + JodaTimeHelper.calculateAge(DateTime.parse(projectModel.created_at)));
                idText.setText(getString(R.string.srid) + projectModel.id);
                return rowView;
            }
        };

        setListAdapter(arrayAdapter);
    }


    private TextWatcher filterTextWatcher = new TextWatcher() {

        public void afterTextChanged(Editable s) {
            // ignore
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // ignore
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // arrayAdapter.getFilter().filter(s);
            filterList(s.toString());
        }
    };

}
