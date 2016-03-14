package xyz.shaohui.sicilly.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by kpt on 16/3/2.
 */
public class TimeFormat {

    public static String format(String str) {
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM d hh:mm:ss Z yyyy", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = format.parse(str);
            long currentTime = System.currentTimeMillis();
            long dateTime = date.getTime();
            long s = (currentTime - dateTime) /1000;
            Log.i("TAG_currentTime", currentTime + "");
            Log.i("TAG_dateTime", dateTime + "");
            int m = (int) (s/60);
            if (m < 60){
                int c = m == 0?1:m;
                return c+"分钟前";
            } else if (m/60 < 24){
                int c = m/60 == 0?1:m/60;
                Log.i("TAG_S", s + "");
                Log.i("TAG_time", str);
                Log.i("TAG_format", date.toString());
                return c+"小时前";
            } else if (m/60/24 < 3){
                int c = m/60/24 == 0?1:m/60/24;
                return c+"天前";
            } else {
                return isYear(date);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static long toLong(String str) {
        SimpleDateFormat format = new SimpleDateFormat("EEE MMM d hh:mm:ss Z yyyy", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            Date date = format.parse(str);
            return date.getTime();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public  static String isYear(Date date){
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTime(date);
        dateCalendar.get(Calendar.YEAR);
        if (year == dateCalendar.get(Calendar.YEAR)){
            return dateToStringNoYear(date);
        } else {
            return dateToString(date);
        }

    }

    public static String dateToString(Date date){
        SimpleDateFormat format = new SimpleDateFormat("yyyy年M月d日 HH:mm");
        return  format.format(date);
    }

    public static String dateToStringNoYear(Date date){
        SimpleDateFormat format = new SimpleDateFormat("M月d日 HH:mm");
        return format.format(date);
    }

}
