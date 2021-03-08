package cu.phibrain.plugins.cardinal.io.utils;


public interface Observer {

    //method to update the observer, used by subject
    void update();

    //attach with subject to observe
    void attachTo(Subject sub);
}
