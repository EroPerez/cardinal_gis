package cu.phibrain.cardinal.app.ui.layer;

public enum CardinalLayerGroups {
    GROUP_MAPLAYERS(0, "maplayers"),//
    GROUP_PROJECTLAYERS(1, "projectlayers"),//
    GROUP_3D(2, "3dlayers"),//
    GROUP_SYSTEM_TOP(3, "systemtoplayers"),
    GROUP_CARDINALLAYERS(4,"cardinallayers");

    private final int groupId;
    private final String groupName;

    CardinalLayerGroups(int groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public int getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }
}
