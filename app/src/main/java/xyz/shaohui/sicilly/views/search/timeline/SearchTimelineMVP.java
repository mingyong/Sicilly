package xyz.shaohui.sicilly.views.search.timeline;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.views.feed.FeedMVP;

/**
 * Created by shaohui on 2016/11/1.
 */

public interface SearchTimelineMVP {

    interface View extends FeedMVP.View {
        String getKey();
    }

    interface Presenter extends FeedMVP.Presenter<View> {
    }

}
