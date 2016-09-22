package xyz.shaohui.sicilly.views.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.data.database.AppUserDbAccessor;
import xyz.shaohui.sicilly.data.models.AppUser;
import xyz.shaohui.sicilly.data.network.api.UserAPI;
import xyz.shaohui.sicilly.data.network.auth.AuthService;
import xyz.shaohui.sicilly.data.network.auth.OAuthToken;
import xyz.shaohui.sicilly.views.home.IndexActivity;

public class LoginActivity extends BaseActivity {
    private final String TAG = "loginActivity";

    @BindView(R.id.edit_username)
    EditText username;
    @BindView(R.id.edit_password)
    EditText password;

    @Inject
    UserAPI mUserService;

    @Inject
    AppUserDbAccessor mAppUserDbAccessor;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activty);
        ButterKnife.bind(this);

        mDialog = new ProgressDialog(this);
        mDialog.setProgress(ProgressDialog.STYLE_SPINNER);
        mDialog.setCancelable(false);
        mDialog.setMessage("登录中...");
    }

    @Override
    public void initializeInjector() {
        LoginComponent component =
                DaggerLoginComponent.builder().appComponent(getAppComponent()).build();
        component.inject(this);
    }

    @Override
    public EventBus getBus() {
        return null;
    }

    @OnClick(R.id.btn_login)
    void actionLogin() {
        Log.i(TAG, "登录");
        AuthService service = new AuthService();
        service.getAccessToken(this, username.getText().toString(), password.getText().toString(),
                new AuthService.AuthListener() {
                    @Override
                    public void begin() {
                        mDialog.show();
                    }

                    @Override
                    public void end(OAuthToken token) {
                        loadUserInfo(token);
                    }

                    @Override
                    public void failure() {
                        mDialog.dismiss();
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
                    SicillyApplication.setCUrrentAppUser(appUser);

                    mDialog.dismiss();
                    startIndex();
                });
    }

    private void startIndex() {
        Intent intent = new Intent(this, IndexActivity.class);
        startActivity(intent);
        finish();
    }
}
