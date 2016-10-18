package xyz.shaohui.sicilly.event;

/**
 * Created by shaohui on 16/9/27.
 */

public class FriendRequestEvent {

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    private int count;

    public FriendRequestEvent(int count) {
        this.count = count;
    }

}
