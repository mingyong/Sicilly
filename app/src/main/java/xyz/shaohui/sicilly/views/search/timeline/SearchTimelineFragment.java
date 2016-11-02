package xyz.shaohui.sicilly.views.search.timeline;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import butterknife.BindView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.shaohui.sicillylib.utils.ToastUtils;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import me.shaohui.vistarecyclerview.decoration.SpacingDecoration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.views.home.timeline.TimelineItemListener;
import xyz.shaohui.sicilly.views.search.di.SearchComponent;
import xyz.shaohui.sicilly.views.search.event.SearchTimelineEvent;
import xyz.shaohui.sicilly.views.status_detail.StatusDetailAdapter;

/**
 * Created by shaohui on 2016/11/1.
 */

public class SearchTimelineFragment
        extends BaseFragment<SearchTimelineMVP.View, SearchTimelineMVP.Presenter>
        implements SearchTimelineMVP.View, TimelineItemListener {

    @Inject
    EventBus mBus;

    @BindView(R.id.recycler)
    VistaRecyclerView mRecyclerView;

    private List<Status> mStatusList;

    private String key;

    @NonNull
    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public void injectDependencies() {
        SearchComponent component = getComponent(SearchComponent.class);
        component.inject(this);
        presenter = component.timelinePresenter();
    }

    @Override
    public void bindViews(View view) {
        mStatusList = new ArrayList<>();
        StatusDetailAdapter adapter =
                new StatusDetailAdapter(mStatusList, this, getFragmentManager());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new SpacingDecoration(8));
        mRecyclerView.setOnMoreListener((total, left, current) -> {
            if (mStatusList.size() > 0) {
                presenter.loadStatus(key, mStatusList.get(mStatusList.size() - 1).rawid());
            }
        }, 6);
        mRecyclerView.setRefreshListener(() -> {
            presenter.loadStatus(key, null);
        });
    }

    private void performSearch() {
        mRecyclerView.getRecycler().scrollToPosition(0);
        mRecyclerView.setRefreshing(true);
        presenter.loadStatus(key, null);
    }

    @Override
    public int layoutRes() {
        return R.layout.fragment_timeline;
    }

    @Override
    public void loadDataSuccess(List<Status> statuses) {
        mStatusList.clear();
        mStatusList.addAll(statuses);
        mRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void loadMoreSuccess(List<Status> statuses) {
        mStatusList.addAll(statuses);
        mRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void loadDataFailure() {
        mRecyclerView.showErrorView();
    }

    @Override
    public void loadEmpty() {
        mRecyclerView.showEmptyView();
    }

    @Override
    public void loadMoreError() {
        mRecyclerView.loadMoreFailure();
    }

    @Override
    public void loadNoMore() {
        mRecyclerView.loadNoMore();
    }

    @Override
    public void opStar(Status status, int position) {

    }

    @Override
    public void opDelete(Status status, int position) {
        ToastUtils.showToast(getContext(), "暂不支持从搜索结果删除");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void subscribeSearch(SearchTimelineEvent event) {
        if (!TextUtils.equals(event.getKey(), key)) {
            key = event.getKey();
            performSearch();
        }
    }
}
