package cu.phibrain.plugins.cardinal.io.utils;

public enum CardinalMetadataTableDefaultValues {
    /**
     * The project key to use.
     */
    PROJECT_ID("projectId", "Project Id", String.class);

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
