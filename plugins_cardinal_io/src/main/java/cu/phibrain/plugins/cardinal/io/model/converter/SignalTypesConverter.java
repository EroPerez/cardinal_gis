package cu.phibrain.plugins.cardinal.io.model.converter;


import org.greenrobot.greendao.converter.PropertyConverter;

import cu.phibrain.plugins.cardinal.io.model.SignalEvents.SignalTypes;

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
