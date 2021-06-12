package cu.phibrain.plugins.cardinal.io.ui.menu;

import android.widget.SpinnerAdapter;

public class NetorksModel {
    public int ID;
    public String name;

    public NetorksModel(int id, String name){
        this.ID = id;
        this.name = name;
    }
    @Override
    public String toString() {
        return this.name; // What to display in the Spinner list.
    }
}
