package xyz.shaohui.sicilly.views.login.di;

import dagger.Module;
import dagger.Provides;
import xyz.shaohui.sicilly.views.login.LoginPresenterImpl;
import xyz.shaohui.sicilly.views.login.mvp.LoginPresenter;

/**
 * Created by shaohui on 16/9/30.
 */

@Module
public class LoginModule {

    @Provides
    LoginPresenter provideLoginPresenter(LoginPresenterImpl presenter) {
        return presenter;
    }

}
