package xyz.shaohui.sicilly.views.home.timeline;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.OnClick;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import me.shaohui.vistarecyclerview.decoration.SpacingDecoration;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.event.HomeUpdateEvent;
import xyz.shaohui.sicilly.views.create_status.CreateStatusActivity;
import xyz.shaohui.sicilly.views.feed.BaseFeedFragment;
import xyz.shaohui.sicilly.views.feed.FeedMVP;
import xyz.shaohui.sicilly.views.feed.adapter.SimpleFeedAdapter;
import xyz.shaohui.sicilly.views.home.di.HomeComponent;
import xyz.shaohui.sicilly.views.search.SearchActivity;

@FragmentWithArgs
public class HomeTimelineFragment extends BaseFeedFragment<FeedMVP.View, HomeTimelinePresenterImpl>
        implements FeedMVP.View {

    private int mPage = 1;

    private final static int PRE_LOAD = 6;

    @BindView(R.id.title_bar)
    LinearLayout titleBar;

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
        SimpleFeedAdapter mAdapter = new SimpleFeedAdapter(getContext(), mStatusList, this,
                getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpacingDecoration(8));
        mRecyclerView.setOnMoreListener((total, left, current) -> {
            if (mStatusList.size() > 0) {
                presenter.loadMoreMessage(++mPage, mStatusList.get(mStatusList.size() - 1));
            }
        }, PRE_LOAD);
        mRecyclerView.setRefreshListener(() -> presenter.loadMessage());

        // 加载数据
        presenter.loadMessage();
        mRecyclerView.setRefreshing(true);
    }

    @OnClick(R.id.btn_add)
    void btnAdd() {
        startActivity(new Intent(getActivity(), CreateStatusActivity.class));
    }

    @OnClick(R.id.btn_search)
    void btnSearch() {
        startActivity(new Intent(getContext(), SearchActivity.class));
    }

    @OnClick(R.id.img_icon)
    void imgIconClick() {
        mRecyclerView.getRecycler().smoothScrollToPosition(0);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateHome(HomeUpdateEvent event) {
        mRecyclerView.getRecycler().scrollToPosition(0);
        presenter.loadMessage();
        mRecyclerView.setRefreshing(true);
    }
}
