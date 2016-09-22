package xyz.shaohui.sicilly.views.user_info;

import android.support.annotation.NonNull;
import android.view.View;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.views.user_info.di.UserInfoComponent;

/**
 * Created by shaohui on 16/9/18.
 */

public class PrivacyFragment extends BaseFragment<MvpView, MvpBasePresenter<MvpView>> {

    @Inject
    EventBus mBus;

    @NonNull
    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public void injectDependencies() {
        UserInfoComponent component = getComponent(UserInfoComponent.class);
        component.inject(this);
    }

    @Override
    public int layoutRes() {
        return R.layout.fragment_user_privacy;
    }

    @Override
    public void bindViews(View view) {
        super.bindViews(view);
    }
}
