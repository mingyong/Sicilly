package xyz.shaohui.sicilly.views.home.chat.mention;

import android.view.View;
import me.shaohui.vistarecyclerview.decoration.SpacingDecoration;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.event.MentionUpdateEvent;
import xyz.shaohui.sicilly.views.feed.BaseFeedFragment;
import xyz.shaohui.sicilly.views.feed.FeedMVP;
import xyz.shaohui.sicilly.views.feed.adapter.SimpleFeedAdapter;
import xyz.shaohui.sicilly.views.home.di.HomeComponent;

/**
 * Created by shaohui on 2016/11/27.
 */

public class MentionFragment extends BaseFeedFragment<FeedMVP.View, MentionPresenter> {

    private int mPage = 1;

    @Override
    public void injectDependencies() {
        HomeComponent component = getComponent(HomeComponent.class);
        component.inject(this);
        presenter = component.mentionPresenter();
    }

    @Override
    public void bindViews(View view) {
        SimpleFeedAdapter adapter = new SimpleFeedAdapter(mStatusList, this, getFragmentManager());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setRefreshListener(() -> presenter.loadMessage());
        mRecyclerView.addItemDecoration(new SpacingDecoration(8));
        mRecyclerView.setOnMoreListener((total, left, current) -> {
            if (mStatusList.size() > 0) {
                presenter.loadMoreMessage(++mPage, mStatusList.get(mStatusList.size() - 1));
            }
        }, 6);

        // 首次加载
        mRecyclerView.setRefreshing(true);
        presenter.loadMessage();
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_mention;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateMention(MentionUpdateEvent event) {
        mRecyclerView.getRecycler().scrollToPosition(0);
        presenter.loadMessage();
        mRecyclerView.setRefreshing(true);
    }
}
