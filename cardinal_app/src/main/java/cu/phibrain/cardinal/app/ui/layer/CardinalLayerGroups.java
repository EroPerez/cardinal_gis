package cu.phibrain.cardinal.app.ui.layer;

public enum CardinalLayerGroups {
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
