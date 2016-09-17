package xyz.shaohui.sicilly.utils;

import android.text.format.DateUtils;
import java.util.Date;

/**
 * Created by shaohui on 16/8/19.
 */
public class TimeUtils {

    public static String simpleFormat(Date date) {
        return DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(),
                DateUtils.MINUTE_IN_MILLIS).toString();
    }
}
