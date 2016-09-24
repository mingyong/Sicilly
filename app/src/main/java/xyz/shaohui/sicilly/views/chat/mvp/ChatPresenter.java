package xyz.shaohui.sicilly.views.chat.mvp;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import xyz.shaohui.sicilly.data.models.Message;

/**
 * Created by shaohui on 16/9/23.
 */

public abstract class ChatPresenter extends MvpBasePresenter<ChatView> {

    public abstract void fetchMessage();

    public abstract void fetchMessageNext(int page, Message lastMessage);

    public abstract void sendMessage(String text);

}
