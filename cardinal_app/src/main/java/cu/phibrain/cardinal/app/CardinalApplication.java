package cu.phibrain.cardinal.app;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;

import java.io.IOException;

import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.cardinal.app.ui.layer.CardinalLayerManager;
import cu.phibrain.plugins.cardinal.io.database.base.DaoSessionManager;
import cu.phibrain.plugins.cardinal.io.database.entity.events.MapObjectEntityEventListener;
import cu.phibrain.plugins.cardinal.io.database.entity.events.MapObjectHasDefectEventListener;
import cu.phibrain.plugins.cardinal.io.database.entity.events.RouteSegmentEntityEventListener;
import cu.phibrain.plugins.cardinal.io.database.entity.model.DaoSession;
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
import eu.geopaparazzi.core.GeopaparazziApplication;
import eu.geopaparazzi.library.core.ResourcesManager;
import eu.geopaparazzi.library.profiles.ProfilesHandler;


public class CardinalApplication extends GeopaparazziApplication {
    private DaoSession daoSession;
    private static Context context;

    public CardinalApplication() {
    }


    public static Context getContext() {
        return context;
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    public AppContainer appContainer;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        try {
            DaoSessionManager.getInstance().setContext(context);
            DaoSessionManager.getInstance().setDatabaseName(this.getDatabase().getPath());
            daoSession = DaoSessionManager.getInstance().getDaoSession();

            //Attach observer to subject
            ProjectOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            WorkerOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            ContractOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            StockOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            ZoneOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            NetworksOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            LayerOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            MapObjecTypeOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            MapObjectTypeAttributeOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            MapObjecTypeDefectOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            MapObjecTypeStateOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            LabelBatchesOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            LabelMaterialOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            LabelSubLotOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            MapObjectHasDefectHasImagesOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            MapObjectHasDefectOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            MapObjectHasStateOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            MapObjectImagesOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            MapObjectMetadataOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            MapObjectOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            MaterialOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            RouteSegmentOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            SignalEventsOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            SupplierOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            TopologicalRuleOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            WorkerRouteOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            WorkSessionOperations.getInstance().attachTo(DaoSessionManager.getInstance());
            LabelOperations.getInstance().attachTo(DaoSessionManager.getInstance());

            appContainer = new AppContainer();
            ResourcesManager.resetManager();
            ProfilesHandler.INSTANCE.checkActiveProfile(getContentResolver());
            if (appContainer.getProjectActive() != null)
                CardinalLayerManager.INSTANCE.init();

            Log.i("GEOPAPARAZZIAPPLICATION", "ACRA Initialized.");

            // Register entity event listener to handling cascade delete and order related task
            MapObjectOperations.getInstance().registerEventDispatcher(new MapObjectEntityEventListener());
            RouteSegmentOperations.getInstance().registerEventDispatcher(new RouteSegmentEntityEventListener());
            MapObjectHasDefectOperations.getInstance().registerEventDispatcher(new MapObjectHasDefectEventListener());

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
