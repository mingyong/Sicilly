package xyz.shaohui.sicilly.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by shaohui on 16/10/15.
 */

public class SimpleUtils {

    public static void opMarket(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + context.getPackageName()));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

}
