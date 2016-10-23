package xyz.shaohui.sicilly.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.umeng.analytics.MobclickAgent;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.app.di.AppComponent;

/**
 * Created by shaohui on 16/9/10.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjector();
        try {
            if (getBus() != null && !getBus().isRegistered(this)) {
                getBus().register(this);
            }
        } catch (Exception ignored) {
        }
    }

    public abstract void initializeInjector();

    public AppComponent getAppComponent() {
        return SicillyApplication.getAppComponent();
    }

    public abstract EventBus getBus();

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        if (getBus() != null && getBus().isRegistered(this)) {
            getBus().unregister(this);
        }
        super.onDestroy();
    }
}
