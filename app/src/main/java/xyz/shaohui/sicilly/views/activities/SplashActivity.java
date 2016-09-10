package xyz.shaohui.sicilly.views.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;

import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.data.SPDataManager;
import xyz.shaohui.sicilly.data.network.auth.OAuthToken;

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
