package xyz.shaohui.sicilly.views.search.timeline;

import android.text.TextUtils;
import android.view.View;
import me.shaohui.vistarecyclerview.decoration.SpacingDecoration;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.views.feed.BaseFeedFragment;
import xyz.shaohui.sicilly.views.feed.adapter.SimpleFeedAdapter;
import xyz.shaohui.sicilly.views.search.di.SearchComponent;
import xyz.shaohui.sicilly.views.search.event.SearchTimelineEvent;

/**
 * Created by shaohui on 2016/11/1.
 */

public class SearchTimelineFragment
        extends BaseFeedFragment<SearchTimelineMVP.View, SearchTimelinePresenterImpl>
        implements SearchTimelineMVP.View {

    private String key;

    @Override
    public void injectDependencies() {
        SearchComponent component = getComponent(SearchComponent.class);
        component.inject(this);
        presenter = component.timelinePresenter();
    }

    @Override
    public void bindViews(View view) {
        SimpleFeedAdapter adapter = new SimpleFeedAdapter(mStatusList, this, getFragmentManager());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new SpacingDecoration(8));
        mRecyclerView.setOnMoreListener((total, left, current) -> {
            if (mStatusList.size() > 0) {
                presenter.loadMoreMessage(0, mStatusList.get(mStatusList.size() - 1));
            }
        }, 6);
        mRecyclerView.setRefreshListener(() -> presenter.loadMessage());
    }

    private void performSearch() {
        mRecyclerView.getRecycler().scrollToPosition(0);
        mRecyclerView.setRefreshing(true);
        presenter.loadMessage();
    }

    @Override
    public int layoutRes() {
        return R.layout.fragment_timeline;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void subscribeSearch(SearchTimelineEvent event) {
        if (!TextUtils.equals(event.getKey(), key)) {
            key = event.getKey();
            performSearch();
        }
    }

    @Override
    public String getKey() {
        return key;
    }
}
