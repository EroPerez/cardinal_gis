package cu.phibrain.plugins.cardinal.io.database.entity.model.converter;


import org.greenrobot.greendao.converter.PropertyConverter;

import cu.phibrain.plugins.cardinal.io.database.entity.model.SignalEvents.SignalTypes;

public  class SignalTypesConverter implements PropertyConverter<SignalTypes, Integer> {
    @Override
    public SignalTypes convertToEntityProperty(Integer databaseValue) {
        if (databaseValue == null) {
            return null;
        }

        return SignalTypes.fromId(databaseValue);
    }

    @Override
    public Integer convertToDatabaseValue(SignalTypes entityProperty) {
        return entityProperty == null ? null : entityProperty.getId();
    }
}
