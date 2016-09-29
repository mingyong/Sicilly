package xyz.shaohui.sicilly.views.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
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
import xyz.shaohui.sicilly.views.home.IndexActivity;

/**
 * Created by shaohui on 16/8/11.
 */
public class LoginDialogFragment extends BaseDialogFragment {

    @BindView(R.id.edit_username)
    EditText username;

    @BindView(R.id.edit_password)
    EditText password;

    UserAPI mUserService;

    private ProgressDialog mProgressDialog;

    private AppUserDbAccessor mAppUserDbAccessor;

    @Override
    public int layoutRes() {
        return R.layout.login_dialog_layout;
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

    private void loginSuccess() {
        startActivity(new Intent(getContext(), IndexActivity.class));
        getActivity().finish();
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
                    mAppUserDbAccessor.insertUser(appUser);
                    SicillyApplication.setCurrentAppUser(appUser);

                    loginSuccess();
                });
    }
}
