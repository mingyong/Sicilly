package xyz.shaohui.sicilly.utils;

import xyz.shaohui.sicilly.SicillyFactory;

/**
 * Created by kpt on 16/3/9.
 */
public class Validate {

    public static boolean isFanFouUrl(String url) {
        if (url.startsWith(SicillyFactory.PREFIX_FAN_INDEX)) {
            String str = url.substring(SicillyFactory.PREFIX_FAN_INDEX.length());
            return str.contains("/")?false:true;

        }
        return false;
    }

    public static boolean isFanTrendUrl(String url) {
        return url.startsWith(SicillyFactory.PREFIX_FAN_TREND);
    }

    public static boolean isFanWebUrl(String url) {
        return url.startsWith(SicillyFactory.PREFIX_FAN_WEB);
    }

    public static boolean isUserUrl(String url) {
        return url.startsWith(SicillyFactory.PREFIX_USER);
    }

    public static boolean isTrendUrl(String url) {
        return url.startsWith(SicillyFactory.PREFIX_TREND);
    }

    public static boolean isWebUrl(String url) {
        return url.startsWith(SicillyFactory.PREFIX_WEB);
    }

}
