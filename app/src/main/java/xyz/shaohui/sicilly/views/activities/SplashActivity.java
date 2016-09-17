package xyz.shaohui.sicilly.views.activities;

import android.content.Intent;
import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.data.SPDataManager;
import xyz.shaohui.sicilly.data.network.auth.OAuthToken;
import xyz.shaohui.sicilly.views.home.IndexActivity;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkToken();
    }

    @Override
    public void initializeInjector() {

    }

    @Override
    public EventBus getBus() {
        return null;
    }

    private void checkToken() {
        OAuthToken token = SPDataManager.getToken(this);
        if (token == null) {
            startActivity(new Intent(this, LoginActivity.class));
        } else {
            SicillyApplication.setToken(token);
            startActivity(new Intent(this, IndexActivity.class));
        }
        finish();
    }
}
