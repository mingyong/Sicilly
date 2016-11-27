package xyz.shaohui.sicilly.views.user_info.di;

import dagger.Component;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.data.network.di.FavoriteModule;
import xyz.shaohui.sicilly.data.network.di.FriendshipModule;
import xyz.shaohui.sicilly.data.network.di.StatusModule;
import xyz.shaohui.sicilly.data.network.di.UserModule;
import xyz.shaohui.sicilly.views.user_info.PrivacyFragment;
import xyz.shaohui.sicilly.views.user_info.UserActivity;
import xyz.shaohui.sicilly.views.user_info.mvp.UserInfoPresenter;
import xyz.shaohui.sicilly.views.user_info.photo.UserPhotoFragment;
import xyz.shaohui.sicilly.views.user_info.photo.mvp.UserPhotoPresenter;
import xyz.shaohui.sicilly.views.user_info.timeline.UserTimelineFragment;
import xyz.shaohui.sicilly.views.user_info.timeline.UserTimelinePresenterImpl;

/**
 * Created by shaohui on 16/9/18.
 */
@Component(dependencies = AppComponent.class, modules = {
        UserInfoPresenterModule.class,

        UserModule.class, StatusModule.class, FavoriteModule.class, FriendshipModule.class
})
public interface UserInfoComponent {
    void inject(UserActivity activity);

    void inject(UserTimelineFragment fragment);

    void inject(UserPhotoFragment fragment);

    void inject(PrivacyFragment fragment);

    UserInfoPresenter userInfoPresenter();

    UserTimelinePresenterImpl userTimelinePresenter();

    UserPhotoPresenter userPhotoPresenter();
}
