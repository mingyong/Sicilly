package xyz.shaohui.sicilly.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.appModel.AppSetting;

/**
 * Created by shaohui on 16/8/19.
 */
public class SPDataManager {

    public static final String SP_KEY_FRIEND_REQUEST =
            "friend_request" + SicillyApplication.currentUId();

    public static final String SP_KEY_MENTION = "mention" + SicillyApplication.currentUId();

    public static final String SP_KEY_MESSAGE = "message" + SicillyApplication.currentUId();

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

    public static int getInt(String key, int defaultValue) {
        SharedPreferences sp = getSetting();
        Log.i("TAG_sp", "get" + key + sp.getInt(key, defaultValue));
        return sp.getInt(key, defaultValue);
    }

    public static boolean setInt(String key, int value, boolean isAsync) {
        Log.i("TAG_sp", "set" + key + value);
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
