package xyz.shaohui.sicilly.views.home.chat.mvp;

import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;
import xyz.shaohui.sicilly.data.models.Conversation;
import xyz.shaohui.sicilly.data.models.ConversationBean;

/**
 * Created by shaohui on 16/9/22.
 */

public interface MessageListView extends MvpView {

    void showEmpty();

    void showNetError();

    void showConversation(List<Conversation> conversations);

    void showMoreConversation(List<Conversation> conversations);

    void loadNoMore();

    void loadMoreError();

}
