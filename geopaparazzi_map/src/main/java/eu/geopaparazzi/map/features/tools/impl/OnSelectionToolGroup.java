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
package eu.geopaparazzi.map.features.tools.impl;

import java.util.List;

import eu.geopaparazzi.map.features.Feature;
import eu.geopaparazzi.map.features.tools.interfaces.ToolGroup;

/**
 * The group of tools active when a selection has been done.
 *
 * @author Cesar Martinez Izquierdo (www.scolab.es)
 */
public interface OnSelectionToolGroup extends ToolGroup {
    /**
     * Forces a feature selection.
     * <p/>
     * <p>Previous selections are cleared and a redrawing is triggered.
     *
     * @param features the new features to select.
     */
    void setSelectedFeatures(List<Feature> features);

}
