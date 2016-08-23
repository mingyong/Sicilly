package xyz.shaohui.sicilly.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by shaohui on 16/8/19.
 */
public class TimeUtils {

    public static String simpleFormat(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm dd/MM", Locale.CHINA);
        return format.format(date);
    }

}
