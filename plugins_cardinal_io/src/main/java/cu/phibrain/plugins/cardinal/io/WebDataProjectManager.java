package cu.phibrain.plugins.cardinal.io;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.text.Html;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.database.base.DaoSessionManager;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Contract;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Devices;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Label;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelBatches;
import cu.phibrain.plugins.cardinal.io.database.entity.model.LabelMaterial;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Layer;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjecType;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObject;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefect;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasDefectHasImages;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectHasState;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectImages;
import cu.phibrain.plugins.cardinal.io.database.entity.model.MapObjectMetadata;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Networks;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Project;
import cu.phibrain.plugins.cardinal.io.database.entity.model.ProjectConfig;
import cu.phibrain.plugins.cardinal.io.database.entity.model.RouteSegment;
import cu.phibrain.plugins.cardinal.io.database.entity.model.SignalEvents;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Stock;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Supplier;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WebDataProjectModel;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkSession;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Worker;
import cu.phibrain.plugins.cardinal.io.database.entity.model.WorkerRoute;
import cu.phibrain.plugins.cardinal.io.database.entity.model.Zone;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.ContractOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.operations.DevicesOperations;
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
import cu.phibrain.plugins.cardinal.io.database.entity.operations.ProjectConfigOperations;
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
import cu.phibrain.plugins.cardinal.io.network.api.APIError;
import cu.phibrain.plugins.cardinal.io.network.api.AuthToken;
import cu.phibrain.plugins.cardinal.io.utils.CardinalMetadataTableDefaultValues;
import cu.phibrain.plugins.cardinal.io.utils.GsonHelper;
import eu.geopaparazzi.core.database.DaoGpsLog;
import eu.geopaparazzi.core.database.DaoMetadata;
import eu.geopaparazzi.core.database.objects.Line;
import eu.geopaparazzi.library.R;
import eu.geopaparazzi.library.core.ResourcesManager;
import eu.geopaparazzi.library.database.DefaultHelperClasses;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.database.IGpsLogDbHelper;
import eu.geopaparazzi.library.database.TableDescriptions;
import eu.geopaparazzi.library.network.NetworkUtilities;
import eu.geopaparazzi.library.style.ColorUtilities;
import eu.geopaparazzi.library.util.DynamicDoubleArray;
import eu.geopaparazzi.library.util.TimeUtilities;
import eu.geopaparazzi.library.util.Utilities;

import static eu.geopaparazzi.library.util.LibraryConstants.DEFAULT_LOG_WIDTH;


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


    /**
     * Uploads a project to the given server via POST.
     *
     * @param context   the {@link Context} to use.
     * @param server    the server to which to upload.
     * @param user      the username for authentication.
     * @param passwd    the password for authentication.
     * @param projectId the project id to upload
     * @return the return message.
     */
    public String uploadProject(Context context, String server, String user, String passwd, long projectId, boolean uploadImages) {
        boolean interrupted = false;
        try {
            server = addActionPath(server, "");
            //First login into cardinal cloud service
            AuthToken token = NetworkUtilitiesCardinalOl.sendGetAuthToken(server, user, passwd);

            List<Contract> contractList = ContractOperations.getInstance().findAllBy(projectId);
            List<RouteSegment> routeSegments = new ArrayList<>();
            List<MapObject> mapObjectListToDelete = new ArrayList<>();
            // Collect all session in project
            for (Contract contract : contractList) {
               // Worker worker = contract.getTheWorker();
                Worker worker = WorkerOperations.getInstance().load(contract.getWorkerId());

                // export to server from here
                for (Devices device : worker.getDevices()) {
                    // Synchronize worker device
                    if (device.mustExport()) {
                        if (device.getIsSync()) {
                            if (!NetworkUtilitiesCardinalOl.sendPutWorkerDevice(server, token, (Devices) device.toRemoteObject())) {
                                interrupted = true;
                                break;
                            } else {
                                device.setSyncDate(new Date());
                            }
                        } else {
                            Devices remoteDevice = NetworkUtilitiesCardinalOl.sendPostWorkerDevice(server, token, (Devices) device.toRemoteObject());
                            if (remoteDevice != null) {
                                device.setIsSync(true);
                                Date now = new Date();
                                device.setUpdatedAt(now);
                                device.setSyncDate(now);
                            } else {
                                interrupted = true;
                                break;
                            }
                        }
                        device.update();
                    }
                }


                // Get and save all objects in worksessions
                for (WorkSession session : contract.getWorkSessions()) {

                    // Synchronize work session
                    if (session.mustExport()) {
                        if (!NetworkUtilitiesCardinalOl.sendPut2WorkSession(server, token, (WorkSession) session.toRemoteObject())) {
                            interrupted = true;
                            break;
                        } else {
                            session.setSyncDate(new Date());
                            session.update();
                        }
                    }

                    // Synchronize map objects
                    session.resetMapObjects();
                    List<MapObject> mapObjectList = session.getMapObjects();
                    for (MapObject mapObject : mapObjectList) {
                        // Sync map object itself
                        if (mapObject.mustExport()) {
                            MapObject mapObjectJoinObj = mapObject.getJoinObj();
                            if (mapObjectJoinObj == null || updateJoinObjBefore(server, token, mapObjectJoinObj)) {
                                mapObject.refresh();
                                mapObject.setSyncDate(new Date());
                                if (mapObject.getIsSync()) {
                                    if (!mapObject.getDeleted()) {
                                        if (!NetworkUtilitiesCardinalOl.sendPut2MapObject(server, token, (MapObject) mapObject.toRemoteObject())) {
                                            interrupted = true;
                                            break;
                                        }
                                    }
                                } else {
                                    mapObject.setIsSync(true);
                                    MapObject mapObjectRemote = NetworkUtilitiesCardinalOl.sendPostMapObject(server, token, (MapObject) mapObject.toRemoteObject());
                                    if (mapObjectRemote != null) {
                                        mapObject.setRemoteId(mapObjectRemote.getId());
                                    } else {
                                        interrupted = true;
                                        break;
                                    }
                                }
                                if (mapObject.getDeleted())
                                    mapObjectListToDelete.add(mapObject);
                                else
                                    mapObject.update();
                            } else {
                                interrupted = true;
                                break;
                            }
                        }
                        // Select route segments to sync
                        mapObject.resetRouteSegments();
                        for (RouteSegment route : mapObject.getRouteSegments()) {
                            if (route.mustExport()) {
                                routeSegments.add(route);
                            }
                        }

                        // Sync MapObjectHasState
                        mapObject.resetStates();
                        List<MapObjectHasState> statesList = mapObject.getStates();
                        for (MapObjectHasState state : statesList) {
                            if (state.mustExport()) {
                                if (state.getIsSync()) {
                                    if (!NetworkUtilitiesCardinalOl.sendPut2MapObjectHasState(server, token, (MapObjectHasState) state.toRemoteObject())) {
                                        interrupted = true;
                                        break;
                                    } else {
                                        state.setSyncDate(new Date());
                                    }
                                } else {
                                    MapObjectHasState remoteState = NetworkUtilitiesCardinalOl.sendPostMapObjectHasState(server, token, (MapObjectHasState) state.toRemoteObject());
                                    if (remoteState != null) {
                                        state.setIsSync(true);
                                        state.setSyncDate(new Date());
                                        state.setRemoteId(remoteState.getId());
                                    } else {
                                        interrupted = true;
                                        break;
                                    }
                                }
                                if (state.getDeleted())
                                    state.delete();
                                else
                                    state.update();
                            }
                        }
                        // Sync MapObjectImages
                        if (uploadImages) {
                            mapObject.resetImages();
                            List<MapObjectImages> imageList = mapObject.getImages();
                            for (MapObjectImages objectImages : imageList) {
                                if (objectImages.mustExport()) {
                                    if (objectImages.getIsSync()) {
                                        if (!NetworkUtilitiesCardinalOl.sendPut2MapObjectImages(server, token, (MapObjectImages) objectImages.toRemoteObject())) {
                                            interrupted = true;
                                            break;
                                        } else {
                                            objectImages.setSyncDate(new Date());
                                        }
                                    } else {
                                        MapObjectImages remoteImage = NetworkUtilitiesCardinalOl.sendPostMapObjectImages(server, token, (MapObjectImages) objectImages.toRemoteObject());
                                        if (remoteImage != null) {
                                            objectImages.setIsSync(true);
                                            objectImages.setSyncDate(new Date());
                                            objectImages.setRemoteId(remoteImage.getId());
                                        } else {
                                            interrupted = true;
                                            break;
                                        }
                                    }
                                    if (objectImages.getDeleted())
                                        objectImages.delete();
                                    else
                                        objectImages.update();
                                }
                            }
                        }

                        // Sync MapObjectMetadata
                        mapObject.resetMetadata();
                        List<MapObjectMetadata> metadata = mapObject.getMetadata();
                        for (MapObjectMetadata objectMetadata : metadata) {
                            if (objectMetadata.mustExport()) {
                                if (objectMetadata.getIsSync()) {
                                    if (!NetworkUtilitiesCardinalOl.sendPut2MapObjectMetadata(server, token, (MapObjectMetadata) objectMetadata.toRemoteObject())) {
                                        interrupted = true;
                                        break;
                                    } else {
                                        objectMetadata.setSyncDate(new Date());
                                    }
                                } else {
                                    MapObjectMetadata remoteMeta = NetworkUtilitiesCardinalOl.sendPostMapObjectMetadata(server, token, (MapObjectMetadata) objectMetadata.toRemoteObject());
                                    if (remoteMeta != null) {
                                        objectMetadata.setIsSync(true);
                                        objectMetadata.setSyncDate(new Date());
                                        remoteMeta.setRemoteId(remoteMeta.getId());
                                    } else {
                                        interrupted = true;
                                        break;
                                    }
                                }
                                if (objectMetadata.getDeleted())
                                    objectMetadata.delete();
                                else
                                    objectMetadata.update();
                            }
                        }

                        // Sync MapObjectHasDefect
                        mapObject.resetDefects();
                        List<MapObjectHasDefect> mapObjectHasDefectList = mapObject.getDefects();
                        for (MapObjectHasDefect defect : mapObjectHasDefectList) {
                            if (defect.mustExport()) {
                                if (defect.getIsSync()) {
                                    if (!defect.getDeleted()) {
                                        if (!NetworkUtilitiesCardinalOl.sendPut2MapObjectHasDefect(server, token, (MapObjectHasDefect) defect.toRemoteObject())) {
                                            interrupted = true;
                                            break;
                                        } else {
                                            defect.setSyncDate(new Date());
                                        }
                                    }
                                } else {
                                    MapObjectHasDefect defectRemote = NetworkUtilitiesCardinalOl.sendPostMapObjectHasDefect(server, token, (MapObjectHasDefect) defect.toRemoteObject());
                                    if (defectRemote != null) {
                                        defect.setIsSync(true);
                                        defect.setSyncDate(new Date());
                                        defect.setRemoteId(defectRemote.getId());
                                    } else {
                                        interrupted = true;
                                        break;
                                    }
                                }

                                defect.update();
                            }

                            // Sync images in defect
                            if (uploadImages) {
                                defect.resetImages();
                                List<MapObjectHasDefectHasImages> hasDefectHasImages = defect.getImages();
                                for (MapObjectHasDefectHasImages images : hasDefectHasImages) {
                                    if (images.mustExport()) {
                                        if (images.getIsSync()) {
                                            if (!NetworkUtilitiesCardinalOl.sendPut2MapObjectHasDefectHasImages(server, token, (MapObjectHasDefectHasImages) images.toRemoteObject())) {
                                                interrupted = true;
                                                break;
                                            } else {
                                                defect.setSyncDate(new Date());
                                            }
                                        } else {
                                            MapObjectHasDefectHasImages defectImageRemote = NetworkUtilitiesCardinalOl.sendPostMapObjectHasDefectHasImages(server, token, (MapObjectHasDefectHasImages) images.toRemoteObject());
                                            if (defectImageRemote != null) {
                                                images.setIsSync(true);
                                                images.setSyncDate(new Date());
                                                images.setRemoteId(defectImageRemote.getId());

                                            } else {
                                                interrupted = true;
                                                break;
                                            }
                                        }
                                        if (images.getDeleted())
                                            images.delete();
                                        else
                                            images.update();
                                    }

                                }
                            }

                            if (defect.getDeleted() && !interrupted) {
                                if (NetworkUtilitiesCardinalOl.sendDelete2MapObjectHasDefect(server, token, (MapObjectHasDefect) defect.toRemoteObject())) {
                                    defect.delete();
                                }
                            }
                        }

                    }

                    // Synchronize signal events
                    session.resetEvents();
                    List<SignalEvents> eventList = session.getEvents();
                    for (SignalEvents event : eventList) {
                        if (event.mustExport()) {
                            if (event.getIsSync()) {
                                if (!NetworkUtilitiesCardinalOl.sendPut2SignalEvents(server, token, (SignalEvents) event.toRemoteObject())) {
                                    interrupted = true;
                                    break;
                                } else {
                                    event.setSyncDate(new Date());
                                }
                            } else {
                                SignalEvents remoteEvent = NetworkUtilitiesCardinalOl.sendPostSignalEvents(server, token, (SignalEvents) event.toRemoteObject());
                                if (remoteEvent != null) {
                                    event.setIsSync(true);
                                    event.setSyncDate(new Date());
                                    event.setRemoteId(remoteEvent.getId());
                                } else {
                                    interrupted = true;
                                    break;
                                }
                            }
                            event.update();
                        }

                    }

                    // Synchronize worker route
                    session.resetWorkerRoute();
                    List<WorkerRoute> routeList = session.getWorkerRoute();
                    for (WorkerRoute wroute : routeList) {
                        if (wroute.mustExport()) {
                            Line line = DaoGpsLog.getGpslogAsLine(wroute.getGpsLogsTableId(), -1);
                            DynamicDoubleArray lonArray = line.getLonList();
                            DynamicDoubleArray latArray = line.getLatList();
                            DynamicDoubleArray elevArray = line.getAltimList();
                            List<String> dates = line.getDateList();
                            for (int i = (int) wroute.getSyncPointCount(); i < lonArray.size(); i++) {
                                double elev = elevArray.get(i);
                                double lat = latArray.get(i);
                                double lon = lonArray.get(i);
                                String dateStr = dates.get(i);
                                long time = Long.parseLong(dateStr);

                                if (!NetworkUtilitiesCardinalOl.sendPostWorkerRoute(server, token, new WorkerRoute(null, session.getId(), lat, lon, elev, new Date(time)))) {
                                    interrupted = true;
                                    break;
                                }
                            }

                            if (!interrupted) {
                                wroute.setIsSync(true);
                                wroute.setSyncDate(new Date());
                                wroute.setSyncPointCount(lonArray.size());
                                WorkerRouteOperations.getInstance().update2(wroute);
                            }
                        }

                    }
                }

            }

            //Finally sync al route segment to server
            for (RouteSegment route : routeSegments) {

//                Log.d("RouteSegmenTag", route.toString());
                if (route.getIsSync()) {
                    if (NetworkUtilitiesCardinalOl.sendPut2RouteSegment(server, token, (RouteSegment) route.toRemoteObject())) {
                        route.setSyncDate(new Date());
                    } else {
                        interrupted = true;
                        break;
                    }
                } else {
                    RouteSegment routeSegmentRemote = NetworkUtilitiesCardinalOl.sendPostRouteSegment(server, token, (RouteSegment) route.toRemoteObject());
                    if (routeSegmentRemote != null) {
                        route.setIsSync(true);
                        route.setSyncDate(new Date());
                        route.setRemoteId(routeSegmentRemote.getId());

                    } else {
                        interrupted = true;
                        break;
                    }
                }

                if (route.getDeleted())
                    route.delete();
                else
                    route.update();

            }

            if (!interrupted) {
                for (MapObject mapObject : mapObjectListToDelete) {
                    if (NetworkUtilitiesCardinalOl.sendDelete2MapObject(server, token, (MapObject) mapObject.toRemoteObject())) {
                        mapObject.delete();
                    }
                }
                return NetworkUtilities.getMessageForCode(context, 200,
                        context.getResources().getString(R.string.post_completed_properly));
            }

            return NetworkUtilities.getMessageForCode(context, 200,
                    context.getResources().getString(cu.phibrain.plugins.cardinal.io.R.string.post_not_completed_properly));
        } catch (Exception e) {
            GPLog.error(this, null, e);
            if (GsonHelper.isJSONValid(e.getMessage())) {
                APIError error = GsonHelper.createPojoFromString(e.getMessage(), APIError.class);
                return NetworkUtilities.getMessageForCode(context, error.status(), error.message());
            } else
                return NetworkUtilities.getMessageForCode(context, 500, e.getLocalizedMessage());
        }

    }


    private boolean updateJoinObjBefore(String server, AuthToken token, MapObject mapObjectJoinObj) {

        MapObject mapObject = mapObjectJoinObj.getJoinObj();

        if (mapObject == null) {

            if (mapObjectJoinObj.mustExport()) {
                if (!mapObjectJoinObj.getIsSync()) {
                    mapObjectJoinObj.setSyncDate(new Date());
                    mapObjectJoinObj.setIsSync(true);
                    MapObject mapObjectRemote = NetworkUtilitiesCardinalOl.sendPostMapObject(server, token, (MapObject) mapObjectJoinObj.toRemoteObject());
                    if (mapObjectRemote != null) {
                        mapObjectJoinObj.setRemoteId(mapObjectRemote.getId());
                        mapObjectJoinObj.update();
                    } else {
                        return false;
                    }
                }
            }
            return true;
        }

        return updateJoinObjBefore(server, token, mapObject);
    }


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
     * @param context        the {@link Context} to use.
     * @param server         the server from which to download.
     * @param user           the username for authentication.
     * @param passwd         the password for authentication.
     * @param webproject     the project to download.
     * @param downloadImages
     * @return The path to the downloaded file
     */
    public String downloadProject(Context context, String server, String user, String passwd, WebDataProjectModel webproject, String outputFileName, boolean downloadImages) throws DownloadError {
        try {
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
            Project project = NetworkUtilitiesCardinalOl.sendGetProjectData(server, token, webproject.id, downloadImages);

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
            List<ProjectConfig> configList = project.getConfigurations();

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
            String description = "Cloud project";
            if (project.getDescription() != null) {
                description = Html.fromHtml(project.getDescription()).toString();
            }
            DaoMetadata.initProjectMetadata(db, project.getName(), description, null, user, uniqueDeviceId);
            DaoMetadata.setValue(db, TableDescriptions.MetadataTableDefaultValues.KEY_CREATIONTS.getFieldName(), String.valueOf(project.getCreatedAt().getTime()));
            DaoMetadata.insertNewItem(db, CardinalMetadataTableDefaultValues.PROJECT_ID.getFieldName(), CardinalMetadataTableDefaultValues.PROJECT_ID.getFieldLabel(), String.valueOf(project.getId()));
            DaoMetadata.insertNewItem(db, CardinalMetadataTableDefaultValues.WORK_SESSION_ID.getFieldName(), CardinalMetadataTableDefaultValues.WORK_SESSION_ID.getFieldLabel(), "");
            DaoMetadata.insertNewItem(db, CardinalMetadataTableDefaultValues.MAP_OBJECT_TYPE_ID.getFieldName(), CardinalMetadataTableDefaultValues.MAP_OBJECT_TYPE_ID.getFieldLabel(), "");
            DaoMetadata.insertNewItem(db, CardinalMetadataTableDefaultValues.CURRENT_MAP_OBJECT_ID.getFieldName(), CardinalMetadataTableDefaultValues.CURRENT_MAP_OBJECT_ID.getFieldLabel(), "");
            DaoMetadata.insertNewItem(db, CardinalMetadataTableDefaultValues.NETWORK_ID.getFieldName(), CardinalMetadataTableDefaultValues.NETWORK_ID.getFieldLabel(), "");

            //Inserts Operations
            WorkerOperations.getInstance().insertAll(workerList);
            Worker currentWorker = WorkerOperations.getInstance().findOneBy(user);
            DevicesOperations.getInstance().insert(new Devices(null, uniqueDeviceId, String.format("%s Mobile", currentWorker.getFirstName()), cu.phibrain.plugins.cardinal.io.utils.Utilities.getOSVersion(), new Date(), currentWorker.getId()));
            ContractOperations.getInstance().insertAll(contractList);
            StockOperations.getInstance().insertAll(stockList);
            ZoneOperations.getInstance().insertAll(zoneList);
            NetworksOperations.getInstance().insertAll(networksList);
            LabelBatchesOperations.getInstance().insertAll(labelBatchesList);
            LabelOperations.getInstance().insertAll(labelList);
            ProjectConfigOperations.getInstance().insertAll(configList);

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

                    long gpsLogId = cu.phibrain.plugins.cardinal.io.utils.Utilities.addGpsLog(db, now, now, 0, logName, DEFAULT_LOG_WIDTH, ColorUtilities.RED.getHex(), true); //$NON-NLS-1$
                    db.beginTransaction();
                    try {
                        for (WorkerRoute log :
                                gpslogs) {
                            dbHelper.addGpsLogDataPoint(db, gpsLogId, log.getLongitude(), log.getLatitude(), log.getAltitude(), log.getCreatedAt().getTime());
                        }
                        db.setTransactionSuccessful();
                    } finally {
                        db.endTransaction();
                    }

                    //save current log
                    WorkerRouteOperations.getInstance().save(new WorkerRoute(null, null, session.getId(), gpsLogId, new Date(now), true, new Date(now), (long) gpslogs.size(), false));
                }
            }


            MapObjectOperations.getInstance().insertAll(mapObjectList);
            //Get and save all object in mapObjects
            List<MapObjectHasDefect> mapObjectHasDefectList = new ArrayList<>();
            for (MapObject mapObject : mapObjectList) {
                RouteSegmentOperations.getInstance().insertAll(mapObject.getRouteSegments());
                MapObjectHasStateOperations.getInstance().insertAll(mapObject.getStates());
                if (downloadImages) {
                    MapObjectImagesOperations.getInstance().insertAll(mapObject.getImages());
                }
                MapObjectMetadataOperations.getInstance().insertAll(mapObject.getMetadata());
                mapObjectHasDefectList.addAll(mapObject.getDefects());

            }
            MapObjectHasDefectOperations.getInstance().insertAll(mapObjectHasDefectList);

            if (downloadImages) {
                for (MapObjectHasDefect mapObjectdefect : mapObjectHasDefectList) {
                    MapObjectHasDefectHasImagesOperations.getInstance().insertAll(mapObjectdefect.getImages());
                }
            }

            long fileLength = downloadedProjectFile.length();
            if (fileLength == 0) {
                throw new DownloadError("Error downloading project from cloud server.");
            }

            return downloadedProjectFile.getCanonicalPath();
        } catch (Exception e) {
            GPLog.error(this, null, e);
            APIError error = null;
            if (GsonHelper.isJSONValid(e.getMessage())) {
                error = GsonHelper.createPojoFromString(e.getMessage(), APIError.class);
                throw new DownloadError(NetworkUtilities.getMessageForCode(context, error.status(), null));

            } else {
                error = new APIError(500, e.getMessage());
                throw new DownloadError(error.message());
            }
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
