package xyz.shaohui.sicilly.views.feedback.mvp;

import xyz.shaohui.sicilly.base.BasePresenter;

/**
 * Created by shaohui on 16/9/24.
 */

public abstract class FeedbackPresenter extends BasePresenter<FeedbackView> {

    public abstract void loadFeedbackList();

    public abstract void sendFeedback(String text);

    public abstract void uploadImage(String localPath);

    public abstract void deleteAll();

}
