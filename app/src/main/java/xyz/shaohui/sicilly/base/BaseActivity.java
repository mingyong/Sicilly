package xyz.shaohui.sicilly.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import com.yatatsu.autobundle.AutoBundle;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.app.di.AppComponent;

/**
 * Created by shaohui on 16/9/10.
 */

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //AutoBundle.bind(this);
        initializeInjector();
    }

    public abstract void initializeInjector();

    public AppComponent getAppComponent() {
        return SicillyApplication.getAppComponent();
    }
}
