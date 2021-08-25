package cu.phibrain.plugins.cardinal.io.utils;

import android.os.Build;

import java.lang.reflect.Field;

public class Utilities {

    public static String getOSName() {
        StringBuilder builder = new StringBuilder();
        builder.append("android : ").append(Build.VERSION.RELEASE);

        Field[] fields = Build.VERSION_CODES.class.getFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            int fieldValue = -1;

            try {
                fieldValue = field.getInt(new Object());
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

            if (fieldValue == Build.VERSION.SDK_INT) {
                builder.append(" : ").append(fieldName).append(" : ");
                builder.append("sdk=").append(fieldValue);
            }
        }

        // android : 4.1.1 : JELLY_BEAN : sdk=16
        return builder.toString();
    }

    public static String getOSVersion() {
        return Build.VERSION.RELEASE;
    }
}
