package xyz.shaohui.sicilly.utils;

import android.text.format.DateUtils;
import java.util.Date;
import xyz.shaohui.sicilly.SicillyApplication;

/**
 * Created by shaohui on 16/8/19.
 */
public class TimeUtils {

    public static String simpleFormat(Date date) {
        return DateUtils.getRelativeTimeSpanString(date.getTime(), System.currentTimeMillis(),
                DateUtils.SECOND_IN_MILLIS).toString();
    }

    public static String timeFormat(Date date) {
        int flag = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE;
        return DateUtils.formatDateTime(SicillyApplication.getContext(), date.getTime(), flag);
    }
}
