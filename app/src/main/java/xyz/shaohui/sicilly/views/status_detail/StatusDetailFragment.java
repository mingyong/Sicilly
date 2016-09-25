package xyz.shaohui.sicilly.views.status_detail;

import android.support.annotation.NonNull;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.views.status_detail.mvp.StatusDetailView;

/**
 * Created by shaohui on 16/9/17.
 */

public class StatusDetailFragment extends BaseFragment implements StatusDetailView {
    @NonNull
    @Override
    public EventBus getBus() {
        return null;
    }

    @Override
    public void injectDependencies() {

    }

    @Override
    public int layoutRes() {
        return 0;
    }
}
