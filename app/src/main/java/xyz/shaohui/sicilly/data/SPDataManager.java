package xyz.shaohui.sicilly.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import xyz.shaohui.sicilly.data.appModel.AppSetting;
import xyz.shaohui.sicilly.data.network.auth.OAuthToken;

/**
 * Created by shaohui on 16/8/19.
 */
public class SPDataManager {

    public static AppSetting loadSetting(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        boolean sendMessage = sp.getBoolean(AppSetting.SEND_MESSAGE_KEY, true);
        boolean sendMention = sp.getBoolean(AppSetting.SEND_MENTION_KEY, true);
        boolean sendRequest = sp.getBoolean(AppSetting.SEND_REQUEST_KEY, true);
        return AppSetting.create(sendMessage, sendMention, sendRequest);
    }

    public static void modifySetting(Context context, String key, boolean value) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putBoolean(key, value).apply();
    }


}
