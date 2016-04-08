package xyz.shaohui.sicilly;

import android.app.Application;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.umeng.analytics.AnalyticsConfig;

public class SicillyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        AnalyticsConfig.setAppkey(this, SicillyFactory.UMENG_KEY);
        AnalyticsConfig.setChannel("Text");
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
