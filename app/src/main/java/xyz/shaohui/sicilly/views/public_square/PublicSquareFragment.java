package xyz.shaohui.sicilly.views.public_square;

import android.view.View;

import butterknife.OnClick;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.views.feed.BaseFeedFragment;
import xyz.shaohui.sicilly.views.feed.FeedMVP;
import xyz.shaohui.sicilly.views.feed.adapter.SimpleFeedAdapter;
import xyz.shaohui.sicilly.views.public_square.di.PublicSquareComponent;

/**
 * Created by shaohui on 2016/10/27.
 */

public class PublicSquareFragment extends BaseFeedFragment<FeedMVP.View, PublicSquarePresenterImpl>
        implements FeedMVP.View {

    @Override
    public void injectDependencies() {
        PublicSquareComponent component = getComponent(PublicSquareComponent.class);
        component.inject(this);
        presenter = component.presenter();
    }

    @Override
    public void bindViews(View view) {
        SimpleFeedAdapter adapter = new SimpleFeedAdapter(getContext(), mStatusList, this,
                getFragmentManager());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setRefreshListener(() -> presenter.loadMessage());

        // 首次加载
        mRecyclerView.setRefreshing(true);
        presenter.loadMessage();
    }

    @Override
    public int layoutRes() {
        return R.layout.activity_public_square;
    }

    @OnClick(R.id.btn_back)
    void btnBack() {
        getActivity().finish();
    }

    @OnClick(R.id.action_refresh)
    void actionRefresh() {
        mRecyclerView.getRecycler().scrollToPosition(0);
        presenter.loadMessage();
        mRecyclerView.setRefreshing(true);
    }
}
