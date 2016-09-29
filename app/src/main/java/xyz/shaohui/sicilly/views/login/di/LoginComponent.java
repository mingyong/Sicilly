package xyz.shaohui.sicilly.views.login.di;

import dagger.Component;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.data.network.di.UserModule;
import xyz.shaohui.sicilly.views.login.LoginActivity;
import xyz.shaohui.sicilly.views.login.LoginFragment;
import xyz.shaohui.sicilly.views.login.mvp.LoginPresenter;

/**
 * Created by shaohui on 16/9/21.
 */

@Component(
        dependencies = AppComponent.class,

        modules = { UserModule.class, LoginModule.class}
)
public interface LoginComponent {
    void inject(LoginActivity activity);

    void inject(LoginFragment fragment);

    LoginPresenter presenter();
}
