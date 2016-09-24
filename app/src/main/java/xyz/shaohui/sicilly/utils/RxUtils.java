package xyz.shaohui.sicilly.utils;

import rx.functions.Action1;

/**
 * Created by shaohui on 16/9/24.
 */

public class RxUtils {

    public static Action1<Throwable> ignoreNetError = new Action1<Throwable>() {
        @Override
        public void call(Throwable throwable) {
            throwable.printStackTrace();
        }
    };

}
