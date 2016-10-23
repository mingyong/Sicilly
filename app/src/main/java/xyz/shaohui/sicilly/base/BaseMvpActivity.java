package xyz.shaohui.sicilly.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.app.di.AppComponent;

/**
 * Created by shaohui on 16/9/18.
 */

public abstract class BaseMvpActivity<V extends MvpView, T extends MvpPresenter<V>>
        extends MvpActivity<V, T> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        injectDependencies();
        super.onCreate(savedInstanceState);
        try {
            if (!getBus().isRegistered(this)) {
                getBus().register(this);
            }
        } catch (Exception ignored) {
        }
    }

    @Override
    public T createPresenter() {
        return presenter;
    }

    public AppComponent getAppComponent() {
        return SicillyApplication.getAppComponent();
    }

    @NonNull
    public abstract EventBus getBus();

    public abstract void injectDependencies();
}
