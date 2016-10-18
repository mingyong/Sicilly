package xyz.shaohui.sicilly.views.login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.views.home.IndexActivity;
import xyz.shaohui.sicilly.views.login.di.LoginComponent;
import xyz.shaohui.sicilly.views.login.mvp.LoginPresenter;
import xyz.shaohui.sicilly.views.login.mvp.LoginView;
import xyz.shaohui.sicilly.views.web.WebActivity;

/**
 * Created by shaohui on 16/9/30.
 */

public class LoginFragment extends BaseFragment<LoginView, LoginPresenter> implements LoginView {

    @Inject
    EventBus mBus;

    @BindView(R.id.edit_username)
    EditText username;

    @BindView(R.id.edit_password)
    EditText password;

    private ProgressDialog mProgressDialog;

    @NonNull
    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public void injectDependencies() {
        LoginComponent component = getComponent(LoginComponent.class);
        component.inject(this);
        presenter = component.presenter();
    }

    @Override
    public int layoutRes() {
        return R.layout.activity_login_activty;
    }

    @Override
    public void bindViews(View view) {
        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage(getString(R.string.login_load_message));
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    @OnClick(R.id.btn_login)
    void opLogin() {
        if (TextUtils.isEmpty(username.getText()) || TextUtils.isEmpty(password.getText())) {
            return;
        }
        presenter.opLogin(username.getText().toString(), password.getText().toString());
    }

    @OnClick(R.id.action_register)
    void actionRegister() {
        startActivity(WebActivity.newIntent(getContext(), "http://fanfou.com/register/"));
    }

    @Override
    public void startLogin() {
        mProgressDialog.show();
    }

    @Override
    public void loginSuccess() {
        mProgressDialog.dismiss();
        startActivity(new Intent(getContext(), IndexActivity.class));
        getActivity().finish();
    }

    @Override
    public void loginFail() {
        mProgressDialog.dismiss();
    }
}
