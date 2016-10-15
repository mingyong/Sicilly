package xyz.shaohui.sicilly.views.home.timeline;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.shaohui.sicillylib.utils.ToastUtils;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import me.shaohui.vistarecyclerview.decoration.SpacingDecoration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import retrofit2.Retrofit;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.utils.SimpleUtils;
import xyz.shaohui.sicilly.views.create_status.CreateStatusActivity;
import xyz.shaohui.sicilly.views.create_status.CreateStatusDialog;
import xyz.shaohui.sicilly.views.create_status.CreateStatusDialogBuilder;
import xyz.shaohui.sicilly.views.create_status.CreateStatusDialog_ViewBinder;
import xyz.shaohui.sicilly.views.create_status.CreateStatusFragment;
import xyz.shaohui.sicilly.views.home.di.HomeComponent;
import xyz.shaohui.sicilly.views.home.timeline.adapter.IndexStatusAdapter;
import xyz.shaohui.sicilly.views.home.timeline.mvp.HomeTimelinePresenter;
import xyz.shaohui.sicilly.views.home.timeline.mvp.HomeTimelineView;
import xyz.shaohui.sicilly.views.status_detail.StatusDetailActivity;

@FragmentWithArgs
public class HomeTimelineFragment extends BaseFragment<HomeTimelineView, HomeTimelinePresenter>
        implements HomeTimelineView, TimelineItemListener {

    public static final int TYPE_HOME = 1;
    public static final int TYPE_ABOUT_ME = 2;

    @Inject
    EventBus mBus;

    @Inject
    Retrofit mRetrofit;

    @Arg
    int mType;

    IndexStatusAdapter mAdapter;
    List<Status> mDataList;
    private int mPage = 1;

    private final static int PRE_LOAD = 6;

    @BindView(R.id.recycler)
    VistaRecyclerView mRecyclerView;
    @BindView(R.id.title_bar)
    LinearLayout titleBar;

    @NonNull
    @Override
    public EventBus getBus() {
        return mBus;
    }

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
        mDataList = new ArrayList<>();
        mAdapter = new IndexStatusAdapter(mDataList, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpacingDecoration(8));
        mRecyclerView.setOnMoreListener((total, left, current) -> {
            if (mDataList.size() > 0) {
                presenter.loadMoreMessage(++mPage, mDataList.get(mDataList.size() - 1), mType);
            }
        }, PRE_LOAD);
        mRecyclerView.setRefreshListener(() -> presenter.loadMessage(mType));

        // 加载数据
        presenter.loadMessage(mType);
        mRecyclerView.setRefreshing(true);

        // 除home外 其他隐藏TitleBar
        if (mType != TYPE_HOME) {
            titleBar.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams =
                    (RelativeLayout.LayoutParams) mRecyclerView.getLayoutParams();
            layoutParams.topMargin = 0;
        }
    }

    @OnClick(R.id.btn_add)
    void btnAdd() {
        startActivity(new Intent(getActivity(), CreateStatusActivity.class));
    }

    @Override
    public void showMessage(List<Status> statuses) {
        mPage = 1;
        mRecyclerView.setRefreshing(false);
        if (statuses.size() > 0) {
            mDataList.clear();
            mDataList.addAll(statuses);
            mRecyclerView.notifyDataSetChanged();
        } else {
            mRecyclerView.showEmptyView();
        }
    }

    @Override
    public void showMoreMessage(List<Status> statuses) {
        if (statuses.size() > 0) {
            mDataList.addAll(statuses);
            mRecyclerView.notifyDataSetChanged();
        } else {
            mRecyclerView.loadNoMore();
        }
    }

    @OnClick(R.id.img_icon)
    void imgIconClick() {
        mRecyclerView.getRecycler().smoothScrollToPosition(0);
    }

    class User {
        public User(String userId) {
            user_id = userId;
        }

        private String user_id;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }
    }

    @Override
    public void showNewNotice() {

    }

    @Override
    public void showRefresh() {

    }

    @Override
    public void loadMoreFail() {
        mPage--;
        mRecyclerView.loadMoreFailure();
    }

    @Override
    public void networkError() {
        mRecyclerView.showErrorView();
    }

    @Override
    public void opStarFailure(int position) {
        mDataList.set(position, Status.updateStatusStar(mDataList.get(position)));
        mRecyclerView.notifyDataSetChanged();
        ToastUtils.showToast(getActivity(), R.string.op_star_failure);
    }

    @Override
    public void deleteStatusFailure(Status status, int position) {
        mDataList.set(position, status);
        mRecyclerView.notifyDataSetChanged();
        ToastUtils.showToast(getActivity(), R.string.delete_status_failure);
    }

    @Override
    public void opAvatar() {

    }

    @Override
    public void opContent(Status status) {
        startActivity(StatusDetailActivity.newIntent(getContext(), status));
    }

    @Override
    public void opStar(Status status, int position) {
        mDataList.set(position, Status.updateStatusStar(status));
        mRecyclerView.notifyDataSetChanged();
        presenter.opStar(status, position);
    }

    @Override
    public void opComment(Status status) {
        //startActivity(CreateStatusActivity.newIntent(getActivity(), status,
        //        CreateStatusActivity.TYPE_REPLY));
        new CreateStatusDialogBuilder(CreateStatusActivity.TYPE_REPLY)
                .originStatus(status).build().show(getFragmentManager());
    }

    @Override
    public void opRepost(Status status) {
        //startActivity(CreateStatusActivity.newIntent(getActivity(), status,
        //        CreateStatusActivity.TYPE_REPOST));
        new CreateStatusDialogBuilder(CreateStatusActivity.TYPE_REPOST)
                .originStatus(status).build().show(getFragmentManager());
    }

    @Override
    public void opDelete(Status status, int position) {
        new MaterialDialog.Builder(getActivity()).content(R.string.confirm_delete_status)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .onPositive((dialog, which) -> {
                    mDataList.remove(position);
                    mRecyclerView.notifyDataSetChanged();
                    presenter.deleteMessage(status, position);
                })
                .show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateStatus(Status status) {
        mDataList.add(0, status);
        mRecyclerView.notifyDataSetChanged();
    }
}
