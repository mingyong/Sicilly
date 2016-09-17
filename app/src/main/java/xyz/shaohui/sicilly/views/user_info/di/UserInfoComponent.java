package xyz.shaohui.sicilly.views.user_info.di;

import dagger.Component;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.data.network.di.UserModule;
import xyz.shaohui.sicilly.views.user_info.UserActivity;
import xyz.shaohui.sicilly.views.user_info.mvp.UserInfoPresenter;

/**
 * Created by shaohui on 16/9/18.
 */
@Component(dependencies = AppComponent.class,
modules = {
        UserInfoPresenterModule.class,

        UserModule.class
})
public interface UserInfoComponent {
    void inject(UserActivity activity);

    UserInfoPresenter presenter();
}
