package xyz.shaohui.sicilly.views.user_info.timeline;

import android.support.annotation.NonNull;
import android.view.View;
import butterknife.BindView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.shaohui.scrollablelayout.ScrollableHelper;
import me.shaohui.sicillylib.utils.ToastUtils;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import me.shaohui.vistarecyclerview.decoration.SpacingDecoration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.views.create_status.CreateStatusActivity;
import xyz.shaohui.sicilly.views.home.timeline.TimelineItemListener;
import xyz.shaohui.sicilly.views.home.timeline.adapter.IndexStatusAdapter;
import xyz.shaohui.sicilly.views.status_detail.StatusDetailActivity;
import xyz.shaohui.sicilly.views.user_info.di.UserInfoComponent;
import xyz.shaohui.sicilly.views.user_info.timeline.mvp.UserTimelinePresenter;
import xyz.shaohui.sicilly.views.user_info.timeline.mvp.UserTimelineView;

/**
 * Created by shaohui on 16/9/18.
 */

@FragmentWithArgs
public class UserTimelineFragment extends BaseFragment<UserTimelineView, UserTimelinePresenter>
        implements UserTimelineView, TimelineItemListener, ScrollableHelper.ScrollableContainer {

    @Inject
    EventBus mBus;

    @Arg
    String mUserId;

    IndexStatusAdapter mAdapter;
    List<Status> mDataList;
    private int mPage = 1;

    private final static int PRE_LOAD = 6;

    @BindView(R.id.recycler)
    VistaRecyclerView mRecyclerView;

    @NonNull
    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public void injectDependencies() {
        UserInfoComponent component = getComponent(UserInfoComponent.class);
        component.inject(this);
        presenter = component.userTimelinePresenter();
    }

    @Override
    public int layoutRes() {
        return R.layout.fragment_user_timeline;
    }

    @Override
    public void bindViews(View view) {
        mDataList = new ArrayList<>();
        mAdapter = new IndexStatusAdapter(mDataList, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpacingDecoration(8));
        mRecyclerView.setOnMoreListener((total, left, current) -> {
            if (mDataList.size() > 0) {
                presenter.loadMoreStatus(mUserId, ++mPage, mDataList.get(mDataList.size() - 1));
            }
        }, PRE_LOAD);

        // 加载数据
        presenter.loadStatus(mUserId);
    }

    @Override
    public void showMessage(List<Status> statuses) {
        mPage = 1;
        mRecyclerView.setRefreshing(false);
        if (statuses.size() > 0) {
            mDataList.clear();
            mDataList.addAll(statuses);
            mRecyclerView.notifyDataSetChanged();
        } else {
            mRecyclerView.showEmptyView();
        }
    }

    @Override
    public void showMoreMessage(List<Status> statuses) {
        if (statuses.size() > 0) {
            mDataList.addAll(statuses);
            mRecyclerView.notifyDataSetChanged();
        } else {
            mRecyclerView.loadNoMore();
        }
    }

    @Override
    public void loadMoreFailure() {
        mPage--;
        mRecyclerView.loadMoreFailure();
    }

    @Override
    public void networkError() {
        mRecyclerView.showErrorView();
    }

    @Override
    public void opStarFailure(int position) {
        mDataList.set(position, Status.updateStatusStar(mDataList.get(position)));
        mRecyclerView.notifyDataSetChanged();
        ToastUtils.showToast(getActivity(), R.string.op_star_failure);
    }

    @Override
    public void deleteStatusFailure(Status status, int position) {
        mDataList.set(position, status);
        mRecyclerView.notifyDataSetChanged();
        ToastUtils.showToast(getActivity(), R.string.delete_status_failure);
    }

    @Override
    public void opAvatar() {

    }

    @Override
    public void opContent(Status status) {
        startActivity(StatusDetailActivity.newIntent(getContext(), status));
    }

    @Override
    public void opStar(Status status, int position) {
        mDataList.set(position, Status.updateStatusStar(status));
        mRecyclerView.notifyDataSetChanged();
        presenter.opStar(status, position);
    }

    @Override
    public void opComment(Status status) {
        startActivity(CreateStatusActivity.newIntent(getActivity(), status,
                CreateStatusActivity.TYPE_REPLY));
    }

    @Override
    public void opRepost(Status status) {
        startActivity(CreateStatusActivity.newIntent(getActivity(), status,
                CreateStatusActivity.TYPE_REPOST));
    }

    @Override
    public void opDelete(Status status, int position) {
        new MaterialDialog.Builder(getActivity()).content(R.string.confirm_delete_status)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .onPositive((dialog, which) -> {
                    mDataList.remove(position);
                    mRecyclerView.notifyDataSetChanged();
                    presenter.deleteStatus(status, position);
                })
                .show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateStatus(Status status) {
        mDataList.add(0, status);
        mRecyclerView.notifyDataSetChanged();
    }

    @Override
    public View getScrollableView() {
        if (mRecyclerView != null) {
            return mRecyclerView.getRecycler();
        }
        return null;
    }
}
