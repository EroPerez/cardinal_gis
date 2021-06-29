package cu.phibrain.plugins.cardinal.io.database.entity.model.converter;

import android.util.Base64;

import org.greenrobot.greendao.converter.PropertyConverter;

public class Base64ImageTypeConverter implements PropertyConverter<byte[], String> {

    @Override
    public byte[] convertToEntityProperty(String databaseValue) {

        byte[] decodedByte = Base64.decode(databaseValue, 0);
        return decodedByte;
    }

    @Override
    public String convertToDatabaseValue(byte[] entityProperty) {
        return null;
    }
}
