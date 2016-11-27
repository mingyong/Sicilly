package xyz.shaohui.sicilly.views.feed;

import xyz.shaohui.sicilly.data.models.Status;

/**
 * Created by shaohui on 2016/11/27.
 */

public interface FeedItemListener {

    void opStar(Status status, int position);

    void opDelete(Status status, int position);

}
