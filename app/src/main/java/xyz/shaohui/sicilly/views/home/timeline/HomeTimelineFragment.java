package xyz.shaohui.sicilly.views.home.timeline;

import android.support.annotation.NonNull;
import android.view.View;
import butterknife.BindView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import me.shaohui.vistarecyclerview.decoration.SpacingDecoration;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.views.home.di.HomeComponent;
import xyz.shaohui.sicilly.views.home.timeline.adapter.IndexStatusAdapter;
import xyz.shaohui.sicilly.views.home.timeline.mvp.HomeTimelinePresenter;
import xyz.shaohui.sicilly.views.home.timeline.mvp.HomeTimelineView;

public class HomeTimelineFragment extends BaseFragment<HomeTimelineView, HomeTimelinePresenter>
        implements HomeTimelineView, TimelineItemListener {

    @Inject
    EventBus mBus;

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
        HomeComponent component = getComponent(HomeComponent.class);
        component.inject(this);
        presenter = component.timelinePresenter();
    }

    @Override
    public int layoutRes() {
        return R.layout.fragment_home_timeline;
    }

    @Override
    public void bindViews(View view) {
        mDataList = new ArrayList<>();
        mAdapter = new IndexStatusAdapter(mDataList, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpacingDecoration(8));
        mRecyclerView.setOnMoreListener((total, left, current) -> {
            if (mDataList.size() > 0) {
                presenter.loadMoreMessage(++mPage,
                        mDataList.get(mDataList.size() - 1));
            }
        }, PRE_LOAD);
        mRecyclerView.setRefreshListener(() -> presenter.loadMessage());

        // 加载数据
        presenter.loadMessage();
        mRecyclerView.setRefreshing(true);
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
    public void showNewNotice() {

    }

    @Override
    public void showRefresh() {

    }

    @Override
    public void loadMoreFail() {

    }

    @Override
    public void networkError() {
        mRecyclerView.showErrorView();
    }

    @Override
    public void opAvatar() {

    }

    @Override
    public void opContent() {

    }

    @Override
    public void opStar() {

    }

    @Override
    public void opComment() {

    }

    @Override
    public void opRepost() {

    }

    @Override
    public void opDelete() {

    }
}
