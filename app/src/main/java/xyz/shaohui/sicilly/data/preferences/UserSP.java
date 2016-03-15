package xyz.shaohui.sicilly.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import xyz.shaohui.sicilly.data.models.User;

/**
 * Created by kpt on 16/3/15.
 */
public class UserSP {

    public static void saveCurrentUser(Context context, User user) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString("user.id", user.getId())
                .putString("user.avatar", user.getProfileImageUrl())
                .putString("user.name", user.getNickName())
                .apply();
    }

    public static User getUserInfo(Context context) {
        User user = new User();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);

        user.setId(sp.getString("user.id", ""));
        user.setProfileImageUrl(sp.getString("user.avatar", ""));
        user.setNickName(sp.getString("user.name", ""));

        return user;
    }

}
