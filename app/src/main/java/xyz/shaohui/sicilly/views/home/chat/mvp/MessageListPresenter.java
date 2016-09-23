package xyz.shaohui.sicilly.views.home.chat.mvp;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by shaohui on 16/9/22.
 */

public abstract class MessageListPresenter extends MvpBasePresenter<MessageListView> {

    public abstract void fetchMessageList();

    public abstract void fetchMessageListNext(int page);

}
