package xyz.shaohui.sicilly;

import android.app.Application;
import android.content.Context;

import xyz.shaohui.sicilly.data.network.RetrofitService;

public class SicillyApplication extends Application {

    public static Context context;
    public static String token;
    public static RetrofitService retrofitService;

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

    public static RetrofitService getRetrofitService() {
        if (retrofitService == null) {
            retrofitService = new RetrofitService();
        }
        return retrofitService;
    }
}
