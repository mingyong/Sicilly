package xyz.shaohui.sicilly;

import android.app.Application;
import android.content.Context;

public class SicillyApplication extends Application {

    public static Context context;
    public static String token;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        SicillyApplication.token = token;
    }
}
