package xyz.shaohui.sicilly.views.search.event;

/**
 * Created by shaohui on 2016/11/1.
 */

public class SearchTimelineEvent {

    private String key;

    public SearchTimelineEvent(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
