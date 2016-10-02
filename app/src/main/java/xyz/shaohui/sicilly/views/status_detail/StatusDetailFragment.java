package xyz.shaohui.sicilly.views.status_detail;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.views.status_detail.di.StatusDetailComponent;
import xyz.shaohui.sicilly.views.status_detail.mvp.StatusDetailPresenter;
import xyz.shaohui.sicilly.views.status_detail.mvp.StatusDetailView;

/**
 * Created by shaohui on 16/9/17.
 */

@FragmentWithArgs
public class StatusDetailFragment extends BaseFragment<StatusDetailView, StatusDetailPresenter>
        implements StatusDetailView {

    @Inject
    EventBus mBus;

    @Arg
    Status mOriginStatus;

    private List<Status> mDataList;

    private StatusDetailAdapter mAdapter;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @NonNull
    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public void injectDependencies() {
        StatusDetailComponent component = getComponent(StatusDetailComponent.class);
        component.inject(this);
        presenter = component.presenter();
    }

    @Override
    public int layoutRes() {
        return R.layout.fragment_status_detail;
    }

    @Override
    public void bindViews(View view) {
        mDataList = new ArrayList<>();
        mAdapter = new StatusDetailAdapter(mDataList);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        presenter.loadStatus(mOriginStatus);
    }

    @Override
    public void showStatus(List<Status> statuses) {
        if (!mDataList.isEmpty()) {
            mDataList.clear();
        }
        mDataList.addAll(statuses);
        mAdapter.notifyDataSetChanged();
    }
}
