package xyz.shaohui.sicilly.views.user_info.timeline.mvp;

import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;
import xyz.shaohui.sicilly.data.models.Status;

/**
 * Created by shaohui on 16/9/18.
 */

public interface UserTimelineView extends MvpView {

    void showMessage(List<Status> statuses);

    void showMoreMessage(List<Status> statuses);

    void loadMoreFailure();

    void networkError();

    void opStarFailure(int position);

    void deleteStatusFailure(Status status, int position);

}
