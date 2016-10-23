package xyz.shaohui.sicilly.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.appModel.AppSetting;
import xyz.shaohui.sicilly.data.models.AppUser;

/**
 * Created by shaohui on 16/8/19.
 */
public class SPDataManager {

    public static final String SP_KEY_FRIEND_REQUEST =
            "friend_request" + SicillyApplication.currentUId();

    public static final String SP_KEY_MENTION = "mention" + SicillyApplication.currentUId();

    public static final String SP_KEY_MESSAGE = "message" + SicillyApplication.currentUId();

    private static final String APP_USER_ID = "app_user_id";
    private static final String APP_USER_NAME = "app_user_name";
    private static final String APP_USER_AVATAR = "app_user_avatar";
    private static final String APP_USER_TOKEN = "app_user_token";
    private static final String APP_USER_SECRET = "app_user_secret";

    public static SharedPreferences getSetting() {
        return PreferenceManager.getDefaultSharedPreferences(SicillyApplication.getContext());
    }

    public static AppSetting loadAppSetting(Context context) {
        SharedPreferences sp = getSetting();
        boolean sendMessage = sp.getBoolean(AppSetting.SEND_MESSAGE_KEY, true);
        boolean sendMention = sp.getBoolean(AppSetting.SEND_MENTION_KEY, true);
        boolean sendRequest = sp.getBoolean(AppSetting.SEND_REQUEST_KEY, true);
        return AppSetting.create(sendMessage, sendMention, sendRequest);
    }

    public static void modifyAppSetting(Context context, String key, boolean value) {
        SharedPreferences sp = getSetting();
        sp.edit().putBoolean(key, value).apply();
    }

    public static void checkAndSaveAppUser(AppUser user) {
        SharedPreferences sp = getSetting();
        if (!user.id().equals(sp.getString(APP_USER_ID, ""))) {
            sp.edit()
                    .putString(APP_USER_ID, user.id())
                    .putString(APP_USER_AVATAR, user.avatar())
                    .putString(APP_USER_NAME, user.name())
                    .putString(APP_USER_TOKEN, user.token())
                    .putString(APP_USER_SECRET, user.token_secret())
                    .apply();
        }
    }

    public static AppUser getAppUser() {
        SharedPreferences sp = getSetting();
        return AppUser.create(sp.getString(APP_USER_ID, ""), sp.getString(APP_USER_TOKEN, ""),
                        sp.getString(APP_USER_SECRET, ""), sp.getString(APP_USER_NAME, ""),
                        sp.getString(APP_USER_AVATAR, ""), true);
    }

    public static int getInt(String key, int defaultValue) {
        SharedPreferences sp = getSetting();
        return sp.getInt(key, defaultValue);
    }

    public static boolean setInt(String key, int value, boolean isAsync) {
        SharedPreferences sp = getSetting();
        SharedPreferences.Editor editor = sp.edit().putInt(key, value);
        if (isAsync) {
            editor.apply();
        } else {
            return editor.commit();
        }
        return true;
    }
}
