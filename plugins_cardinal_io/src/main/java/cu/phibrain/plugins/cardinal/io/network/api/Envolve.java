package cu.phibrain.plugins.cardinal.io.network.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Michel on 13/9/2020.
 */

public class Envolve<T> implements Serializable {
    @SerializedName("count")
    @Expose
    private int count;
    @SerializedName("next")
    @Expose
    private String next;
    @SerializedName("previous")
    @Expose
    private String previous;
    @SerializedName("results")
    @Expose
    private T results;

    Envolve() {
    }

    Envolve(int count, String next, String previous, T pData) {
        this.results = pData;
        this.count = count;
        this.next = next;
        this.previous = previous;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}
