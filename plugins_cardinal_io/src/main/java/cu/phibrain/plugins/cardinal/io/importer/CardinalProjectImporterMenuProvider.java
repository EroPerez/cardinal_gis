package cu.phibrain.plugins.cardinal.io.importer;

import android.content.Intent;
import android.os.IBinder;


import eu.geopaparazzi.library.plugin.PluginService;
import eu.geopaparazzi.library.plugin.types.MenuEntryList;


/**
 * @author Erodis Pérez Michel  (eperezm1986@gmail.com)
 */
public class CardinalProjectImporterMenuProvider extends PluginService {
    private static final String NAME = "CardinalProjectImporterMenuProvider";
    private MenuEntryList list = null;
    public CardinalProjectImporterMenuProvider() {
        super(NAME);
    }

    public IBinder onBind (Intent intent) {
        if (list==null) {
            list = new MenuEntryList();
            list.addEntry(new CardinalProjectImportMenuEntry(getApplicationContext()));
        }
        return list;
    }


}
