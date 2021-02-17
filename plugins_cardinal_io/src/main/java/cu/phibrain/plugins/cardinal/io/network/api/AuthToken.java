package cu.phibrain.plugins.cardinal.io.network.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class AuthToken implements Serializable {
    @SerializedName("key")
    @Expose
    private String key;

    public AuthToken() {
    }

    public AuthToken(String pkey) {
        this.key = pkey;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String pkey) {
        this.key = pkey;
    }

    @Override
    public String toString() {
        return "Token " + this.key;
    }

}
