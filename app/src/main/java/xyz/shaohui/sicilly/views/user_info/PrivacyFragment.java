package xyz.shaohui.sicilly.views.user_info;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import javax.inject.Inject;
import me.shaohui.scrollablelayout.ScrollableHelper;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.views.user_info.di.UserInfoComponent;

/**
 * Created by shaohui on 16/9/18.
 */

public class PrivacyFragment extends Fragment implements
        ScrollableHelper.ScrollableContainer {

    @Inject
    EventBus mBus;

    private View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user_privacy, container, false);
        mView = v;
        return v;
    }


    @Override
    public View getScrollableView() {
        return mView;
    }
}
