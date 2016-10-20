package xyz.shaohui.sicilly.views.activities;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.app.di.DaggerAppComponent;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.data.database.AppUserDbAccessor;
import xyz.shaohui.sicilly.data.models.AppUser;
import xyz.shaohui.sicilly.service.SicillyService;
import xyz.shaohui.sicilly.views.home.IndexActivity;
import xyz.shaohui.sicilly.views.login.LoginActivity;

public class SplashActivity extends BaseActivity {

    @Inject
    AppUserDbAccessor mAppUserDbAccessor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        checkToken();
    }

    @Override
    public void initializeInjector() {
        AppComponent component = DaggerAppComponent.builder().build();
        component.inject(this);
    }

    @Override
    public EventBus getBus() {
        return null;
    }

    private void checkToken() {

        mAppUserDbAccessor.selectCurrentUser()
                .subscribe(cursor -> {
                    Log.i("TAG_login_user", cursor.getCount() + "");
                   if (cursor.getCount() > 0) {
                       cursor.moveToFirst();
                       SicillyApplication.setCurrentAppUser(AppUser.MAPPER.map(cursor));
                       startActivity(new Intent(getApplicationContext(), IndexActivity.class));
                   } else {
                       startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                   }
                    finish();
                });
    }
}
