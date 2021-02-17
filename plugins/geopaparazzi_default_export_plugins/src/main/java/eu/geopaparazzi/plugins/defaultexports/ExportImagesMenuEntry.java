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
 *////*
// * Geopaparazzi - Digital field mapping on Android based devices
// * Copyright (C) 2010  HydroloGIS (www.hydrologis.com)
// *
// * This program is free software: you can redistribute it and/or modify
// * it under the terms of the GNU General Public License as published by
// * the Free Software Foundation, either version 3 of the License, or
// * (at your option) any later version.
// *
// * This program is distributed in the hope that it will be useful,
// * but WITHOUT ANY WARRANTY; without even the implied warranty of
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// * GNU General Public License for more details.
// *
// * You should have received a copy of the GNU General Public License
// * along with this program.  If not, see <http://www.gnu.org/licenses/>.
// */
//package eu.geopaparazzi.spatialite.database.spatial.core.daos;
//
//import org.locationtech.jts.geom.Geometry;
//
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Locale;
//
//import eu.geopaparazzi.library.database.GPLog;
//import eu.geopaparazzi.library.features.Feature;
//import eu.geopaparazzi.map.utils.EDataType;
//import eu.geopaparazzi.spatialite.ISpatialiteTableAndFieldsNames;
//import eu.geopaparazzi.spatialite.database.spatial.SpatialiteSourcesManager;
//import eu.geopaparazzi.spatialite.database.spatial.core.databasehandlers.SpatialiteDatabaseHandler;
//import eu.geopaparazzi.spatialite.database.spatial.core.enums.GeometryType;
//import eu.geopaparazzi.spatialite.database.spatial.core.tables.SpatialVectorTable;
//import eu.geopaparazzi.spatialite.database.spatial.util.SpatialiteUtilities;
//import jsqlite.Database;
//import jsqlite.Exception;
//import jsqlite.Stmt;
//
//
///**
// * Spatialite support methods.
// * <p/>
// * This class should contain a more user oriented tasks API.
// *
// * @author Andrea Antonello (www.hydrologis.com)
// */
//@SuppressWarnings("nls")
//public class DaoSpatialite implements ISpatialiteTableAndFieldsNames {
//
//    /**
//     * Collects the fields of a given table.
//     * <p/>
//     * <br>- name of Field
//     * <br>- type of field as defined in Database
//     *
//     * @param database  the database to use.
//     * @param tableName name of table to read.
//     * @return the {@link HashMap} of fields: [name of field, type of field]
//     * @throws Exception if something goes wrong.
//     */
//    public static HashMap<String, String> collectTableFields(Database database, String tableName) throws Exception {
//
//        HashMap<String, String> fieldNamesToTypeMap = new LinkedHashMap<String, String>();
//        String s_sql_command = "pragma table_info('" + tableName + "')";
//        String tableType = "";
//        String sqlCreationString = "";
//        Stmt statement = null;
//        String name = "";
//        try {
//            statement = database.prepare(s_sql_command);
//            while (statement.step()) {
//                name = statement.column_string(1);
//                tableType = statement.column_string(2);
//                sqlCreationString = statement.column_string(5); // pk
//                // try to unify the data-types: varchar(??),int(11) mysql-syntax
//                if (tableType.contains("int("))
//                    tableType = "INTEGER";
//                if (tableType.contains("varchar("))
//                    tableType = "TEXT";
//                // pk: 0 || 1;Data-TypeTEXT || DOUBLE || INTEGER || REAL || DATE || BLOB ||
//                // geometry-types
//                fieldNamesToTypeMap.put(name, sqlCreationString + ";" + tableType.toUpperCase(Locale.US));
//            }
//        } catch (jsqlite.Exception e_stmt) {
//            GPLog.error("DAOSPATIALIE",
//                    "collectTableFields[" + tableName + "] sql[" + s_sql_command + "] db[" + database.getFilename()
//                            + "]", e_stmt
//            );
//        } finally {
//            if (statement != null) {
//                statement.close();
//            }
//        }
//        return fieldNamesToTypeMap;
//    }
//
//    /**
//     * Attemt to retrieve row-count and bounds for this geometry field.
//     *
//     * @param database       the db to use.
//     * @param tableName      the table of the db to use.
//     * @param geometryColumn the geometry field of the table to use.
//     * @return 'rows_count;min_x,min_y,max_x,max_y;datetimestamp_now'.
//     * @throws Exception if something goes wrong.
//     */
//    public static String getGeometriesBoundsString(Database database, String tableName, String geometryColumn)
//            throws Exception {
//        StringBuilder queryBuilder = new StringBuilder();
//        String s_vector_extent = "";
//        // return the format used in DaoSpatialite.checkDatabaseTypeAndValidity()
//        queryBuilder.append("SELECT count(");
//        queryBuilder.append(geometryColumn);
//        queryBuilder.append(")||';'||Min(MbrMinX(");
//        queryBuilder.append(geometryColumn);
//        queryBuilder.append("))||','||Min(MbrMinY(");
//        queryBuilder.append(geometryColumn);
//        queryBuilder.append("))||','||Max(MbrMaxX(");
//        queryBuilder.append(geometryColumn);
//        queryBuilder.append("))||','||Max(MbrMaxY(");
//        queryBuilder.append(geometryColumn);
//        queryBuilder.append("))||';'||strftime('%Y-%m-%dT%H:%M:%fZ','now')");
//        queryBuilder.append(" FROM \"");
//        queryBuilder.append(tableName);
//        queryBuilder.append("\" ;");
//        // ;617;7255796.59288944,246133.478270624,7395508.96772464,520956.218508861;2014-03-26T06:32:58.572Z
//        String s_select_bounds = queryBuilder.toString();
//        Stmt statement = null;
//        try {
//            statement = database.prepare(s_select_bounds);
//            if (statement.step()) {
//                if (statement.column_string(0) != null) { // The geometries may be null, thus
//                    // returns null
//                    s_vector_extent = statement.column_string(0);
//                    return s_vector_extent;
//                }
//            }
//        } catch (jsqlite.Exception e_stmt) {
//            GPLog.error("DAOSPATIALIE", "spatialiteRetrieveBounds sql[" + s_select_bounds + "] db[" + database.getFilename() + "]",
//                    e_stmt);
//        } finally {
//            if (statement != null)
//                statement.close();
//        }
//        return s_vector_extent;
//    }
//
//    /**
//     * Attemt to count geometry field.
//     * returned the number of Geometries that are NOT NULL
//     * - no recovery attemts should be done when this returns 0
//     * --- will abort attemts to recover if returns 0
//     * --- this speeds up the loading by 50% in my case
//     * VECTOR_LAYERS_QUERY_MODE=3 : fragment_about 5 seconds [before fragment_about 10 seconds]
//     * VECTOR_LAYERS_QUERY_MODE=0 : fragment_about 2 seconds
//     *
//     * @param database       the db to use.
//     * @param tableName      the table of the db to use.
//     * @param geometryColumn the geometry field of the table to use.
//     * @return count of Geometries NOT NULL
//     * @throws Exception if something goes wrong.
//     */
//    public static int getGeometriesCount(Database database, String tableName, String geometryColumn) throws Exception {
//        int i_count = 0;
//        if ((tableName.equals("")) || (geometryColumn.equals("")))
//            return i_count;
//        // SELECT CreateSpatialIndex('prov2008_s','Geometry');
//        String s_CountGeometries = "SELECT count('" + geometryColumn + "') FROM '" + tableName + "' WHERE '" + geometryColumn
//                + "' IS NOT NULL;";
//        Stmt statement = null;
//        try {
//            statement = database.prepare(s_CountGeometries);
//            if (statement.step()) {
//                i_count = statement.column_int(0);
//                return i_count;
//            }
//        } catch (jsqlite.Exception e_stmt) {
//            GPLog.error("DAOSPATIALIE", "spatialiteCountGeometries sql[" + s_CountGeometries
//                    + "] db[" + database.getFilename() + "]", e_stmt);
//        } finally {
//            if (statement != null)
//                statement.close();
//        }
//        return i_count;
//    }
//
//
//    /**
//     * Delete a list of features in the given database.
//     * <p/>
//     * <b>The features need to be from the same table</b>
//     *
//     * @param features the features list.
//     * @throws Exception if something goes wrong.
//     */
//    public static void deleteFeatures(List<Feature> features) throws Exception {
//        Feature firstFeature = features.get(0);
//
//        String databasePath = firstFeature.getDatabasePath();
//        SpatialiteDatabaseHandler databaseHandler = SpatialiteSourcesManager.INSTANCE.getExistingDatabaseHandlerByPath(databasePath);
//        Database database = databaseHandler.getDatabase();
//        String tableName = firstFeature.getTableName();
//
//        StringBuilder sbIn = new StringBuilder();
//        sbIn.append("delete from \"").append(tableName);
//        sbIn.append("\" where ");
//
//        StringBuilder sb = new StringBuilder();
//        for (Feature feature : features) {
//            sb.append(" OR ");
//            sb.append(SpatialiteUtilities.SPATIALTABLE_ID_FIELD).append("=");
//            sb.append(feature.getId());
//        }
//        String valuesPart = sb.substring(4);
//
//        sbIn.append(valuesPart);
//
//        String updateQuery = sbIn.toString();
//        database.exec(updateQuery, null);
//    }
//
//    /**
//     * Add a new spatial record by adding a geometry.
//     * <p/>
//     * <p>The other attributes will not be populated.
//     *
//     * @param geometry           the geometry that will create the new record.
//     * @param geometrySrid       the srid of the geometry without the EPSG prefix.
//     * @param spatialVectorTable the table into which to insert the record.
//     * @throws Exception if something goes wrong.
//     */
//    public static void addNewFeatureByGeometry(Geometry geometry, String geometrySrid, SpatialVectorTable spatialVectorTable)
//            throws Exception {
//
//        SpatialiteDatabaseHandler databaseHandler = SpatialiteSourcesManager.INSTANCE.getExistingDatabaseHandlerByTable(spatialVectorTable);
//        Database database = databaseHandler.getDatabase();
//        String tableName = spatialVectorTable.getTableName();
//        String geometryFieldName = spatialVectorTable.getGeomName();
//        String srid = spatialVectorTable.getSrid();
//        int geomType = spatialVectorTable.getGeomType();
//        GeometryType geometryType = GeometryType.forValue(geomType);
//        String geometryTypeCast = geometryType.getGeometryTypeCast();
//        String spaceDimensionsCast = geometryType.getSpaceDimensionsCast();
//        String multiSingleCast = geometryType.getMultiSingleCast();
//
//        String pkIgnoredField = SpatialiteUtilities.getIgnoredPkField(spatialVectorTable);
//
//        // get list of non geom fields and default values
//        String nonGeomFieldsNames = "";
//        String nonGeomFieldsValues = "";
//        for (String field : spatialVectorTable.getTableFieldNamesList()) {
//            boolean ignore = SpatialiteUtilities.doIgnoreField(field, pkIgnoredField);
//            if (!ignore) {
//                EDataType tableFieldType = spatialVectorTable.getTableFieldType(field);
//                if (tableFieldType != null) {
//                    nonGeomFieldsNames = nonGeomFieldsNames + "," + field;
//                    nonGeomFieldsValues = nonGeomFieldsValues + "," + tableFieldType.getDefaultValueForSql();
//                }
//            }
//        }
//
//        boolean doTransform = true;
//        if (srid.equals(geometrySrid)) {
//            doTransform = false;
//        }
//
//        StringBuilder sbIn = new StringBuilder();
//        sbIn.append("insert into \"").append(tableName);
//        sbIn.append("\" (");
//        sbIn.append(geometryFieldName);
//        // add fields
//        if (nonGeomFieldsNames.length() > 0) {
//            sbIn.append(nonGeomFieldsNames);
//        }
//        sbIn.append(") values (");
//        if (doTransform)
//            sbIn.append("ST_Transform(");
//        if (multiSingleCast != null)
//            sbIn.append(multiSingleCast).append("(");
//        if (spaceDimensionsCast != null)
//            sbIn.append(spaceDimensionsCast).append("(");
//        if (geometryTypeCast != null)
//            sbIn.append(geometryTypeCast).append("(");
//        sbIn.append("GeomFromText('");
//        sbIn.append(geometry.toText());
//        sbIn.append("' , ");
//        sbIn.append(geometrySrid);
//        sbIn.append(")");
//        if (geometryTypeCast != null)
//            sbIn.append(")");
//        if (spaceDimensionsCast != null)
//            sbIn.append(")");
//        if (multiSingleCast != null)
//            sbIn.append(")");
//        if (doTransform) {
//            sbIn.append(",");
//            sbIn.append(srid);
//            sbIn.append(")");
//        }
//        // add field default values
//        if (nonGeomFieldsNames.length() > 0) {
//            sbIn.append(nonGeomFieldsValues);
//        }
//        sbIn.append(")");
//        String insertQuery = sbIn.toString();
//
//        database.exec(insertQuery, null);
//    }
//
//    protected static void createImageField(SpatialVectorTable table) {
//        String GEOPAP_IMG_TYPE = "GEOPAP_TEXTARRAY_IMG";
//        String GEOPAP_IMG_DEFAULT_NAME = "geopapimgs";
//        String GEOPAP_IMG_DEFAULT_VALUE = "'[]'";
//
//        SpatialiteDatabaseHandler spatialiteDatabaseHandler = SpatialiteSourcesManager.INSTANCE.getExistingDatabaseHandlerByTable(table);
//        Database database = spatialiteDatabaseHandler.getDatabase();
//        String tableName = table.getTableName();
//
//        try {
//            HashMap<String, String> names2fieldInfo = collectTableFields(database, table.getTableName());
//            String imageField = null;
//            for (String name: names2fieldInfo.keySet()) {
//                String typeInfo = names2fieldInfo.get(name);
//                if (typeInfo.contains(GEOPAP_IMG_TYPE)) {
//                    imageField = name;
//                    break;
//                }
//            }
//            if (imageField==null) {
//                StringBuilder sqlImageField = new StringBuilder();
//                sqlImageField.append("ALTER TABLE ").append(tableName);
//                sqlImageField.append(" ADD COLUMN ");
//                sqlImageField.append(GEOPAP_IMG_DEFAULT_NAME);
//                sqlImageField.append(" ");
//                sqlImageField.append(GEOPAP_IMG_TYPE);
//                sqlImageField.append(" DEFAULT ").append(GEOPAP_IMG_DEFAULT_VALUE);
//                database.exec(sqlImageField.toString(), null);
//            }
//
//            } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Updates the alphanumeric values of a feature in the given database.
//     *
//     * @param database the database.
//     * @param feature  the feature.
//     * @throws Exception if something goes wrong.
//     */
//    public static void updateFeatureAlphanumericAttributes(Database database, Feature feature) throws Exception {
//        String tableName = feature.getTableName();
//        List<String> attributeNames = feature.getAttributeNames();
//        List<String> attributeValuesStrings = feature.getAttributeValuesStrings();
//        List<String> attributeTypes = feature.getAttributeTypes();
//
//        StringBuilder sbIn = new StringBuilder();
//        sbIn.append("update \"").append(tableName);
//        sbIn.append("\" set ");
//
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < attributeNames.size(); i++) {
//            String fieldName = attributeNames.get(i);
//            String value = attributeValuesStrings.get(i);
//            String type = attributeTypes.get(i);
//            boolean ignore = SpatialiteUtilities.doIgnoreField(fieldName);
//            if (!ignore) {
//                EDataType dataType = EDataType.getType4Name(type);
//                if (dataType == EDataType.TEXT || dataType == EDataType.DATE) {
//                    value = escapeString(value);
//                    sb.append(" , ").append(fieldName).append("='").append(value).append("'");
//                } else if (value == null || "".equals(value)) {
//                    sb.append(" , ").append(fieldName).append("=NULL");
//                }
//                else{
//                    sb.append(" , ").append(fieldName).append("=").append(value);
//                }
//            }
//        }
//        String valuesPart = sb.substring(3);
//
//        sbIn.append(" ");
//        sbIn.append(valuesPart);
//        sbIn.append(" where ");
//        sbIn.append(SpatialiteUtilities.SPATIALTABLE_ID_FIELD);
//        sbIn.append("=");
//        sbIn.append(feature.getId());
//
//        String updateQuery = sbIn.toString();
//        database.exec(updateQuery, null);
//
//        //SpatialVectorTable table = SpatialiteSourcesManager.INSTANCE.getTableFromFeature(feature);
//        //createImageField(table);
//    }
//
//    private static String escapeString(String value) {
//        return value.replaceAll("'", "''");
//    }
//
//    /**
//     * Updates the geometry of a feature in the given database.
//     *
//     * @throws Exception if something goes wrong.
//     */
//    public static void updateFeatureGeometry(String id, Geometry geometry, String geometrySrid, SpatialVectorTable spatialVectorTable)
//            throws Exception {
//        SpatialiteDatabaseHandler databaseHandler = SpatialiteSourcesManager.INSTANCE.getExistingDatabaseHandlerByTable(spatialVectorTable);
//        Database database = databaseHandler.getDatabase();
//        String tableName = spatialVectorTable.getTableName();
//        String geometryFieldName = spatialVectorTable.getGeomName();
//        String srid = spatialVectorTable.getSrid();
//        int geomType = spatialVectorTable.getGeomType();
//        GeometryType geometryType = GeometryType.forValue(geomType);
//        String geometryTypeCast = geometryType.getGeometryTypeCast();
//        String spaceDimensionsCast = geometryType.getSpaceDimensionsCast();
//        String multiSingleCast = geometryType.getMultiSingleCast();
//
//        boolean doTransform = true;
//        if (srid.equals(geometrySrid)) {
//            doTransform = false;
//        }
//
//        StringBuilder sbIn = new StringBuilder();
//        sbIn.append("update \"").append(tableName);
//        sbIn.append("\" set ");
//        sbIn.append(geometryFieldName);
//        sbIn.append(" = ");
//        if (doTransform)
//            sbIn.append("ST_Transform(");
//        if (multiSingleCast != null)
//            sbIn.append(multiSingleCast).append("(");
//        if (spaceDimensionsCast != null)
//            sbIn.append(spaceDimensionsCast).append("(");
//        if (geometryTypeCast != null)
//            sbIn.append(geometryTypeCast).append("(");
//        sbIn.append("GeomFromText('");
//        sbIn.append(geometry.toText());
//        sbIn.append("' , ");
//        sbIn.append(geometrySrid);
//        sbIn.append(")");
//        if (geometryTypeCast != null)
//            sbIn.append(")");
//        if (spaceDimensionsCast != null)
//            sbIn.append(")");
//        if (multiSingleCast != null)
//            sbIn.append(")");
//        if (doTransform) {
//            sbIn.append(",");
//            sbIn.append(srid);
//            sbIn.append(")");
//        }
//        sbIn.append("");
//        sbIn.append(" where ");
//        sbIn.append(SpatialiteUtilities.SPATIALTABLE_ID_FIELD).append("=");
//        sbIn.append(id);
//        String insertQuery = sbIn.toString();
//        database.exec(insertQuery, null);
//    }
//
//    /**
//     * Get the area and length in original units of a feature by its id.
//     *
//     * @param id                 the id of the feature, as defined by field
//     *                           {@link eu.geopaparazzi.spatialite.database.spatial.util.SpatialiteUtilities#SPATIALTABLE_ID_FIELD}
//     * @param spatialVectorTable the table in which the feature resides.
//     * @return the array with [area, length].
//     * @throws Exception if something goes wrong.
//     */
//    public static double[] getAreaAndLengthById(String id, SpatialVectorTable spatialVectorTable) throws Exception {
//        SpatialiteDatabaseHandler databaseHandler = SpatialiteSourcesManager.INSTANCE.getExistingDatabaseHandlerByTable(spatialVectorTable);
//        Database database = databaseHandler.getDatabase();
//        String tableName = spatialVectorTable.getTableName();
//        String geomName = spatialVectorTable.getGeomName();
//
//        StringBuilder sbIn = new StringBuilder();
//        sbIn.append("SELECT ");
//        sbIn.append("Area(").append(geomName).append("),");
//        sbIn.append("Length(").append(geomName).append(")");
//        sbIn.append(" from \"").append(tableName);
//        sbIn.append("\" where ");
//        sbIn.append(SpatialiteUtilities.SPATIALTABLE_ID_FIELD).append(" = ").append(id);
//
//        String selectQuery = sbIn.toString();
//        Stmt statement = null;
//        try {
//            statement = database.prepare(selectQuery);
//            if (statement.step()) {
//                double area = statement.column_double(0);
//                double length = statement.column_double(1);
//
//                return new double[]{area, length};
//            }
//        } catch (jsqlite.Exception e_stmt) {
//            GPLog.error("DAOSPATIALIE",
//                    "getAreaAndLengthById[" + tableName + "] sql[" + selectQuery + "] db[" + database.getFilename()
//                            + "]", e_stmt
//            );
//        } finally {
//            if (statement != null) {
//                statement.close();
//            }
//        }
//        return null;
//    }
//}

package eu.geopaparazzi.plugins.defaultexports;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import eu.geopaparazzi.core.GeopaparazziApplication;
import eu.geopaparazzi.core.database.DaoImages;
import eu.geopaparazzi.core.database.DaoMetadata;
import eu.geopaparazzi.library.core.ResourcesManager;
import eu.geopaparazzi.library.database.GPLog;
import eu.geopaparazzi.library.database.Image;
import eu.geopaparazzi.library.plugin.types.MenuEntry;
import eu.geopaparazzi.library.util.GPDialogs;
import eu.geopaparazzi.library.util.IActivitySupporter;
import eu.geopaparazzi.library.util.StringAsyncTask;
import eu.geopaparazzi.library.util.TimeUtilities;

/**
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class ExportImagesMenuEntry extends MenuEntry {


    private Context serviceContext;

    public ExportImagesMenuEntry(Context context) {
        this.serviceContext = context;
    }

    @Override
    public String getLabel() {
        return serviceContext.getString(eu.geopaparazzi.core.R.string.export_images);
    }

    @Override
    public void onClick(IActivitySupporter clickActivityStarter) {
        exportImages(clickActivityStarter.getContext());

    }

    private void exportImages(final Context context) {
        try {
            String projectName = DaoMetadata.getProjectName();
            if (projectName == null) {
                projectName = "geopaparazzi_images_";
            } else {
                projectName += "_images_";
            }
            File exportDir = ResourcesManager.getInstance(GeopaparazziApplication.getInstance()).getApplicationExportDir();
            final File outFolder = new File(exportDir, projectName + TimeUtilities.INSTANCE.TIMESTAMPFORMATTER_LOCAL.format(new Date()));
            if (!outFolder.mkdir()) {
                GPDialogs.warningDialog(context, context.getString(eu.geopaparazzi.core.R.string.export_img_unable_to_create_folder) + outFolder, null);
                return;
            }
            final List<Image> imagesList = DaoImages.getImagesList(false, false);
            if (imagesList.size() == 0) {
                GPDialogs.infoDialog(context, context.getString(eu.geopaparazzi.core.R.string.no_images_in_project), null);
                return;
            }


            final DaoImages imageHelper = new DaoImages();
            StringAsyncTask exportImagesTask = new StringAsyncTask(context) {
                protected String doBackgroundWork() {
                    try {
                        for (int i = 0; i < imagesList.size(); i++) {
                            Image image = imagesList.get(i);
                            try {
                                byte[] imageData = imageHelper.getImageData(image.getId());
                                if (imageData != null) {
                                    File imageFile = new File(outFolder, image.getName());

                                    FileOutputStream fos = new FileOutputStream(imageFile);
                                    fos.write(imageData);
                                    fos.close();
                                }
                            } catch (IOException e) {
                                GPLog.error(this, "For file: " + image.getName(), e);
                            } finally {
                                publishProgress(i);
                            }
                        }
                    } catch (Exception e) {
                        return "ERROR: " + e.getLocalizedMessage();
                    }
                    return "";
                }

                protected void doUiPostWork(String response) {
                    if (response == null) response = "";
                    if (response.length() != 0) {
                        GPDialogs.warningDialog(context, response, null);
                    } else {
                        GPDialogs.infoDialog(context, context.getString(eu.geopaparazzi.core.R.string.export_img_ok_exported) + outFolder, null);
                    }
                }
            };
            exportImagesTask.setProgressDialog(context.getString(eu.geopaparazzi.core.R.string.export_uc), context.getString(eu.geopaparazzi.core.R.string.export_img_processing), false, imagesList.size());
            exportImagesTask.execute();


        } catch (Exception e) {
            GPLog.error(this, null, e);
            GPDialogs.errorDialog(context, e, null);
        }
    }


}