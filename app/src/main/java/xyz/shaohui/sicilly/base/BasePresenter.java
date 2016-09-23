package xyz.shaohui.sicilly.base;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import rx.Subscription;

/**
 * Created by shaohui on 16/9/23.
 */

public class BasePresenter<T extends MvpView> extends MvpBasePresenter<T> {

    public void addSubscribe(Subscription subscription) {

    }

}
