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
package cu.phibrain.plugins.cardinal.io.exporter;

import android.content.Context;

import cu.phibrain.plugins.cardinal.io.exporter.dialogs.GpxExportDialogFragment;
import eu.geopaparazzi.library.plugin.types.MenuEntry;
import eu.geopaparazzi.library.util.IActivitySupporter;

/**
 * @author Andrea Antonello (www.hydrologis.com)
 */
public class ExportGpxMenuEntry extends MenuEntry {

    private final Context serviceContext;

    public ExportGpxMenuEntry(Context context) {
        this.serviceContext = context;
    }

    @Override
    public String getLabel() {
        return serviceContext.getString(eu.geopaparazzi.core.R.string.gpx);
    }

    @Override
    public void onClick(IActivitySupporter clickActivityStarter) {
        GpxExportDialogFragment gpxExportDialogFragment = GpxExportDialogFragment.newInstance(null);
        gpxExportDialogFragment.show(clickActivityStarter.getSupportFragmentManager(), "gpx export");
    }
}
