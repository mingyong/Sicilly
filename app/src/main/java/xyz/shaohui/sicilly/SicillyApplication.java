package xyz.shaohui.sicilly;

import android.app.Application;
import android.content.Context;

import android.text.TextUtils;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.app.di.DaggerAppComponent;
import xyz.shaohui.sicilly.data.SPDataManager;
import xyz.shaohui.sicilly.data.network.RetrofitService;
import xyz.shaohui.sicilly.data.network.auth.OAuthToken;

public class SicillyApplication extends Application {

    public static Context context;
    public static OAuthToken oAuthToken;
    public static RetrofitService retrofitService;
    public static AppComponent mAppComponent;
    public static String currentUId;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        mAppComponent = DaggerAppComponent.builder().build();
    }

    public static Context getContext() {
        return context;
    }

    public static OAuthToken getToken() {
        return oAuthToken;
    }

    public static void setToken(OAuthToken token) {
        SicillyApplication.oAuthToken = token;
    }

    public static RetrofitService getRetrofitService() {
        if (retrofitService == null) {
            retrofitService = new RetrofitService();
        }
        return retrofitService;
    }

    public static String currentUId() {
        if (currentUId == null) {
            currentUId = SPDataManager.getUserId(getContext());
        }
        return currentUId;
    }

    public static boolean isSelf(String id) {
        return TextUtils.equals(currentUId(), id);
    }

    public static AppComponent getAppComponent() {
        return mAppComponent;
    }
}
