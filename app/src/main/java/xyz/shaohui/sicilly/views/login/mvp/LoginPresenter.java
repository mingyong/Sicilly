package xyz.shaohui.sicilly.views.login.mvp;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by shaohui on 16/9/30.
 */

public abstract class LoginPresenter extends MvpBasePresenter<LoginView>{

    public abstract void opLogin(String username, String password);

}
