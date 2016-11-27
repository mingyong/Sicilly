package xyz.shaohui.sicilly.views.timeline;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import javax.inject.Inject;
import javax.inject.Named;
import me.shaohui.vistarecyclerview.decoration.SpacingDecoration;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.views.feed.BaseFeedFragment;
import xyz.shaohui.sicilly.views.feed.FeedMVP;
import xyz.shaohui.sicilly.views.feed.adapter.SimpleFeedAdapter;
import xyz.shaohui.sicilly.views.timeline.di.TimelineComponent;
import xyz.shaohui.sicilly.views.timeline.di.TimelineModule;

/**
 * Created by shaohui on 2016/10/19.
 */

public class TimelineFragment extends BaseFeedFragment<FeedMVP.View, TimelinePresenterImpl>
        implements FeedMVP.View {

    @Inject
    @Named(TimelineModule.TIMELINE_USER_ID)
    String mUserId;

    @Inject
    @Named(TimelineModule.TIMELINE_DATA_TYPE)
    int mDataType;

    @BindView(R.id.text_title)
    TextView mTitle;

    private int mPage = 1;

    @Override
    public void injectDependencies() {
        TimelineComponent component = getComponent(TimelineComponent.class);
        component.inject(this);
        presenter = component.presenter();
    }

    @Override
    public int layoutRes() {
        return R.layout.activity_timeline;
    }

    @Override
    public void bindViews(View view) {
        String name =
                TextUtils.equals(mUserId, SicillyApplication.currentUId()) ? getString(R.string.me)
                        : getString(R.string.other);
        if (mDataType == TimelineActivity.DATA_TYPE_TIMELINE) {
            mTitle.setText(String.format(getString(R.string.timeline_message_title), name));
        } else {
            mTitle.setText(String.format(getString(R.string.timeline_favorite_title), name));
        }

        SimpleFeedAdapter adapter = new SimpleFeedAdapter(mStatusList, this, getFragmentManager());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new SpacingDecoration(8));
        mRecyclerView.setOnMoreListener((total, left, current) -> {
            if (mStatusList.size() > 0) {
                presenter.loadMoreMessage(++mPage, mStatusList.get(mStatusList.size() - 1));
            }
        }, 6);
        presenter.loadMessage();
    }

    @OnClick(R.id.btn_back)
    void btnBack() {
        getActivity().finish();
    }
}
