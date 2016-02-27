package xyz.shaohui.sicilly.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyFactory;
import xyz.shaohui.sicilly.data.preferences.TokenSP;
import xyz.shaohui.sicilly.data.services.auth.OAuthToken;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        checkToken();
    }

    private void checkToken() {
        OAuthToken token = TokenSP.accessToken(this);

        if (TextUtils.isEmpty(token.getToken())) {
            startActivity(new Intent(SplashActivity.this, LoginActivity.class));
        } else {
            SicillyFactory.token = token;
            startActivity(new Intent(SplashActivity.this, IndexActivity.class));
        }
        finish();
    }

}
