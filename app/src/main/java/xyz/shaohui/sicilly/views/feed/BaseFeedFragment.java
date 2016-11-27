package xyz.shaohui.sicilly.views.feed;

import android.support.annotation.NonNull;
import butterknife.BindView;
import com.afollestad.materialdialogs.MaterialDialog;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.shaohui.sicillylib.utils.ToastUtils;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.views.home.timeline.TimelineItemListener;

/**
 * Created by shaohui on 2016/11/26.
 */

public abstract class BaseFeedFragment<V extends FeedMVP.View, P extends FeedMVP.Presenter<V>>
        extends BaseFragment<V, P> implements FeedMVP.View, FeedItemListener {

    @BindView(R.id.recycler)
    public VistaRecyclerView mRecyclerView;

    protected List<Status> mStatusList = new ArrayList<>();

    @Inject
    EventBus mBus;

    @NonNull
    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public int layoutRes() {
        return R.layout.layout_feed;
    }

    @Override
    public void opStar(Status status, int position) {
        mStatusList.set(position, Status.updateStatusStar(status));
        mRecyclerView.notifyDataSetChanged();
        presenter.opStar(status, position);
    }

    @Override
    public void opDelete(Status status, int position) {
        new MaterialDialog.Builder(getActivity()).content(R.string.confirm_delete_status)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .onPositive((dialog, which) -> {
                    mStatusList.remove(position);
                    mRecyclerView.notifyDataSetChanged();
                    presenter.opDelete(status, position);
                })
                .show();
    }

    @Override
    public void showMessage(List<Status> statuses) {
        if (!mStatusList.isEmpty()) {
            mStatusList.clear();
        }
        mStatusList.addAll(statuses);
        mRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void showMoreMessage(List<Status> statuses) {
        mStatusList.addAll(statuses);
        mRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void showRefresh() {
        mRecyclerView.setRefreshing(true);
    }

    @Override
    public void networkError() {
        mRecyclerView.showErrorView();
    }

    @Override
    public void loadNoMore() {
        mRecyclerView.loadNoMore();
    }

    @Override
    public void loadMoreError() {
        mRecyclerView.loadMoreFailure();
    }

    @Override
    public void opStarFailure(int position) {
        mStatusList.set(position, Status.updateStatusStar(mStatusList.get(position)));
        mRecyclerView.notifyDataSetChanged();
        ToastUtils.showToast(getActivity(), R.string.op_star_failure);
    }

    @Override
    public void opDeleteFailure(Status status, int position) {
        mStatusList.set(position, status);
        mRecyclerView.notifyDataSetChanged();
        ToastUtils.showToast(getActivity(), R.string.delete_status_failure);
    }
}
