package xyz.shaohui.sicilly.views.login.mvp;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by shaohui on 16/9/30.
 */

public interface LoginView extends MvpView {

    void startLogin();

    void loginSuccess();

    void loginFail();

}
