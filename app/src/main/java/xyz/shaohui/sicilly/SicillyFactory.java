package xyz.shaohui.sicilly;

import xyz.shaohui.sicilly.data.services.RetrofitService;
import xyz.shaohui.sicilly.data.services.auth.OAuthToken;

/**
 * Created by kpt on 16/2/22.
 */
public class SicillyFactory {

    public static final boolean DEBUG = true;

    public static OAuthToken token;

    private static RetrofitService retrofitService;

    public static final int PAGE_COUNT = 30;

    public static final int PAGE_HOME = 1;
    public static final int PAGE_ABOUT_ME = 2;
    public static final int PAGE_PUBLIC = 3;

    public static final String PREFIX_USER = "user://";
    public static final String PREFIX_TREND = "trend://";
    public static final String PREFIX_WEB = "web://";
    public static final String PREFIX_FAN_INDEX = "http://fanfou.com/";
    public static final String PREFIX_FAN_TREND = "/q/";
    public static final String PREFIX_FAN_WEB = "http://";

    public static void setToken(OAuthToken token) {
        SicillyFactory.token = token;
    }

    public static OAuthToken getToken() {
        return token;
    }

    public static RetrofitService getRetrofitService() {
        if (retrofitService == null) {
            retrofitService = new RetrofitService();
        }
        return retrofitService;
    }
}
