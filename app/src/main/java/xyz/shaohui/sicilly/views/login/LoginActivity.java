package xyz.shaohui.sicilly.views.login;

import android.os.Bundle;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.base.HasComponent;
import xyz.shaohui.sicilly.views.login.di.DaggerLoginComponent;
import xyz.shaohui.sicilly.views.login.di.LoginComponent;

public class LoginActivity extends BaseActivity implements HasComponent<LoginComponent>{
    private final String TAG = "loginActivity";

    @Inject
    EventBus mBus;

    LoginComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new LoginFragment())
                .commit();
    }

    @Override
    public void initializeInjector() {
        mComponent =
                DaggerLoginComponent.builder().appComponent(getAppComponent()).build();
        mComponent.inject(this);
    }

    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public LoginComponent getComponent() {
        if (mComponent == null) {
            mComponent =
                    DaggerLoginComponent.builder().appComponent(getAppComponent()).build();
        }
        return mComponent;
    }
}
