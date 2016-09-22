package xyz.shaohui.sicilly.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import org.greenrobot.eventbus.EventBus;

/**
 * Created by shaohui on 16/9/10.
 */

public abstract class BaseFragment<V extends MvpView, T extends MvpPresenter<V>>
        extends MvpFragment<V, T> {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
        injectDependencies();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(layoutRes(), container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        try {
            if (!getBus().isRegistered(this)) {
                getBus().register(this);
            }
        } catch (Exception e) {
        }
        bindViews(view);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public T createPresenter() {
        return presenter;
    }

    @SuppressWarnings("unchecked")
    public <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

    @NonNull
    public abstract EventBus getBus();

    public abstract void injectDependencies();

    @LayoutRes
    public abstract int layoutRes();

    public void bindViews(View view) {
    }
}
