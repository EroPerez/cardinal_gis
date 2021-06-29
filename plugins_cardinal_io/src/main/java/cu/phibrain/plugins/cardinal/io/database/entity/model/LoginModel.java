package cu.phibrain.plugins.cardinal.io.database.entity.model;

/**
 * The class holding login data.
 *
 * @author Erodis Pérez Michel  (eperezm1986@gmail.com)
 */
public class LoginModel {
    /**
     * The username of workers.
     */
    public String username;

    /**
     * The password of workers.
     */
    public String password;

    public LoginModel(String user, String passwd){
        this.username = user;
        this.password = passwd;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }
}
