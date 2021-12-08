package cu.phibrain.plugins.cardinal.io.database.entity.model;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * The class holding login data.
 *
 * @author Erodis Pérez Michel  (eperezm1986@gmail.com)
 */
public class LoginModel implements Serializable {
    /**
     * The username of workers.
     */
    @Expose
    public String username;

    /**
     * The password of workers.
     */
    @Expose
    public String password;

    @Expose(serialize = false, deserialize = false)
    private String token;

    private final static long serialVersionUID = -41119234649201958L;

    public LoginModel(String user, String passwd) {
        this.username = user;
        this.password = passwd;
        this.token = "";
    }

    public LoginModel() {
        this.username = "";
        this.password = "";
        this.token = "";
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }

    public String getToken() {
        return token;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
