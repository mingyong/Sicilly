package xyz.shaohui.sicilly.views.chat.mvp;

import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;
import xyz.shaohui.sicilly.data.models.Message;

/**
 * Created by shaohui on 16/9/23.
 */

public interface ChatView extends MvpView{

    void showMessage(List<Message> messages);

    void sendMessage(Message message);

    void sendMessageFail(String text);

}
