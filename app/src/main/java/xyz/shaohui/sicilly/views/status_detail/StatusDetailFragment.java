package xyz.shaohui.sicilly.views.status_detail;

import android.view.View;
import butterknife.OnClick;
import me.shaohui.vistarecyclerview.decoration.SpacingDecoration;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.views.feed.BaseFeedFragment;
import xyz.shaohui.sicilly.views.feed.FeedMVP;
import xyz.shaohui.sicilly.views.feed.adapter.SimpleFeedAdapter;
import xyz.shaohui.sicilly.views.status_detail.di.StatusDetailComponent;

/**
 * Created by shaohui on 16/9/17.
 */

public class StatusDetailFragment extends BaseFeedFragment<FeedMVP.View, StatusDetailPresenterImpl>
        implements FeedMVP.View {

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
        SimpleFeedAdapter mAdapter = new SimpleFeedAdapter(mStatusList, this, getFragmentManager());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpacingDecoration(8));

        presenter.loadMessage();
    }

    @OnClick(R.id.btn_back)
    void btnBack() {
        getActivity().finish();
    }
}
