package xyz.shaohui.sicilly.views.home.di;

import dagger.Module;
import dagger.Provides;
import xyz.shaohui.sicilly.views.home.chat.MessageListPresenterImpl;
import xyz.shaohui.sicilly.views.home.chat.mvp.MessageListPresenter;
import xyz.shaohui.sicilly.views.home.profile.ProfilePresenterImpl;
import xyz.shaohui.sicilly.views.home.profile.mvp.ProfilePresenter;

/**
 * Created by shaohui on 16/9/22.
 */

@Module
public class HomeModule {

    @Provides
    ProfilePresenter provideProfilePresenter(ProfilePresenterImpl presenter) {
        return presenter;
    }

    @Provides
    MessageListPresenter provideMessageListPresenter(MessageListPresenterImpl presenter) {
        return presenter;
    }
}
