package cu.phibrain.plugins.cardinal.io.utils;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import java.io.IOException;
import java.lang.reflect.Field;

import eu.geopaparazzi.core.GeopaparazziApplication;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.database.TableDescriptions;
import eu.geopaparazzi.library.util.TimeUtilities;

import static eu.geopaparazzi.library.database.TableDescriptions.TABLE_GPSLOGS;
import static eu.geopaparazzi.library.database.TableDescriptions.TABLE_GPSLOG_PROPERTIES;

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

    public static long addGpsLog(SQLiteDatabase sqliteDatabase, long startTs, long endTs, double lengthm, String text, float width, String color, boolean visible)
            throws IOException {

        if (sqliteDatabase == null)
            sqliteDatabase = GeopaparazziApplication.getInstance().getDatabase();

        sqliteDatabase.beginTransaction();
        long rowId;
        try {
            // add new log
            ContentValues values = new ContentValues();
            values.put(TableDescriptions.GpsLogsTableFields.COLUMN_LOG_STARTTS.getFieldName(), startTs);
            values.put(TableDescriptions.GpsLogsTableFields.COLUMN_LOG_ENDTS.getFieldName(), endTs);
            if (text == null) {
                text = "log_" + TimeUtilities.INSTANCE.TIMESTAMPFORMATTER_LOCAL.format(new java.util.Date(startTs));
            }
            values.put(TableDescriptions.GpsLogsTableFields.COLUMN_LOG_LENGTHM.getFieldName(), lengthm);
            values.put(TableDescriptions.GpsLogsTableFields.COLUMN_LOG_TEXT.getFieldName(), text);
            values.put(TableDescriptions.GpsLogsTableFields.COLUMN_LOG_ISDIRTY.getFieldName(), 1);
            rowId = sqliteDatabase.insertOrThrow(TABLE_GPSLOGS, null, values);

            // and some default properties
            ContentValues propValues = new ContentValues();
            propValues.put(TableDescriptions.GpsLogsPropertiesTableFields.COLUMN_LOGID.getFieldName(), rowId);
            propValues.put(TableDescriptions.GpsLogsPropertiesTableFields.COLUMN_PROPERTIES_COLOR.getFieldName(), color);
            propValues.put(TableDescriptions.GpsLogsPropertiesTableFields.COLUMN_PROPERTIES_WIDTH.getFieldName(), width);
            propValues.put(TableDescriptions.GpsLogsPropertiesTableFields.COLUMN_PROPERTIES_VISIBLE.getFieldName(), visible ? 1 : 0);
            sqliteDatabase.insertOrThrow(TABLE_GPSLOG_PROPERTIES, null, propValues);

            sqliteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            GPLog.error("DAOGPSLOG", e.getLocalizedMessage(), e);
            throw new IOException(e.getLocalizedMessage());
        } finally {
            sqliteDatabase.endTransaction();
        }
        return rowId;
    }
}
