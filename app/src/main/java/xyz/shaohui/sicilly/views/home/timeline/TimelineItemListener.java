package xyz.shaohui.sicilly.views.home.timeline;

import xyz.shaohui.sicilly.data.models.Status;

/**
 * Created by shaohui on 16/9/10.
 */

public interface TimelineItemListener {

    void opStar(Status status, int position);

    void opDelete(Status status, int position);

}
