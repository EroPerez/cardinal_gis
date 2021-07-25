package cu.phibrain.plugins.cardinal.io.utils;

public enum CardinalMetadataTableDefaultValues {
    /**
     * The project key to use.
     */
    PROJECT_ID("projectId", "Project Id", String.class),
    WORK_SESSION_ID("work_session_id", "Work Session Active", String.class),
    MAP_OBJECT_TYPE_ID("map_object_type_id", "Map Object Type Active", String.class),
    CURRENT_MAP_OBJECT_ID("current_map_object_id", "Current Map Object", String.class),
    PREVIOUS_MAP_OBJECT_ID("previous_map_object_id", "Previous Map Object", String.class),
    NETWORK_ID("network_id", "Network Active", String.class);

    private String fieldLabel;
    private String fieldName;
    private Class fieldClass;

    CardinalMetadataTableDefaultValues(String fieldName, String fieldLabel, Class fieldClass) {
        this.fieldName = fieldName;
        this.fieldLabel = fieldLabel;
        this.fieldClass = fieldClass;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getFieldLabel() {
        return fieldLabel;
    }

    public Class getFieldClass() {
        return fieldClass;
    }

}
