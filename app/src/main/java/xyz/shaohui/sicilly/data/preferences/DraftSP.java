package xyz.shaohui.sicilly.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by kpt on 16/3/26.
 */
public class DraftSP {

    public static void saveDraft(Context context, String text) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        sp.edit().putString("text", text).apply();
    }

    public static String loadDraft(Context context) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String str = sp.getString("text", "");
        saveDraft(context, "");
        return str;
    }

}
