package xyz.shaohui.sicilly.views.login;

import dagger.Component;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.data.network.di.UserModule;

/**
 * Created by shaohui on 16/9/21.
 */

@Component(
        dependencies = AppComponent.class,

        modules = { UserModule.class}
)
public interface LoginComponent {
    void inject(LoginActivity activity);
}
