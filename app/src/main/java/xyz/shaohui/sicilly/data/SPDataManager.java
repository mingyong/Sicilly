package xyz.shaohui.sicilly.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import xyz.shaohui.sicilly.data.network.auth.OAuthToken;

/**
 * Created by shaohui on 16/8/19.
 */
public class SPDataManager {

    public static void saveToken(Context context, OAuthToken token) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit()
                .putString("auth_token", token.getToken())
                .putString("auth_secret", token.getTokenSecret())
                .apply();
    }

    public static OAuthToken getToken(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String authToken = sp.getString("auth_token", "");
        String authSecret = sp.getString("auth_secret", "");
        if (TextUtils.isEmpty(authSecret) || TextUtils.isEmpty(authToken)) {
            return null;
        } else {
            return new OAuthToken(authToken, authSecret);
        }
    }

}
