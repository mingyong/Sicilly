package xyz.shaohui.sicilly;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import com.xiaomi.mipush.sdk.MiPushClient;
import java.util.List;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.app.di.DaggerAppComponent;
import xyz.shaohui.sicilly.data.models.AppUser;
import xyz.shaohui.sicilly.data.network.auth.OAuthToken;

public class SicillyApplication extends Application {

    public static Context context;
    public static AppComponent mAppComponent;
    public static AppUser mAppUser;
    public static OAuthToken mToken;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        mAppComponent = DaggerAppComponent.builder().build();

        // 初始化小米推送
        initMiPush();
    }

    public static Context getContext() {
        return context;
    }

    public static OAuthToken getToken() {
        if (mToken != null && mAppUser != null && !TextUtils.equals(mToken.getToken(),
                mAppUser.token())) {
            mToken = new OAuthToken(mAppUser.token(), mAppUser.token_secret());
        } else if (mAppUser != null && mToken == null) {
            mToken = new OAuthToken(mAppUser.token(), mAppUser.token_secret());
        }
        return mToken;
    }

    // 仅在登录成功之后调用一次
    public static void setToken(OAuthToken token) {
        mToken = token;
    }

    public static String currentUId() {
        return mAppUser.id();
    }

    public static AppUser currentAppUser() {
        return mAppUser;
    }

    public static void setCurrentAppUser(AppUser appUser) {
        mAppUser = appUser;
    }

    public boolean isCurrentUser(String userId) {
        return TextUtils.equals(userId, currentAppUser().id());
    }

    public static boolean isSelf(String id) {
        return TextUtils.equals(currentUId(), id);
    }

    public static AppComponent getAppComponent() {
        return mAppComponent;
    }

    public static String getRegId() {
        return MiPushClient.getRegId(context);
    }

    private void initMiPush() {
        if (shouldInitMiPush()) {
            MiPushClient.registerPush(this, SicillyFactory.MI_PUSH_APPID,
                    SicillyFactory.MI_PUSH_APPKEY);
        }
    }

    private boolean shouldInitMiPush() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos = manager.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (myPid == info.pid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }
}
