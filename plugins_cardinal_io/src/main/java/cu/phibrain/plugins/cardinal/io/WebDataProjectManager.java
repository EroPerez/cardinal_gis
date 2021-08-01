package cu.phibrain.plugins.cardinal.io;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.base.DaoSessionManager;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Contract;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Label;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelBatches;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelMaterial;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Layer;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefect;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Networks;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Project;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Stock;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Supplier;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WebDataProjectModel;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkSession;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Worker;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkerRoute;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Zone;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.ContractOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.LabelBatchesOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.LabelMaterialOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.LabelOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.LabelSubLotOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.LayerOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjecTypeDefectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjecTypeOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjecTypeStateOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectHasDefectHasImagesOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectHasDefectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectHasStateOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectImagesOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectMetadataOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MapObjectTypeAttributeOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.MaterialOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.NetworksOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.ProjectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.RouteSegmentOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.SignalEventsOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.StockOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.SupplierOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.TopologicalRuleOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.WorkSessionOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.WorkerOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.WorkerRouteOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.ZoneOperations;
import cu.phibrain.plugins.cardinal.io.exceptions.DownloadError;
import cu.phibrain.plugins.cardinal.io.network.NetworkUtilitiesCardinalOl;
import cu.phibrain.plugins.cardinal.io.network.api.AuthToken;
import cu.phibrain.plugins.cardinal.io.utils.CardinalMetadataTableDefaultValues;
import eu.geopaparazzi.core.database.DaoGpsLog;
import eu.geopaparazzi.core.database.DaoMetadata;
import eu.geopaparazzi.library.R;
import eu.geopaparazzi.library.core.ResourcesManager;
import eu.geopaparazzi.library.database.DefaultHelperClasses;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.database.IGpsLogDbHelper;
import eu.geopaparazzi.library.database.TableDescriptions;
import eu.geopaparazzi.library.util.LibraryConstants;
import eu.geopaparazzi.library.util.TimeUtilities;
import eu.geopaparazzi.library.util.Utilities;


/**
 * Singleton to handle cloud up- and download.
 *
 * @author Erodis Pérez Michel  (eperezm1986@gmail.com)
 */
@SuppressWarnings("nls")
public enum WebDataProjectManager {
    /**
     * Singleton instance.
     */
    INSTANCE;


    private String addActionPath(String server, String path) {
        if (server.endsWith("/")) {
            return server + path;
        } else {
            return server + "/" + path;
        }
    }


    /**
     * Downloads a project from the given server via GET.
     *
     * @param context    the {@link Context} to use.
     * @param server     the server from which to download.
     * @param user       the username for authentication.
     * @param passwd     the password for authentication.
     * @param webproject the project to download.
     * @return The path to the downloaded file
     */
    public String downloadProject(Context context, String server, String user, String passwd, WebDataProjectModel webproject, String outputFileName) throws DownloadError {
        try {
            //File outputDir = ResourcesManager.getInstance(context).getApplicationSupporterDir();
            File outputDir = ResourcesManager.getInstance(context).getMainStorageDir();
            File downloadedProjectFile = new File(outputDir, outputFileName);
            String newDbFileName = downloadedProjectFile.getPath();
            Log.i("WebDataProjectManager", "New project path: " + newDbFileName);
            if (downloadedProjectFile.exists()) {
                String wontOverwrite = context.getString(R.string.the_file_exists_wont_overwrite) + " " + downloadedProjectFile.getName();
                throw new DownloadError(wontOverwrite);
            }

            server = addActionPath(server, "");
            //First login into cardinal cloud service
            AuthToken token = NetworkUtilitiesCardinalOl.sendGetAuthToken(server, user, passwd);
            //First download nomencladores
            //material, supplier
            List<Supplier> suppliersList = NetworkUtilitiesCardinalOl.sendGetSuppliers(server, token, new HashMap<String, String>());

            List<LabelMaterial> labelMaterialList = NetworkUtilitiesCardinalOl.sendGetLabelMaterials(server, token, new HashMap<String, String>());


            //download a project referenced to id
            Project project = NetworkUtilitiesCardinalOl.sendGetProjectData(server, token, webproject.id);

            //Extract all worker and sessions from project contracts
            List<Contract> contractList = project.getContracts();
            List<Worker> workerList = new ArrayList<>();
            List<WorkSession> workSessionList = new ArrayList<>();
            for (Contract contract :
                    contractList) {
                Worker worker = contract.getWorker();
                contract.setWorkerId(worker.getId());
                workerList.add(worker);
                workSessionList.addAll(contract.getWorkSessions());
            }

            List<Stock> stockList = project.getStocks();
            List<Zone> zoneList = project.getZone();
            List<Networks> networksList = project.getNetworks();
            List<LabelBatches> labelBatchesList = project.getLabelBatches();
            List<Label> labelList = new ArrayList<>();

            for (LabelBatches lot :
                    labelBatchesList) {
                labelList.addAll(lot.getLabels());
            }

            DaoSessionManager.getInstance().setContext(context);
            DaoSessionManager.getInstance().setDatabaseName(newDbFileName);
            DaoSessionManager.getInstance().resetDaoSession();

            SupplierOperations.getInstance().insertAll(suppliersList);
            LabelMaterialOperations.getInstance().insertAll(labelMaterialList);
            ProjectOperations.getInstance().insert(project);

            // update project metadata
            SQLiteDatabase db = DaoSessionManager.getInstance().getSQLiteDatabase();

            // create table
            DaoMetadata.createTables(db);
            String uniqueDeviceId = Utilities.getUniqueDeviceId(context);
            DaoMetadata.initProjectMetadata(db, project.getName(), project.getDescription(), null, user, uniqueDeviceId);
            DaoMetadata.setValue(db, TableDescriptions.MetadataTableDefaultValues.KEY_CREATIONTS.getFieldName(), String.valueOf(project.getCreatedAt().getTime()));
            DaoMetadata.insertNewItem(db, CardinalMetadataTableDefaultValues.PROJECT_ID.getFieldName(), CardinalMetadataTableDefaultValues.PROJECT_ID.getFieldLabel(), String.valueOf(project.getId()));
            DaoMetadata.insertNewItem(db, CardinalMetadataTableDefaultValues.WORK_SESSION_ID.getFieldName(), CardinalMetadataTableDefaultValues.WORK_SESSION_ID.getFieldLabel(), "");
            DaoMetadata.insertNewItem(db, CardinalMetadataTableDefaultValues.MAP_OBJECT_TYPE_ID.getFieldName(), CardinalMetadataTableDefaultValues.MAP_OBJECT_TYPE_ID.getFieldLabel(), "");
            DaoMetadata.insertNewItem(db, CardinalMetadataTableDefaultValues.CURRENT_MAP_OBJECT_ID.getFieldName(), CardinalMetadataTableDefaultValues.CURRENT_MAP_OBJECT_ID.getFieldLabel(), "");
            DaoMetadata.insertNewItem(db, CardinalMetadataTableDefaultValues.PREVIOUS_MAP_OBJECT_ID.getFieldName(), CardinalMetadataTableDefaultValues.PREVIOUS_MAP_OBJECT_ID.getFieldLabel(), "");
            DaoMetadata.insertNewItem(db, CardinalMetadataTableDefaultValues.NETWORK_ID.getFieldName(), CardinalMetadataTableDefaultValues.NETWORK_ID.getFieldLabel(), "");

            //Inserts Operations
            WorkerOperations.getInstance().insertAll(workerList);
            ContractOperations.getInstance().insertAll(contractList);
            StockOperations.getInstance().insertAll(stockList);
            ZoneOperations.getInstance().insertAll(zoneList);
            NetworksOperations.getInstance().insertAll(networksList);
            LabelBatchesOperations.getInstance().insertAll(labelBatchesList);
            LabelOperations.getInstance().insertAll(labelList);

            // Save all layers in networks
            List<Layer> layerList = new ArrayList<>();
            for (Networks network :
                    networksList) {
                layerList.addAll(network.getLayers());
            }
            LayerOperations.getInstance().insertAll(layerList);

            // Get and save all mapobjectypes in layer
            List<MapObjecType> objecTypeList = new ArrayList<>();
            for (Layer layer :
                    layerList) {
                objecTypeList.addAll(layer.getMapobjectypes());
            }

            MapObjecTypeOperations.getInstance().insertAll(objecTypeList);

            // Get and save all extra mapobject type properties
            for (MapObjecType mapObjecType :
                    objecTypeList) {
                MapObjectTypeAttributeOperations.getInstance().insertAll(mapObjecType.getAttributes());
                MapObjecTypeStateOperations.getInstance().insertAll(mapObjecType.getStates());
                MapObjecTypeDefectOperations.getInstance().insertAll(mapObjecType.getDefects());
                TopologicalRuleOperations.getInstance().insertAll(mapObjecType.getTopoRule());
            }

            WorkSessionOperations.getInstance().insertAll(workSessionList);

            List<MapObject> mapObjectList = new ArrayList<>();
            // Prepare log table to transfer cardus log to gp log
            DaoGpsLog.createTables(db);
            Class<?> logHelper = Class.forName(DefaultHelperClasses.GPSLOG_HELPER_CLASS);
            IGpsLogDbHelper dbHelper = (IGpsLogDbHelper) logHelper.newInstance();

            //Get and save all objects in worksessions
            for (WorkSession session :
                    workSessionList) {
                MaterialOperations.getInstance().insertAll(session.getMaterials());
                LabelSubLotOperations.getInstance().insertAll(session.getLabels());
                SignalEventsOperations.getInstance().insertAll(session.getEvents());
                mapObjectList.addAll(session.getMapObjects());

                List<WorkerRoute> gpslogs = session.getWorkerRoute();
                if (!gpslogs.isEmpty()) {
                    //generate local gps log
                    final String logName = "log_" + TimeUtilities.INSTANCE.TIMESTAMPFORMATTER_LOCAL.format(new Date()); //$NON-NLS-1$
                    long now = System.currentTimeMillis();

                    long gpsLogId = dbHelper.addGpsLog(now, now, 0.0f, logName, LibraryConstants.DEFAULT_LOG_WIDTH, "red", true);

                    for (WorkerRoute log :
                            gpslogs) {
                        dbHelper.addGpsLogDataPoint(db, gpsLogId, log.getLongitude(), log.getLatitude(), log.getAltitude(), log.getCreatedAt().getTime());
                    }

                    try {
                        DaoGpsLog.updateLogLength(gpsLogId);
                    } catch (IOException e) {
                        GPLog.error(this, "ERROR", e);//NON-NLS
                    }


                    //save current log
                    WorkerRouteOperations.getInstance().save(new WorkerRoute(null, session.getId(), gpsLogId));
                }
            }


            MapObjectOperations.getInstance().insertAll(mapObjectList);
            //Get and save all object in mapObjects
            List<MapObjectHasDefect> mapObjectHasDefectList = new ArrayList<>();
            for (MapObject mapObject :
                    mapObjectList) {
                RouteSegmentOperations.getInstance().insertAll(mapObject.getRouteSegments());
                MapObjectHasStateOperations.getInstance().insertAll(mapObject.getStates());
                MapObjectImagesOperations.getInstance().insertAll(mapObject.getImages());
                MapObjectMetadataOperations.getInstance().insertAll(mapObject.getMetadata());
                mapObjectHasDefectList.addAll(mapObject.getDefects());

            }
            MapObjectHasDefectOperations.getInstance().insertAll(mapObjectHasDefectList);

            for (MapObjectHasDefect mapObjectdefect :
                    mapObjectHasDefectList) {
                MapObjectHasDefectHasImagesOperations.getInstance().insertAll(mapObjectdefect.getImages());
            }

            long fileLength = downloadedProjectFile.length();
            if (fileLength == 0) {
                throw new DownloadError("Error downloading project from cloud server.");
            }

            return downloadedProjectFile.getCanonicalPath();
        } catch (DownloadError e) {
            GPLog.error(this, null, e);
            throw e;
        } catch (Exception e) {
            GPLog.error(this, null, e);
            throw new DownloadError(e);
        }
    }

    /**
     * Downloads the data projects list from the given server via GET.
     *
     * @param context the {@link Context} to use.
     * @param server  the server from which to download.
     * @param user    the username for authentication.
     * @param passwd  the password for authentication.
     * @return the project list.
     * @throws Exception if something goes wrong.
     */
    public List<WebDataProjectModel> downloadDataProjectsList(Context context, String server, String user, String passwd) throws Exception {

        server = addActionPath(server, "");

        //First login into cardinal cloud service
        AuthToken token = NetworkUtilitiesCardinalOl.sendGetAuthToken(server, user, passwd);
        //And then get project list
        List<WebDataProjectModel> webDataList = NetworkUtilitiesCardinalOl.sendGetProjectDataList(server, token, new HashMap<String, String>());
        return webDataList;
    }

}
