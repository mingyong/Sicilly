package xyz.shaohui.sicilly.views.timeline;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import me.shaohui.sicillylib.utils.ToastUtils;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import me.shaohui.vistarecyclerview.decoration.SpacingDecoration;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Retrofit;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.views.create_status.DialogController;
import xyz.shaohui.sicilly.views.home.timeline.TimelineItemListener;
import xyz.shaohui.sicilly.views.status_detail.StatusDetailAdapter;
import xyz.shaohui.sicilly.views.timeline.di.TimelineComponent;
import xyz.shaohui.sicilly.views.timeline.di.TimelineModule;
import xyz.shaohui.sicilly.views.timeline.mvp.TimelineMVP;

/**
 * Created by shaohui on 2016/10/19.
 */

public class TimelineFragment extends BaseFragment<TimelineMVP.View, TimelineMVP.Presenter>
        implements TimelineMVP.View, TimelineItemListener, DialogController {

    @Inject
    EventBus mBus;

    @Inject
    Retrofit mRetrofit;

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
        StatusDetailAdapter adapter =
                new StatusDetailAdapter(mStatusList, this, getFragmentManager());
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

    @Override
    public void deleteStatusFailure(Status status, int position) {
        mStatusList.set(position, status);
        mRecyclerView.notifyDataSetChanged();
        ToastUtils.showToast(getActivity(), R.string.delete_status_failure);
    }

    @Override
    public void opStar(Status status, int position) {

    }

    @Override
    public void opDelete(Status status, int position) {
        new MaterialDialog.Builder(getActivity()).content(R.string.confirm_delete_status)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .onPositive((dialog, which) -> {
                    mStatusList.remove(position);
                    mRecyclerView.notifyDataSetChanged();
                    presenter.deleteMessage(status, position);
                })
                .show();
    }

    @Override
    public Retrofit getRetrofit() {
        return mRetrofit;
    }
}
