package cu.phibrain.plugins.cardinal.io.ui.menu;

import android.content.Context;
import android.view.ViewGroup;

import java.util.List;

/*import cu.phibrain.plugins.cardinal.io.R;
import cu.phibrain.plugins.cardinal.io.database.entity.LayerOperations;
import cu.phibrain.plugins.cardinal.io.model.GroupOfLayer;
import cu.phibrain.plugins.cardinal.io.model.Layer;
import cu.phibrain.plugins.cardinal.io.model.MapObjecType;
import eu.geopaparazzi.core.database.DaoMetadata;
import eu.geopaparazzi.core.database.objects.Metadata;
import eu.geopaparazzi.library.database.TableDescriptions;*/

public class UtilTools {

    private static void loadMapObjectType(TreeNodeMenuUI parent, Long layer_id){

    }

   /* private static TreeNodeMenuUI LoadChild(TreeNodeMenuUI node){
        //if(node.paren)
    }*/

    public static void loadDataProgect(Context context, ViewGroup layout) {
        /*try {
            Context context2 = context;
            ViewGroup viewGroup = layout;
            MapObjectTypeUI rootMOT = new MapObjectTypeUI(R.drawable.ic_mapview_mot_parent_24dp, R.color.gpsred_fill, Long.parseLong("1"));
            ImgButtonMapObjectTypeUI fbMotUI = new ImgButtonMapObjectTypeUI(context2, rootMOT);
            TreeNodeMenuUI nodeMenuUIRoot = new TreeNodeMenuUI(fbMotUI);
            nodeMenuUIRoot.getDataUi().themeParent();
            nodeMenuUIRoot.getDataUi().openFab();
            List<Metadata> projectMetadata = DaoMetadata.getProjectMetadata();
            int fieldProyect=  TableDescriptions.MetadataTableDefaultValues.PROJECT_ID.ordinal();
            Metadata project_meta_id = projectMetadata.get(fieldProyect);
            String project_id = project_meta_id.value;

            List<GroupOfLayer> groupLayer = GroupOfLayerOperations.getInstance().getGroupListByProject(Long.parseLong(project_id));
            for (final GroupOfLayer group : groupLayer) {
                  List<Layer> layers= LayerOperations.getInstance().getLayerByLeyerGroup(group.getId());
                  ImgButtonMapObjectTypeUI fbLayerGroup = new ImgButtonMapObjectTypeUI(context2, new MapObjectTypeUI(R.drawable.ic_mapview_mot_children_24dp, R.color.main_decorations, group.getId()));
                  TreeNodeMenuUI groupUi = new TreeNodeMenuUI(fbLayerGroup);
                for (final Layer layer : layers) {
                    ImgButtonMapObjectTypeUI fbLayer = new ImgButtonMapObjectTypeUI(context2, new MapObjectTypeUI(R.drawable.ic_mapview_mot_children_24dp, R.color.main_decorations, layer.getId()));
                    TreeNodeMenuUI layerUi = new TreeNodeMenuUI(fbLayer);
                    viewGroup.addView(layerUi.getDataUi());
                    //map object
                    List<MapObjecType> mapObjectListByLayer = layer.getMapobjectypes();
                    for (final MapObjecType mot : mapObjectListByLayer) {
                        ImgButtonMapObjectTypeUI fbMot = new ImgButtonMapObjectTypeUI(context2, new MapObjectTypeUI(R.drawable.ic_mapview_mot_children_24dp, R.color.design_default_color_error, mot.getId()));
                        TreeNodeMenuUI motUi = new TreeNodeMenuUI(fbMot);
                        viewGroup.addView(motUi.getDataUi());
                        layerUi.addChild(fbMot);
                    }
                    groupUi.addChild(layerUi);
                }
                nodeMenuUIRoot.addChild(groupUi);
                viewGroup.addView(groupUi.getDataUi());
            }
            viewGroup.addView(nodeMenuUIRoot.getDataUi());
            nodeMenuUIRoot.hideBrother();
        }
        catch(Exception ex){

        }*/
    }

}
