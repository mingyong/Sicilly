package xyz.shaohui.sicilly.utils;

import com.crashlytics.android.Crashlytics;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by shaohui on 16/8/20.
 */
public class ErrorUtils {

    public static void catchException(Throwable throwable) {
        throwable.printStackTrace();
        Crashlytics.logException(throwable);
        if (throwable instanceof HttpException) {
            HttpException exception = (HttpException) throwable;
        }
    }
}
