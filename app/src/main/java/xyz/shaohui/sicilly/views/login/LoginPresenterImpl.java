package xyz.shaohui.sicilly.views.login;

import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.database.AppUserDbAccessor;
import xyz.shaohui.sicilly.data.models.AppUser;
import xyz.shaohui.sicilly.data.network.api.UserAPI;
import xyz.shaohui.sicilly.data.network.auth.AuthService;
import xyz.shaohui.sicilly.data.network.auth.OAuthToken;
import xyz.shaohui.sicilly.views.login.mvp.LoginPresenter;

/**
 * Created by shaohui on 16/9/30.
 */

public class LoginPresenterImpl extends LoginPresenter {

    private final UserAPI mUserService;

    private final AppUserDbAccessor mAppUserDbAccessor;

    @Inject
    LoginPresenterImpl(UserAPI userService, AppUserDbAccessor appUserDbAccessor) {
        mUserService = userService;
        mAppUserDbAccessor = appUserDbAccessor;
    }

    @Override
    public void opLogin(String username, String password) {
        AuthService service = new AuthService();
        service.getAccessToken(SicillyApplication.getContext(), username, password,
                new AuthService.AuthListener() {
                    @Override
                    public void begin() {
                        if (isViewAttached()) {
                            getView().startLogin();
                        }
                    }

                    @Override
                    public void end(OAuthToken token) {
                        loadUserInfo(token);
                    }

                    @Override
                    public void failure() {
                        if (isViewAttached()) {
                            getView().loginFail();
                        }
                    }
                });
    }

    private void loadUserInfo(OAuthToken token) {

        // 临时Token
        SicillyApplication.setToken(token);

        mUserService.userInfoSelf()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    AppUser appUser =
                            AppUser.create(user.id(), token.getToken(), token.getTokenSecret(),
                                    user.name(), user.profile_image_url_large(), true);
                    mAppUserDbAccessor.insertUser(appUser);
                    SicillyApplication.setCurrentAppUser(appUser);

                    if (isViewAttached()) {
                        getView().loginSuccess();
                    }
                });
    }

}
