package cu.phibrain.plugins.cardinal.io.ui.menu;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;

import eu.geopaparazzi.core.database.DaoNotes;
import cu.phibrain.plugins.cardinal.io.ui.map.MapviewActivity;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.util.PositionUtilities;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import cu.phibrain.plugins.cardinal.io.ui.layer.EdgesLayer;
import cu.phibrain.plugins.cardinal.io.ui.layer.MapObjectLayer;

public class TreeNodeMenuUI implements TreeNodeMenuUIEventListener {
    private List<TreeNodeMenuUI> children = new ArrayList();
    private ImgButtonMapObjectTypeUI dataUi = null;
    private ArrayList listeners;
    private TreeNodeMenuUI parent = null;
    private boolean state = false;

    public TreeNodeMenuUI(ImgButtonMapObjectTypeUI dataUi2) {
        this.dataUi = dataUi2;
        ArrayList arrayList = new ArrayList();
        this.listeners = arrayList;
        arrayList.add(this);
        dataUi2.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                TreeNodeMenuUI.this.onTreeNodeMenu();
            }
        });
    }

    /* access modifiers changed from: private */
    public void onTreeNodeMenu() {
        ListIterator li = this.listeners.listIterator();
        while (li.hasNext()) {
            ((TreeNodeMenuUIEventListener) li.next()).onClickTreeNodeMenuUI(new TreeNodeMenuUIEventObject(this, this));
        }
    }

    public void addPersonaEventListener(TreeNodeMenuUIEventListener listener) {
        this.listeners.add(listener);
    }

    public TreeNodeMenuUI(ImgButtonMapObjectTypeUI dataUi2, TreeNodeMenuUI parent2) {
        this.dataUi = dataUi2;
        this.parent = parent2;
    }

    public List<TreeNodeMenuUI> getChildren() {
        return this.children;
    }

    public void setParent(TreeNodeMenuUI parent2) {
        this.parent = parent2;
    }

    public void addChild(ImgButtonMapObjectTypeUI data) {
        TreeNodeMenuUI child = new TreeNodeMenuUI(data);
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(TreeNodeMenuUI child) {
        child.setParent(this);
        this.children.add(child);
    }

    public ImgButtonMapObjectTypeUI getDataUi() {
        return this.dataUi;
    }

    public void setDataUi(ImgButtonMapObjectTypeUI dataUi2) {
        this.dataUi = dataUi2;
    }

    public boolean isRoot() {
        return this.parent == null;
    }

    public boolean isLeaf() {
        return this.children.size() == 0;
    }

    public void removeParent() {
        this.parent = null;
    }

    public void paintChildren() {
        int marginButton = 1;
        for (TreeNodeMenuUI childrenNode : getChildren()) {
            childrenNode.dataUi.themeChildren(marginButton);
            if (!childrenNode.isLeaf()) {
                childrenNode.dataUi.themeWhitChildren();
            }
            childrenNode.dataUi.openFab();
            marginButton++;
        }
    }

    public void hideBrother() {
        TreeNodeMenuUI treeNodeMenuUI = this.parent;
        if (treeNodeMenuUI != null) {
            for (TreeNodeMenuUI brotherNode : treeNodeMenuUI.getChildren()) {
                if (this.dataUi.getAdapterMot().getId() != brotherNode.dataUi.getAdapterMot().getId()) {
                    brotherNode.dataUi.closeFab();
                }
            }
        }
        this.dataUi.themeParent();
        this.dataUi.moveFab();
        paintChildren();
    }

    public void hideChildren() {
        if (this.parent != null) {
            for (TreeNodeMenuUI brotherNode : getChildren()) {
                brotherNode.dataUi.closeFab();
            }
        }
    }

    public boolean isState() {
        return this.state;
    }

    public void setState(boolean state2) {
        if (!isRoot()) {
            this.state = state2;
        }
    }

    public void onClickTreeNodeMenuUI(TreeNodeMenuUIEventObject args) {
        int zoom;
        if (isLeaf()) {
            double lon = -76.643069d;
            double lat = 20.377d;
            try {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this.dataUi.getContext());
                double[] mapCenter = PositionUtilities.getMapCenterFromPreferences(preferences, true, true);
                if (mapCenter != null) {
                    lat = mapCenter[1];
                    lon = mapCenter[0];
                }
                DaoNotes.addNote(lon, lat, 16.0d, System.currentTimeMillis(), String.valueOf(this.dataUi.getAdapterMot().getId()), "POI", "", null);
                MapviewActivity activity = (MapviewActivity) this.dataUi.getContext();
                activity.getMapView().reloadLayer(MapObjectLayer.class);

                DaoNotes.addNote(lon, lat, 16.0d, System.currentTimeMillis(), String.valueOf(this.dataUi.getAdapterMot().getId()), "POI", "", null);
                activity.getMapView().reloadLayer(EdgesLayer.class);


            } catch (Exception e) {
                GPLog.error(this, null, e);
            }
        } else if (!isRoot()) {
            if (isState()) {
                hideChildren();
                this.parent.hideBrother();
            } else {
                hideBrother();
            }
        }
        setState(!isState());
    }
}
