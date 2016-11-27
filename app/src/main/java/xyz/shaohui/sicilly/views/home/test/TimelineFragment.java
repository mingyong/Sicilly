package xyz.shaohui.sicilly.views.home.test;

import android.view.View;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.views.feed.BaseFeedFragment;
import xyz.shaohui.sicilly.views.feed.adapter.SimpleFeedAdapter;
import xyz.shaohui.sicilly.views.home.di.HomeComponent;
import xyz.shaohui.sicilly.views.home.test.mvp.TimelineMVP;

/**
 * Created by shaohui on 2016/11/27.
 */

public class TimelineFragment extends BaseFeedFragment<TimelineMVP.View, TimelineMVP.Presenter> {

    @Override
    public void injectDependencies() {
        HomeComponent component = getComponent(HomeComponent.class);
        component.inject(this);
        presenter = component.testTimelinePresenter();
    }

    @Override
    public void bindViews(View view) {
        SimpleFeedAdapter mAdapter =
                new SimpleFeedAdapter(mStatusList, this, getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);

        presenter.loadMessage();
        mRecyclerView.setRefreshing(true);
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_feed;
    }
}
