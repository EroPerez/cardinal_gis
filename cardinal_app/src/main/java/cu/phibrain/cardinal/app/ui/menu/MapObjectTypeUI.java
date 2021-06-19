package cu.phibrain.cardinal.app.ui.menu;

public class MapObjectTypeUI {
    private int color;
    private int icon;
    private Long id;

    public MapObjectTypeUI(int icon2, int color2, Long id2) {
        this.color = color2;
        this.icon = icon2;
        setId(id2);
    }

    public int getIcon() {
        return this.icon;
    }

    public void setIcon(int icon2) {
        this.icon = icon2;
    }

    public int getColor() {
        return this.color;
    }

    public void setColor(int color2) {
        this.color = color2;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id2) {
        this.id = id2;
    }
}
