package xyz.shaohui.sicilly.views.timeline;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import me.shaohui.vistarecyclerview.decoration.SpacingDecoration;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.views.status_detail.StatusDetailAdapter;
import xyz.shaohui.sicilly.views.timeline.di.TimelineComponent;
import xyz.shaohui.sicilly.views.timeline.di.TimelineModule;
import xyz.shaohui.sicilly.views.timeline.mvp.TimelineMVP;

/**
 * Created by shaohui on 2016/10/19.
 */

public class TimelineFragment extends BaseFragment<TimelineMVP.View, TimelineMVP.Presenter>
        implements TimelineMVP.View {

    @Inject
    EventBus mBus;

    @Inject
    @Named(TimelineModule.TIMELINE_USER_ID)
    String mUserId;

    @Inject
    @Named(TimelineModule.TIMELINE_DATA_TYPE)
    int mDataType;

    @BindView(R.id.recycler)
    VistaRecyclerView mRecyclerView;

    @BindView(R.id.text_title)
    TextView mTitle;

    private List<Status> mStatusList;

    private int mPage = 1;

    @NonNull
    @Override
    public EventBus getBus() {
        return mBus;
    }

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

        mStatusList = new ArrayList<>();
        StatusDetailAdapter adapter = new StatusDetailAdapter(mStatusList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new SpacingDecoration(8));
        mRecyclerView.setOnMoreListener((total, left, current) -> {
            if (mStatusList.size() > 0) {
                presenter.loadStatus(++mPage);
            }
        }, 6);
        presenter.loadStatus(mPage);
    }

    @OnClick(R.id.btn_back)
    void btnBack() {
        getActivity().finish();
    }

    @Override
    public void loadDataSuccess(List<Status> statuses) {
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
}
