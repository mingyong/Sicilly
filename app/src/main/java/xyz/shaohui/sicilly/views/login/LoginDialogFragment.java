package xyz.shaohui.sicilly.views.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import javax.inject.Inject;
import me.shaohui.sicillylib.utils.ToastUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.base.BaseDialogFragment;
import xyz.shaohui.sicilly.data.database.AppUserDbAccessor;
import xyz.shaohui.sicilly.data.models.AppUser;
import xyz.shaohui.sicilly.data.network.api.UserAPI;
import xyz.shaohui.sicilly.data.network.auth.AuthService;
import xyz.shaohui.sicilly.data.network.auth.OAuthToken;
import xyz.shaohui.sicilly.notification.NotificationUtils;
import xyz.shaohui.sicilly.views.home.IndexActivity;
import xyz.shaohui.sicilly.views.home.di.HomeComponent;

/**
 * Created by shaohui on 16/8/11.
 */
public class LoginDialogFragment extends BaseDialogFragment {

    @BindView(R.id.edit_username)
    EditText username;

    @BindView(R.id.edit_password)
    EditText password;

    @Inject
    UserAPI mUserService;

    @Inject
    AppUserDbAccessor mAppUserDbAccessor;

    private ProgressDialog mProgressDialog;

    @Override
    public int layoutRes() {
        return R.layout.login_dialog_layout;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeComponent component = getComponent(HomeComponent.class);
        component.inject(this);
    }

    @Override
    public void bindView(View view) {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.login_load_message));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    @OnClick(R.id.btn_login)
    void loginAction() {
        if (TextUtils.isEmpty(username.getText()) || TextUtils.isEmpty(password.getText())) {
            return;
        }
        opLogin(username.getText().toString(), password.getText().toString());
    }

    private void startLogin() {
        mProgressDialog.show();
    }

    private void loginFail() {
        mProgressDialog.dismiss();
        ToastUtils.showToast(getContext(), "登录失败, 请重试");
    }

    public void opLogin(String username, String password) {
        AuthService service = new AuthService();
        service.getAccessToken(SicillyApplication.getContext(), username, password,
                new AuthService.AuthListener() {
                    @Override
                    public void begin() {
                        startLogin();
                    }

                    @Override
                    public void end(OAuthToken token) {
                        loadUserInfo(token);
                    }

                    @Override
                    public void failure() {
                        loginFail();
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
                    //mAppUserDbAccessor
                    // 切换用户
                    // 1. 切换DB中的Active User
                    // 2. 切换Application 的 currentUser
                    // 3. Service 中自动观察数据库变化 更新Application,
                    //      如果不生效, 可以使用AIDL调用, 使Service更新
                    // 4. 清除所有的Notification
                    // 5. 重启Activity

                    mAppUserDbAccessor.updateActiveUser(SicillyApplication.currentAppUser(),
                            appUser);           // 1
                    SicillyApplication.setCurrentAppUser(appUser);          // 2
                    NotificationUtils.clearAll(getContext());       // 4

                    startActivity(new Intent(getContext(), IndexActivity.class));      // 5
                    getActivity().finish();
                });
    }
}
