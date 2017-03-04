package xyz.shaohui.sicilly.views.home.choice;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import android.view.View;
import butterknife.OnClick;
import me.shaohui.vistarecyclerview.decoration.SpacingDecoration;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.event.ChoiceUpdateEvent;
import xyz.shaohui.sicilly.views.feed.BaseFeedFragment;
import xyz.shaohui.sicilly.views.feed.FeedMVP;
import xyz.shaohui.sicilly.views.feed.adapter.SimpleFeedAdapter;
import xyz.shaohui.sicilly.views.home.di.HomeComponent;

/**
 * Created by shaohui on 2017/1/10.
 */

public class ChoiceFragment extends BaseFeedFragment<FeedMVP.View, ChoicePresenterImpl> implements FeedMVP.View {

    @Override
    public void injectDependencies() {
        HomeComponent component = getComponent(HomeComponent.class);
        component.inject(this);
        presenter = component.choicePresenter();
    }

    @Override
    public void bindViews(View view) {
        SimpleFeedAdapter mAdapter = new SimpleFeedAdapter(getContext(), mStatusList, this,
                getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpacingDecoration(4));
        mRecyclerView.setRefreshListener(() -> presenter.loadMessage());

        // 加载数据
        presenter.loadMessage();
        mRecyclerView.setRefreshing(true);
    }

    @OnClick(R.id.action_refresh)
    void refresh() {
        mRecyclerView.getRecycler().scrollToPosition(0);
        mRecyclerView.setRefreshing(true);
        presenter.loadMessage();
    }

    @OnClick(R.id.text_title)
    void scrollToTop() {
        mRecyclerView.getRecycler().scrollToPosition(0);
    }

    @Override
    public int layoutRes() {
        return R.layout.fragment_choice;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void subscribeUpdateEvent(ChoiceUpdateEvent event) {
        mRecyclerView.getRecycler().scrollToPosition(0);
        mRecyclerView.setRefreshing(true);
        presenter.loadMessage();
    }
}
