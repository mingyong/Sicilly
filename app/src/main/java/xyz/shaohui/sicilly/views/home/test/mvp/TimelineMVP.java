package xyz.shaohui.sicilly.views.home.test.mvp;

import xyz.shaohui.sicilly.views.feed.FeedMVP;

/**
 * Created by shaohui on 2016/11/27.
 */

public interface TimelineMVP {

    interface View extends FeedMVP.View {
    }

    interface Presenter extends FeedMVP.Presenter<View> {
    }
}
