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

package eu.geopaparazzi.library.database;

import android.database.sqlite.SQLiteDatabase;

import java.io.IOException;

/**
 * Interface that helps adding points to external databases.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public interface IGpsLogDbHelper {

    /**
     * Get the database.
     *
     * @return a writable database.
     * @throws Exception if something goes wrong.
     */
    SQLiteDatabase getDatabase() throws Exception;

    /**
     * Creates a new gpslog entry and returns the log's new id.
     *
     * @param startTs the start UTC timestamp.
     * @param endTs   the end UTC timestamp.
     * @param lengthm the length of the log in meters
     * @param text    a description or null.
     * @param width   the width of the rendered log.
     * @param color   the color of the rendered log.
     * @param visible if <code>true</code>, it will be visible.
     * @return the id of the new created log.
     * @throws IOException if something goes wrong.
     */
    long addGpsLog(long startTs, long endTs, double lengthm, String text, float width, String color, boolean visible)
            throws IOException;

    /**
     * Adds a single gps log point to a log.
     *
     * <p>Transactions have to be opened and closed if necessary.</p>
     *
     * @param sqliteDatabase the db to use.
     * @param gpslogId       the log id to which to add to.
     * @param lon            the lon coordinate.
     * @param lat            the lat coordinate.
     * @param altim          the elevation of the point.
     * @param timestamp      the timestamp of the point.
     * @throws IOException if something goes wrong.
     */
    void addGpsLogDataPoint(SQLiteDatabase sqliteDatabase, long gpslogId, double lon, double lat, double altim,
                            long timestamp) throws IOException;

    /**
     * Deletes a gps log from the database.
     *
     * @param id the log's id.
     * @throws IOException if something goes wrong.
     */
    void deleteGpslog(long id) throws IOException;

    /**
     * Re-sets the end timestamp, in case it changed because points were added.
     *
     * @param logid the log to change.
     * @param end   the end timestamp.
     * @throws IOException if something goes wrong.
     */
    void setEndTs(long logid, long end) throws IOException;

    /**
     * Re-sets the log (track) length.
     *
     * @param logid  the log to change.
     * @param length the length of the track log
     * @throws IOException if something goes wrong.
     */
    void setTrackLengthm(long logid, double length) throws IOException;

    /**
     * Get the last available log id.
     * <p/>
     * <p>If something goes wrong or no logs are available before, an exception is thrown.</p>
     *
     * @return the last log id.
     * @throws Exception
     */
    long getLastLogId() throws Exception;

}
