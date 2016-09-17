package xyz.shaohui.sicilly.views.user_info.di;

import dagger.Module;
import dagger.Provides;
import xyz.shaohui.sicilly.views.user_info.UserInfoPresenterImpl;
import xyz.shaohui.sicilly.views.user_info.mvp.UserInfoPresenter;

/**
 * Created by shaohui on 16/9/18.
 */

@Module
public class UserInfoPresenterModule {
    @Provides
    UserInfoPresenter provideUserInfoPresenter(UserInfoPresenterImpl presenter) {
        return presenter;
    }
}
