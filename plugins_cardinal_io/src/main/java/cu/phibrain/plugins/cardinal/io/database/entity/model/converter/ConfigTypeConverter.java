package cu.phibrain.plugins.cardinal.io.database.entity.model.converter;


import org.greenrobot.greendao.converter.PropertyConverter;

import cu.phibrain.plugins.cardinal.io.database.entity.model.ProjectConfig;

public  class ConfigTypeConverter implements PropertyConverter<ProjectConfig.ConfigType, Integer> {
    @Override
    public ProjectConfig.ConfigType convertToEntityProperty(Integer databaseValue) {
        if (databaseValue == null) {
            return null;
        }

        return ProjectConfig.ConfigType.fromId(databaseValue);
    }

    @Override
    public Integer convertToDatabaseValue(ProjectConfig.ConfigType entityProperty) {
        return entityProperty == null ? null : entityProperty.getId();
    }
}
