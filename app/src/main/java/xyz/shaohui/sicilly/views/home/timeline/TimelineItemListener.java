package xyz.shaohui.sicilly.views.home.timeline;

import xyz.shaohui.sicilly.data.models.Status;

/**
 * Created by shaohui on 16/9/10.
 */

public interface TimelineItemListener {

    void opAvatar();

    void opContent();

    void opStar(Status status, int position);

    void opComment(Status status);

    void opRepost(Status status);

    void opDelete(Status status, int position);

}
