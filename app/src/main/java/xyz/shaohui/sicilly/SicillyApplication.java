package xyz.shaohui.sicilly;

import android.app.Application;
import android.content.Context;

public class SicillyApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static Context getContext() {
        return context;
    }
}
