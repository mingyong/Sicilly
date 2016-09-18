package xyz.shaohui.sicilly.views.user_info.timeline.mvp;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import xyz.shaohui.sicilly.data.models.Status;

/**
 * Created by shaohui on 16/9/18.
 */

public abstract class UserTimelinePresenter extends MvpBasePresenter<UserTimelineView> {

    public abstract void loadStatus(String userId);

    public abstract void loadMoreStatus(String userId, int page, Status lastStatus);

    public abstract void opStar(Status status, int position);

    public abstract void deleteStatus(Status status, int position);

}
