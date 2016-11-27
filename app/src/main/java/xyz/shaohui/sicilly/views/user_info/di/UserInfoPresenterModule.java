package xyz.shaohui.sicilly.views.user_info.di;

import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import xyz.shaohui.sicilly.views.user_info.UserInfoPresenterImpl;
import xyz.shaohui.sicilly.views.user_info.mvp.UserInfoPresenter;
import xyz.shaohui.sicilly.views.user_info.photo.UserPhotoPresenterImpl;
import xyz.shaohui.sicilly.views.user_info.photo.mvp.UserPhotoPresenter;

/**
 * Created by shaohui on 16/9/18.
 */

@Module
public class UserInfoPresenterModule {

    public static final String USER_ID = "user_id";

    private String mUserId;

    public UserInfoPresenterModule(String userId) {
        mUserId = userId;
    }

    @Named(USER_ID)
    @Provides
    String provideUserId() {
        return mUserId;
    }

    @Provides
    UserInfoPresenter provideUserInfoPresenter(UserInfoPresenterImpl presenter) {
        return presenter;
    }

    @Provides
    UserPhotoPresenter provideUserPhotoPresenter(UserPhotoPresenterImpl presenter) {
        return presenter;
    }
}
