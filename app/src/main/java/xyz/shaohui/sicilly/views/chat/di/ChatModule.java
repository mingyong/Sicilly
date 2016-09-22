package xyz.shaohui.sicilly.views.chat.di;

import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.views.chat.ChatPresenterImpl;
import xyz.shaohui.sicilly.views.chat.mvp.ChatPresenter;

/**
 * Created by shaohui on 16/9/23.
 */

@Module
public class ChatModule {

    private User mOtherUser;

    public ChatModule(User otherUser) {
        mOtherUser = otherUser;
    }

    @Provides
    ChatPresenter provideChatPresenter(ChatPresenterImpl presenter) {
        return presenter;
    }

    @Provides
    @Named("other_user")
    User provideOtherUser() {
        return mOtherUser;
    }

}
