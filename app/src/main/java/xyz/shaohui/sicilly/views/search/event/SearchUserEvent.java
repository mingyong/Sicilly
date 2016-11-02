package xyz.shaohui.sicilly.views.search.event;

/**
 * Created by shaohui on 2016/11/1.
 */

public class SearchUserEvent {

    private String key;

    public SearchUserEvent(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
