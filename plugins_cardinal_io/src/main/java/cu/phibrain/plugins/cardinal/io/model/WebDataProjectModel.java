package cu.phibrain.plugins.cardinal.io.model;

/**
 * The class holding webdata project info.
 *
 * @author Erodis Pérez Michel  (eperezm1986@gmail.com)
 */
public class WebDataProjectModel {

    /**
     * The machine id for the project.
     */
    public Integer id;

    /**
     * The machine name for the project.
     */
    public String name;

    /**
     * The description of the project.
     */
    public String description;

    /**
     * Last date of editing.
     */
    public String created_at;

    public boolean isSelected;

    /**
     * Checks if the project info match the supplied string.
     *
     * @param pattern the pattern to match.
     * @return <code>true</code> if the pattern matches any info.
     */
    public boolean matches(String pattern) {
        pattern = pattern.toLowerCase();
        if (name.toLowerCase().contains(pattern)) {
            return true;
        }
        if (description.toLowerCase().contains(pattern)) {
            return true;
        }
        if (created_at.toLowerCase().contains(pattern)) {
            return true;
        }
        if (String.valueOf(id).contains(pattern)) {
            return true;
        }
        return false;
    }
}
