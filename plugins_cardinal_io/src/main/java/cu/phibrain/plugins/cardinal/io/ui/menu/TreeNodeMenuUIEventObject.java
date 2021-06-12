package cu.phibrain.plugins.cardinal.io.ui.menu;

import java.util.EventObject;

public class TreeNodeMenuUIEventObject extends EventObject {
    public TreeNodeMenuUI treeNode;

    public TreeNodeMenuUIEventObject(Object source, TreeNodeMenuUI treeNode2) {
        super(source);
        this.treeNode = treeNode2;
    }

    public TreeNodeMenuUI getTreeNode() {
        return this.treeNode;
    }
}
