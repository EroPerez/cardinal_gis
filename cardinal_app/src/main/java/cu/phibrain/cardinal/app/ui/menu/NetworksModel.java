package cu.phibrain.cardinal.app.ui.menu;

public class NetworksModel {
    public int ID;
    public String name;

    public NetworksModel(int id, String name) {
        this.ID = id;
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name; // What to display in the Spinner list.
    }
}
