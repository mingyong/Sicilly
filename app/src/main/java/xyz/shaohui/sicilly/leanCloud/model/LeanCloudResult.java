package xyz.shaohui.sicilly.leanCloud.model;

import java.util.List;

/**
 * Created by shaohui on 16/10/6.
 */

public class LeanCloudResult<T> {

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    private List<T> results;

}
