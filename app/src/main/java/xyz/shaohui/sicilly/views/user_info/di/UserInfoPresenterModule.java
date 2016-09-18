package xyz.shaohui.sicilly.views.user_info.di;

import dagger.Module;
import dagger.Provides;
import xyz.shaohui.sicilly.views.user_info.UserInfoPresenterImpl;
import xyz.shaohui.sicilly.views.user_info.mvp.UserInfoPresenter;
import xyz.shaohui.sicilly.views.user_info.photo.UserPhotoPresenterImpl;
import xyz.shaohui.sicilly.views.user_info.photo.mvp.UserPhotoPresenter;
import xyz.shaohui.sicilly.views.user_info.timeline.UserTimelinePresenterImpl;
import xyz.shaohui.sicilly.views.user_info.timeline.mvp.UserTimelinePresenter;

/**
 * Created by shaohui on 16/9/18.
 */

@Module
public class UserInfoPresenterModule {
    @Provides
    UserInfoPresenter provideUserInfoPresenter(UserInfoPresenterImpl presenter) {
        return presenter;
    }

    @Provides
    UserTimelinePresenter provideUserTimelinePresenter(UserTimelinePresenterImpl presenter) {
        return presenter;
    }

    @Provides
    UserPhotoPresenter provideUserPhotoPresenter(UserPhotoPresenterImpl presenter) {
        return presenter;
    }
}
