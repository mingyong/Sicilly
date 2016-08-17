package me.shaohui.sicillylib.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by shaohui on 16/8/11.
 */
public class ToastUtils {

    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

}
