package xyz.shaohui.sicilly.views.chat.di;

import dagger.Component;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.data.network.di.FriendshipModule;
import xyz.shaohui.sicilly.data.network.di.MessageModule;
import xyz.shaohui.sicilly.views.chat.ChatActivity;
import xyz.shaohui.sicilly.views.chat.ChatFragment;
import xyz.shaohui.sicilly.views.chat.mvp.ChatPresenter;

/**
 * Created by shaohui on 16/9/23.
 */

@Component(
        dependencies = AppComponent.class,
        modules = {
                MessageModule.class,
                ChatModule.class, FriendshipModule.class
        })
public interface ChatComponent {

    void inject(ChatActivity activity);

    void inject(ChatFragment fragment);

    ChatPresenter chatPresenter();
}
