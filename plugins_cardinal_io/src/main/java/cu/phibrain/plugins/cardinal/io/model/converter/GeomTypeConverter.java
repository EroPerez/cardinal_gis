package cu.phibrain.plugins.cardinal.io.model.converter;


import org.greenrobot.greendao.converter.PropertyConverter;

import cu.phibrain.plugins.cardinal.io.model.MapObjecType;

public  class GeomTypeConverter implements PropertyConverter<MapObjecType.GeomType, Integer> {
    @Override
    public MapObjecType.GeomType convertToEntityProperty(Integer databaseValue) {
        if (databaseValue == null) {
            return null;
        }

        return MapObjecType.GeomType.fromId(databaseValue);
    }

    @Override
    public Integer convertToDatabaseValue(MapObjecType.GeomType entityProperty) {
        return entityProperty == null ? null : entityProperty.getId();
    }
}
