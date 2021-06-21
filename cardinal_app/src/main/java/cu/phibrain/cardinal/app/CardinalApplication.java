package cu.phibrain.cardinal.app;

import android.content.Context;
import android.util.Log;
import org.json.JSONException;
import java.io.IOException;
import cu.phibrain.cardinal.app.ui.layer.CardinalLayerManager;
import cu.phibrain.plugins.cardinal.io.database.base.DaoSessionManager;
import cu.phibrain.plugins.cardinal.io.database.entity.ContractOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.LabelBatchesOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.LabelMaterialOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.LabelSubLotOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.LayerOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.MapObjecTypeDefectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.MapObjecTypeOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.MapObjecTypeStateOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.MapObjectHasDefectHasImagesOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.MapObjectHasDefectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.MapObjectHasStateOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.MapObjectImagesOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.MapObjectMetadataOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.MapObjectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.MapObjectTypeAttributeOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.MaterialOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.NetworksOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.ProjectOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.RouteSegmentOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.SignalEventsOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.StockOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.SupplierOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.TopologicalRuleOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.WorkSessionOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.WorkerOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.WorkerRouteOperations;
import cu.phibrain.plugins.cardinal.io.database.entity.ZoneOperations;
import cu.phibrain.cardinal.app.injections.AppContainer;
import cu.phibrain.plugins.cardinal.io.model.DaoSession;
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
    public AppContainer appContainer ;
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

            appContainer = new AppContainer();
            ResourcesManager.resetManager();
            ProfilesHandler.INSTANCE.checkActiveProfile(getContentResolver());
            if(appContainer.getProjectActive() !=null)
                CardinalLayerManager.INSTANCE.init();

            Log.i("GEOPAPARAZZIAPPLICATION", "ACRA Initialized.");
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
