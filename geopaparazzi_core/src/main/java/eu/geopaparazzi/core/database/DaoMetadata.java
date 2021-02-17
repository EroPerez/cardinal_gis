/*
 * Geopaparazzi - Digital field mapping on Android based devices
 * Copyright (C) 2016  HydroloGIS (www.hydrologis.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.geopaparazzi.core.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import eu.geopaparazzi.core.GeopaparazziApplication;
import eu.geopaparazzi.core.database.objects.Metadata;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.database.TableDescriptions;
import eu.geopaparazzi.library.util.TimeUtilities;

import static eu.geopaparazzi.library.database.TableDescriptions.MetadataTableDefaultValues;
import static eu.geopaparazzi.library.database.TableDescriptions.MetadataTableFields;
import static eu.geopaparazzi.library.database.TableDescriptions.TABLE_METADATA;

/**
 * @author Andrea Antonello (www.hydrologis.com)
 */
@SuppressWarnings("nls")
public class DaoMetadata {

    private static SimpleDateFormat datesFormatter = TimeUtilities.INSTANCE.TIME_FORMATTER_LOCAL;
    public static final String EMPTY_VALUE = " - ";
    private static String projectName;

    /**
     * Create the notes tables.
     *
     * @throws java.io.IOException if something goes wrong.
     */
    public static void createTables(SQLiteDatabase sqliteDatabase) throws IOException {
        StringBuilder sB = new StringBuilder();
        sB.append("CREATE TABLE ");
        sB.append(TABLE_METADATA);
        sB.append(" (");
        sB.append(MetadataTableFields.COLUMN_KEY.getFieldName()).append(" TEXT NOT NULL, ");
        sB.append(MetadataTableFields.COLUMN_LABEL.getFieldName()).append(" TEXT , ");
        sB.append(MetadataTableFields.COLUMN_VALUE.getFieldName()).append(" TEXT NOT NULL ");
        sB.append(");");
        String CREATE_TABLE_PROJECT = sB.toString();

        if (sqliteDatabase == null)
            sqliteDatabase = GeopaparazziApplication.getInstance().getDatabase();
        if (GPLog.LOG_HEAVY)
            Log.i("DaoProject", "Create the project table with: \n" + CREATE_TABLE_PROJECT);

        sqliteDatabase.beginTransaction();
        try {
            sqliteDatabase.execSQL(CREATE_TABLE_PROJECT);

            sqliteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DaoProject", e.getLocalizedMessage(), e);
            throw new IOException(e.getLocalizedMessage());
        } finally {
            sqliteDatabase.endTransaction();
        }
    }


    /**
     * Populate the project metadata table.
     *
     * @param name         the project name
     * @param description  an optional description.
     * @param notes        optional notes.
     * @param creationUser the user creating the project.
     * @throws java.io.IOException if something goes wrong.
     */
    public static void initProjectMetadata(SQLiteDatabase sqliteDatabase, String name, String description, String notes, String creationUser, String uniqueId) throws IOException {
        Date creationDate = new Date();
        if (name == null) {
            name = "";
        }
        if (description == null) {
            description = EMPTY_VALUE;
        }
        if (notes == null) {
            notes = EMPTY_VALUE;
        }
        if (creationUser == null) {
            creationUser = "dummy user";
        }

        if (sqliteDatabase == null)
            sqliteDatabase = GeopaparazziApplication.getInstance().getDatabase();
        sqliteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(MetadataTableFields.COLUMN_KEY.getFieldName(), MetadataTableDefaultValues.KEY_NAME.getFieldName());
            values.put(MetadataTableFields.COLUMN_LABEL.getFieldName(), MetadataTableDefaultValues.KEY_NAME.getFieldLabel());
            values.put(MetadataTableFields.COLUMN_VALUE.getFieldName(), name);
            sqliteDatabase.insertOrThrow(TABLE_METADATA, null, values);

            values = new ContentValues();
            values.put(MetadataTableFields.COLUMN_KEY.getFieldName(), MetadataTableDefaultValues.KEY_DESCRIPTION.getFieldName());
            values.put(MetadataTableFields.COLUMN_LABEL.getFieldName(), MetadataTableDefaultValues.KEY_DESCRIPTION.getFieldLabel());
            values.put(MetadataTableFields.COLUMN_VALUE.getFieldName(), description);
            sqliteDatabase.insertOrThrow(TABLE_METADATA, null, values);

            values = new ContentValues();
            values.put(MetadataTableFields.COLUMN_KEY.getFieldName(), MetadataTableDefaultValues.KEY_NOTES.getFieldName());
            values.put(MetadataTableFields.COLUMN_LABEL.getFieldName(), MetadataTableDefaultValues.KEY_NOTES.getFieldLabel());
            values.put(MetadataTableFields.COLUMN_VALUE.getFieldName(), notes);
            sqliteDatabase.insertOrThrow(TABLE_METADATA, null, values);

            values = new ContentValues();
            values.put(MetadataTableFields.COLUMN_KEY.getFieldName(), MetadataTableDefaultValues.KEY_CREATIONTS.getFieldName());
            values.put(MetadataTableFields.COLUMN_LABEL.getFieldName(), MetadataTableDefaultValues.KEY_CREATIONTS.getFieldLabel());
            values.put(MetadataTableFields.COLUMN_VALUE.getFieldName(), datesFormatter.format(creationDate));
            sqliteDatabase.insertOrThrow(TABLE_METADATA, null, values);

            values = new ContentValues();
            values.put(MetadataTableFields.COLUMN_KEY.getFieldName(), MetadataTableDefaultValues.KEY_LASTTS.getFieldName());
            values.put(MetadataTableFields.COLUMN_LABEL.getFieldName(), MetadataTableDefaultValues.KEY_LASTTS.getFieldLabel());
            values.put(MetadataTableFields.COLUMN_VALUE.getFieldName(), EMPTY_VALUE);
            sqliteDatabase.insertOrThrow(TABLE_METADATA, null, values);

            values = new ContentValues();
            values.put(MetadataTableFields.COLUMN_KEY.getFieldName(), MetadataTableDefaultValues.KEY_CREATIONUSER.getFieldName());
            values.put(MetadataTableFields.COLUMN_LABEL.getFieldName(), MetadataTableDefaultValues.KEY_CREATIONUSER.getFieldLabel());
            values.put(MetadataTableFields.COLUMN_VALUE.getFieldName(), creationUser);
            sqliteDatabase.insertOrThrow(TABLE_METADATA, null, values);

            values = new ContentValues();
            values.put(MetadataTableFields.COLUMN_KEY.getFieldName(), MetadataTableDefaultValues.KEY_LASTUSER.getFieldName());
            values.put(MetadataTableFields.COLUMN_LABEL.getFieldName(), MetadataTableDefaultValues.KEY_LASTUSER.getFieldLabel());
            values.put(MetadataTableFields.COLUMN_VALUE.getFieldName(), EMPTY_VALUE);
            sqliteDatabase.insertOrThrow(TABLE_METADATA, null, values);

            if (uniqueId != null) {
                values = new ContentValues();
                values.put(MetadataTableFields.COLUMN_KEY.getFieldName(), MetadataTableDefaultValues.KEY_DEVICEID.getFieldName());
                values.put(MetadataTableFields.COLUMN_LABEL.getFieldName(), MetadataTableDefaultValues.KEY_DEVICEID.getFieldLabel());
                values.put(MetadataTableFields.COLUMN_VALUE.getFieldName(), uniqueId);
                sqliteDatabase.insertOrThrow(TABLE_METADATA, null, values);
            }
            sqliteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            GPLog.error("DaoMetadata", e.getLocalizedMessage(), e);
            throw new IOException(e.getLocalizedMessage());
        } finally {
            sqliteDatabase.endTransaction();
        }
    }

    /**
     * Insert a new metadata item.
     *
     * @param key   the key to use.
     * @param label a readable label for the item. if null, the key is used.
     * @param value a value of the item. It can be "" but not null.
     * @throws IOException
     */
    public static void insertNewItem(String key, String label, String value) throws IOException {
        SQLiteDatabase sqliteDatabase = GeopaparazziApplication.getInstance().getDatabase();
        sqliteDatabase.beginTransaction();
        try {
            if (label == null) label = key;
            ContentValues values = new ContentValues();
            values.put(MetadataTableFields.COLUMN_KEY.getFieldName(), key);
            values.put(MetadataTableFields.COLUMN_LABEL.getFieldName(), label);
            values.put(MetadataTableFields.COLUMN_VALUE.getFieldName(), value);
            sqliteDatabase.insertOrThrow(TABLE_METADATA, null, values);

            sqliteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            sqliteDatabase.endTransaction();
            GPLog.error("DaoMetadata", e.getLocalizedMessage(), e);
            // try the old way
            oldInsertNewItem(key, value);
        }
    }

    /**
     * Old insert a new metadata item.
     *
     * @param key   the key to use.
     * @param value a value of the item. It can be "" but not null.
     * @throws IOException
     * @deprecated only for backwards compatibility.
     */
    private static void oldInsertNewItem(String key, String value) throws IOException {
        SQLiteDatabase sqliteDatabase = GeopaparazziApplication.getInstance().getDatabase();
        sqliteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(MetadataTableFields.COLUMN_KEY.getFieldName(), key);
            values.put(MetadataTableFields.COLUMN_VALUE.getFieldName(), value);
            sqliteDatabase.insertOrThrow(TABLE_METADATA, null, values);

            sqliteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            GPLog.error("DaoMetadata", e.getLocalizedMessage(), e);
            throw new IOException(e.getLocalizedMessage());
        } finally {
            sqliteDatabase.endTransaction();
        }
    }

    /**
     * Delete a metadata item.
     *
     * @param key the key to use.
     * @throws IOException
     */
    public static void deleteItem(String key) throws IOException {
        SQLiteDatabase sqliteDatabase = GeopaparazziApplication.getInstance().getDatabase();
        sqliteDatabase.beginTransaction();
        try {
            String where = MetadataTableFields.COLUMN_KEY.getFieldName() + "='" + key + "'";
            sqliteDatabase.delete(TABLE_METADATA, where, null);

            sqliteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            GPLog.error("DaoMetadata", e.getLocalizedMessage(), e);
            throw new IOException(e.getLocalizedMessage());
        } finally {
            sqliteDatabase.endTransaction();
        }
    }

    /**
     * Set a value of the metadata.
     *
     * @param key   the key to use (from {@link TableDescriptions.MetadataTableFields}).
     * @param value the value to set.
     * @throws java.io.IOException if something goes wrong.
     */
    public static void setValue(String key, String value) throws IOException {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(MetadataTableFields.COLUMN_VALUE.getFieldName(), value);

        String where = MetadataTableFields.COLUMN_KEY.getFieldName() + "='" + key + "'";

        SQLiteDatabase sqliteDatabase = GeopaparazziApplication.getInstance().getDatabase();
        sqliteDatabase.update(TABLE_METADATA, updatedValues, where, null);
    }

    /**
     * Get the metadata.
     *
     * @return the map of metadata.
     * @throws java.io.IOException if something goes wrong.
     */
    public static List<Metadata> getProjectMetadata() throws IOException {
        SQLiteDatabase sqliteDatabase = GeopaparazziApplication.getInstance().getDatabase();
        List<Metadata> metadataList = new ArrayList<>();

        try {
            String[] asColumnsToReturn = { //
                    MetadataTableFields.COLUMN_KEY.getFieldName(), //
                    MetadataTableFields.COLUMN_LABEL.getFieldName(), //
                    MetadataTableFields.COLUMN_VALUE.getFieldName()
            };// ,
            Cursor c = sqliteDatabase.query(TABLE_METADATA, asColumnsToReturn, null, null, null, null, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String key = c.getString(0);
                String label = c.getString(1);
                String value = c.getString(2);

                Metadata m = new Metadata();
                m.key = key;
                m.label = label;
                m.value = value;
                metadataList.add(m);

                c.moveToNext();
            }
            c.close();
        } catch (Exception e) {
            // try the old way
            return oldGetProjectMetadata();
        }
        return metadataList;
    }

    /**
     * Old get the metadata.
     *
     * @return the map of metadata.
     * @throws java.io.IOException if something goes wrong.
     * @deprecated only for backwards compatibility.
     */
    private static List<Metadata> oldGetProjectMetadata() throws IOException {
        SQLiteDatabase sqliteDatabase = GeopaparazziApplication.getInstance().getDatabase();
        List<Metadata> metadataList = new ArrayList<>();

        try {
            String[] asColumnsToReturn = { //
                    MetadataTableFields.COLUMN_KEY.getFieldName(), //
                    MetadataTableFields.COLUMN_VALUE.getFieldName()
            };// ,
            Cursor c = sqliteDatabase.query(TABLE_METADATA, asColumnsToReturn, null, null, null, null, null);
            c.moveToFirst();
            while (!c.isAfterLast()) {
                String key = c.getString(0);
                String value = c.getString(1);

                Metadata m = new Metadata();
                m.key = key;
                m.label = key;
                m.value = value;
                metadataList.add(m);

                c.moveToNext();
            }
            c.close();
        } catch (Exception e) {
            GPLog.error("DaoMetadata", null, e);
        }
        return metadataList;
    }


    public static String getProjectName() throws IOException {
        SQLiteDatabase sqliteDatabase = GeopaparazziApplication.getInstance().getDatabase();

        Cursor c = null;
        try {
            String[] asColumnsToReturn = { //
                    MetadataTableFields.COLUMN_VALUE.getFieldName()
            };// ,
            c = sqliteDatabase.query(TABLE_METADATA, asColumnsToReturn, MetadataTableFields.COLUMN_KEY.getFieldName() + "='" + MetadataTableDefaultValues.KEY_NAME.getFieldName() + "'", null, null, null, null);
            c.moveToFirst();
            if (!c.isAfterLast()) {
                String value = c.getString(0);
                return value;
            }
            return null;
        } finally {
            if (c != null)
                c.close();
        }

    }
}
