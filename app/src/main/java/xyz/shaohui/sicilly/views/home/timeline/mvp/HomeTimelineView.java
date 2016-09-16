package xyz.shaohui.sicilly.views.home.timeline.mvp;

import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;
import xyz.shaohui.sicilly.data.models.Status;

/**
 * Created by shaohui on 16/9/10.
 */

public interface HomeTimelineView extends MvpView {

    void showMessage(List<Status> statuses);

    void showMoreMessage(List<Status> statuses);

    void showNewNotice();

    void showRefresh();

    void loadMoreFail();

    void networkError();

    void opStarFailure(int position);

    void deleteStatusFailure(Status status, int position);

}
