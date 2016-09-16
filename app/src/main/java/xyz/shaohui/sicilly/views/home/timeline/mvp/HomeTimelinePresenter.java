package xyz.shaohui.sicilly.views.home.timeline.mvp;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import xyz.shaohui.sicilly.data.models.Status;

/**
 * Created by shaohui on 16/9/10.
 */

public abstract class HomeTimelinePresenter extends MvpBasePresenter<HomeTimelineView> {
    public abstract void loadMessage();

    public abstract void loadMoreMessage(int page, Status lastStatus);

    public abstract void listenerNewMessage();

    public abstract void opStar(Status status, int position);

    public abstract void deleteMessage(Status status, int position);

    public abstract void commmentMessage(String messageId);

    public abstract void repostMessage(String messageId);
}
