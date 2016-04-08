package xyz.shaohui.sicilly.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import xyz.shaohui.sicilly.data.services.auth.OAuthToken;

/**
 * Created by kpt on 16/2/23.
 */
public class TokenSP {

    public static void saveToken(Context context, OAuthToken token) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString("token", token.getToken())
                .putString("token_secret", token.getTokenSecret())
                .apply();
    }

    public static void clearToken(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString("token", "")
                .putString("token_secret", "")
                .apply();
    }

    public static OAuthToken accessToken(Context context) {
        OAuthToken token = new OAuthToken();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        token.setToken(sp.getString("token", ""));
        token.setTokenSecret(sp.getString("token_secret", ""));

        return token;
    }

}
