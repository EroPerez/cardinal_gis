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
package eu.geopaparazzi.core.database.objects;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * A set of serializable logs and log data.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class SerializableLogs implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<LogMapItem> logs = new ArrayList<>();
    private ArrayList<Line> logDatas = new ArrayList<>();
    private int size = 0;

    public void addLog(LogMapItem log, Line logData) {
        logs.add(log);
        logDatas.add(logData);
        size++;
    }

    public LogMapItem getLogAt(int index) {
        return logs.get(index);
    }

    public Line getLogDataAt(int index) {
        return logDatas.get(index);
    }

    public int getSize() {
        return size;
    }
}
