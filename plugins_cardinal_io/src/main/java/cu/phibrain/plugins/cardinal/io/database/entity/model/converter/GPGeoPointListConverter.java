package cu.phibrain.plugins.cardinal.io.database.entity.model.converter;

import com.google.gson.reflect.TypeToken;

import org.greenrobot.greendao.converter.PropertyConverter;

import java.lang.reflect.Type;
import java.util.List;

import cu.phibrain.plugins.cardinal.io.utils.GsonHelper;
import eu.geopaparazzi.map.GPGeoPoint;

public class GPGeoPointListConverter implements PropertyConverter<List<GPGeoPoint>, String> {


    @Override
    public List<GPGeoPoint> convertToEntityProperty(String s) {
        Type listType = new TypeToken<List<GPGeoPoint>>() {
        }.getType();

        return GsonHelper.createPojoFromString(s, listType);
    }

    @Override
    public String convertToDatabaseValue(List<GPGeoPoint> gpGeoPoints) {

        return gpGeoPoints == null ? null : GsonHelper.createJSONStringFromPojo(gpGeoPoints);
    }


}
