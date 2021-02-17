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

/**
 * Utility abstraction.
 *
 * @author Andrea Antonello (www.hydrologis.com)
 */
public abstract class ANote {
    /**
     * Flag to define selection or visibility.
     */
    protected boolean isChecked = true;

    /**
     * @return the note id.
     */
    public abstract long getId();

    /**
     * @return the note name.
     */
    public abstract String getName();

    /**
     * @return the note latitude.
     */
    public abstract double getLat();

    /**
     * @return the note longitude.
     */
    public abstract double getLon();

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }
}
