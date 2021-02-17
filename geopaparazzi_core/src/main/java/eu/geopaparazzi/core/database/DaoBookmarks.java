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
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import eu.geopaparazzi.core.GeopaparazziApplication;
import eu.geopaparazzi.core.database.objects.Bookmark;
import eu.geopaparazzi.library.database.GPLog;

/**
 * @author Andrea Antonello (www.hydrologis.com)
 */
@SuppressWarnings("nls")
public class DaoBookmarks {

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_LON = "lon";
    private static final String COLUMN_LAT = "lat";
    private static final String COLUMN_TEXT = "text";
    private static final String COLUMN_ZOOM = "zoom";

    /**
     * Bookmarks table name.
     */
    public static final String TABLE_BOOKMARKS = "bookmarks";

    /**
     * Add a bookmark.
     *
     * @param lon  lon
     * @param lat  lat
     * @param text a text
     * @param zoom zoom level
     * @throws IOException if something goes wrong.
     */
    public static void addBookmark(double lon, double lat, String text, double zoom) throws IOException {
        SQLiteDatabase sqliteDatabase = GeopaparazziApplication.getInstance().getDatabase();
        sqliteDatabase.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(COLUMN_LON, lon);
            values.put(COLUMN_LAT, lat);
            values.put(COLUMN_TEXT, text);
            values.put(COLUMN_ZOOM, zoom);
            sqliteDatabase.insertOrThrow(TABLE_BOOKMARKS, null, values);

            sqliteDatabase.setTransactionSuccessful();
        } catch (SQLiteConstraintException e) {
            sqliteDatabase.endTransaction();
            sqliteDatabase.beginTransaction();
            // db still has bounds columns, dummy fill them, they are never used
            ContentValues values = new ContentValues();
            values.put(COLUMN_LON, lon);
            values.put(COLUMN_LAT, lat);
            values.put(COLUMN_TEXT, text);
            values.put(COLUMN_ZOOM, zoom);
            values.put("bnorth", -1);
            values.put("bsouth", -1);
            values.put("beast", -1);
            values.put("bwest", -1);
            sqliteDatabase.insertOrThrow(TABLE_BOOKMARKS, null, values);
            sqliteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            GPLog.error("DAOBOOKMARKS", e.getLocalizedMessage(), e);
            throw new IOException(e.getLocalizedMessage());
        } finally {
            sqliteDatabase.endTransaction();
        }
    }

    /**
     * Delete bookmark.
     *
     * @param id the id of the bookmark to delete.
     * @throws IOException if something goes wrong.
     */
    public static void deleteBookmark(long id) throws IOException {
        SQLiteDatabase sqliteDatabase = GeopaparazziApplication.getInstance().getDatabase();
        sqliteDatabase.beginTransaction();
        try {
            // delete note
            String query = "delete from " + TABLE_BOOKMARKS + " where " + COLUMN_ID + " = " + id;
            SQLiteStatement sqlUpdate = sqliteDatabase.compileStatement(query);
            sqlUpdate.execute();

            sqliteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            GPLog.error("DAOBOOKMARKS", e.getLocalizedMessage(), e);
            throw new IOException(e.getLocalizedMessage());
        } finally {
            sqliteDatabase.endTransaction();
        }
    }

    /**
     * Chaneg bm name.
     *
     * @param id      id of bm to change.
     * @param newName new name.
     * @throws IOException if something goes wrong.
     */
    public static void updateBookmarkName(long id, String newName) throws IOException {
        SQLiteDatabase sqliteDatabase = GeopaparazziApplication.getInstance().getDatabase();
        sqliteDatabase.beginTransaction();
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("UPDATE ");
            sb.append(TABLE_BOOKMARKS);
            sb.append(" SET ");
            sb.append(COLUMN_TEXT).append("='").append(newName).append("' ");
            sb.append("WHERE ").append(COLUMN_ID).append("=").append(id);

            String query = sb.toString();
            if (GPLog.LOG_HEAVY)
                GPLog.addLogEntry("DAOBOOKMARKS", query);
            SQLiteStatement sqlUpdate = sqliteDatabase.compileStatement(query);
            sqlUpdate.execute();
            sqlUpdate.close();

            sqliteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            GPLog.error("DAOBOOKMARKS", e.getLocalizedMessage(), e);
            throw new IOException(e.getLocalizedMessage());
        } finally {
            sqliteDatabase.endTransaction();
        }
    }

    /**
     * Get the collected notes from the database inside a given bound.
     *
     * @param n north
     * @param s south
     * @param w west
     * @param e east
     * @return the list of notes inside the bounds.
     * @throws IOException if something goes wrong.
     */
    public static List<Bookmark> getBookmarksInWorldBounds(double n, double s, double w, double e) throws IOException {

        SQLiteDatabase sqliteDatabase = GeopaparazziApplication.getInstance().getDatabase();
        String query = "SELECT _id, lon, lat, text FROM XXX WHERE (lon BETWEEN XXX AND XXX) AND (lat BETWEEN XXX AND XXX)";
        // String[] args = new String[]{TABLE_NOTES, String.valueOf(w), String.valueOf(e),
        // String.valueOf(s), String.valueOf(n)};

        query = query.replaceFirst("XXX", TABLE_BOOKMARKS);
        query = query.replaceFirst("XXX", String.valueOf(w));
        query = query.replaceFirst("XXX", String.valueOf(e));
        query = query.replaceFirst("XXX", String.valueOf(s));
        query = query.replaceFirst("XXX", String.valueOf(n));

        // Logger.i("DAOBOOKMARKS", "Query: " + query);

        Cursor c = sqliteDatabase.rawQuery(query, null);
        List<Bookmark> bookmarks = new ArrayList<>();
        c.moveToFirst();
        while (!c.isAfterLast()) {
            long id = c.getLong(0);
            double lon = c.getDouble(1);
            double lat = c.getDouble(2);
            String text = c.getString(3);

            Bookmark note = new Bookmark(id, text, lon, lat);
            bookmarks.add(note);
            c.moveToNext();
        }
        c.close();
        return bookmarks;
    }

    /**
     * @return all bookmarks.
     * @throws IOException if something goes wrong.
     */
    public static List<Bookmark> getAllBookmarks() throws IOException {
        SQLiteDatabase sqliteDatabase = GeopaparazziApplication.getInstance().getDatabase();
        String query = "SELECT _id, lon, lat, text, zoom FROM " + TABLE_BOOKMARKS;

        // Logger.i("DAOBOOKMARKS", "Query: " + query);

        Cursor c = sqliteDatabase.rawQuery(query, null);
        List<Bookmark> bookmarks = new ArrayList<>();
        c.moveToFirst();
        while (!c.isAfterLast()) {
            long id = c.getLong(0);
            double lon = c.getDouble(1);
            double lat = c.getDouble(2);
            String text = c.getString(3);
            double zoom = c.getDouble(4);

            Bookmark note = new Bookmark(id, text, lon, lat, zoom);
            bookmarks.add(note);
            c.moveToNext();
        }
        c.close();
        return bookmarks;
    }

//    /**
//     * @param marker the marker to use.
//     * @return the list of {@link OverlayItem}s
//     * @throws IOException  if something goes wrong.
//     */
//    public static List<OverlayItem> getBookmarksOverlays( Drawable marker ) throws IOException {
//        SQLiteDatabase sqliteDatabase = GeopaparazziApplication.getInstance().getDatabase();
//        String query = "SELECT lon, lat, text FROM " + TABLE_BOOKMARKS;
//
//        Cursor c = null;
//        try {
//            c = sqliteDatabase.rawQuery(query, null);
//            List<OverlayItem> bookmarks = new ArrayList<>();
//            c.moveToFirst();
//            while( !c.isAfterLast() ) {
//                double lon = c.getDouble(0);
//                double lat = c.getDouble(1);
//                String text = c.getString(2);
//                text = text + "\n";
//                OverlayItem bookmark = new OverlayItem(new GeoPoint(lat, lon), null, text, marker);
//                bookmarks.add(bookmark);
//                c.moveToNext();
//            }
//            return bookmarks;
//        } finally {
//            if (c != null)
//                c.close();
//        }
//    }

    /**
     * Create bookmarks tables.
     *
     * @param db
     * @throws IOException if something goes wrong.
     */
    public static void createTables(SQLiteDatabase sqliteDatabase) throws IOException {
        StringBuilder sB = new StringBuilder();
        sB.append("CREATE TABLE ");
        sB.append(TABLE_BOOKMARKS);
        sB.append(" (");
        sB.append(COLUMN_ID);
        sB.append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
        sB.append(COLUMN_LON).append(" REAL NOT NULL, ");
        sB.append(COLUMN_LAT).append(" REAL NOT NULL,");
        sB.append(COLUMN_ZOOM).append(" REAL NOT NULL,");
        sB.append(COLUMN_TEXT).append(" TEXT NOT NULL ");
        sB.append(");");
        String CREATE_TABLE_BOOKMARKS = sB.toString();

        sB = new StringBuilder();
        sB.append("CREATE INDEX bookmarks_x_by_y_idx ON ");
        sB.append(TABLE_BOOKMARKS);
        sB.append(" ( ");
        sB.append(COLUMN_LON);
        sB.append(", ");
        sB.append(COLUMN_LAT);
        sB.append(" );");
        String CREATE_INDEX_BOOKMARKS_X_BY_Y = sB.toString();

        if (sqliteDatabase == null)
            sqliteDatabase = GeopaparazziApplication.getInstance().getDatabase();
        if (GPLog.LOG_ANDROID)
            Log.i("DAOBOOKMARKS", "Create the bookmarks table.");

        sqliteDatabase.beginTransaction();
        try {
            sqliteDatabase.execSQL(CREATE_TABLE_BOOKMARKS);
            sqliteDatabase.execSQL(CREATE_INDEX_BOOKMARKS_X_BY_Y);

            sqliteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e("DAOBOOKMARKS", e.getLocalizedMessage(), e);
            throw new IOException(e.getLocalizedMessage());
        } finally {
            sqliteDatabase.endTransaction();
        }
    }

}
