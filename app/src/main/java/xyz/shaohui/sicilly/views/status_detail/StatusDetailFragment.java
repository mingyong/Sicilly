package xyz.shaohui.sicilly.views.status_detail;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import butterknife.BindView;
import com.afollestad.materialdialogs.MaterialDialog;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.shaohui.sicillylib.utils.ToastUtils;
import me.shaohui.vistarecyclerview.decoration.SpacingDecoration;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Retrofit;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.views.create_status.CreateStatusActivity;
import xyz.shaohui.sicilly.views.create_status.CreateStatusDialogBuilder;
import xyz.shaohui.sicilly.views.create_status.DialogController;
import xyz.shaohui.sicilly.views.home.timeline.TimelineItemListener;
import xyz.shaohui.sicilly.views.status_detail.di.StatusDetailComponent;
import xyz.shaohui.sicilly.views.status_detail.mvp.StatusDetailPresenter;
import xyz.shaohui.sicilly.views.status_detail.mvp.StatusDetailView;

/**
 * Created by shaohui on 16/9/17.
 */

@FragmentWithArgs
public class StatusDetailFragment extends BaseFragment<StatusDetailView, StatusDetailPresenter>
        implements StatusDetailView, TimelineItemListener, DialogController {

    @Inject
    EventBus mBus;

    @Inject
    Retrofit mRetrofit;

    @Arg
    Status mOriginStatus;

    private List<Status> mDataList;

    private StatusDetailAdapter mAdapter;

    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @NonNull
    @Override
    public EventBus getBus() {
        return mBus;
    }

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
        mDataList = new ArrayList<>();
        mAdapter = new StatusDetailAdapter(mDataList, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addItemDecoration(new SpacingDecoration(8));
        mRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        presenter.loadStatus(mOriginStatus);
    }

    @Override
    public void showStatus(List<Status> statuses) {
        if (!mDataList.isEmpty()) {
            mDataList.clear();
        }
        mDataList.addAll(statuses);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void deleteStatusFailure(Status status, int position) {
        mDataList.set(position, status);
        mAdapter.notifyDataSetChanged();
        ToastUtils.showToast(getActivity(), R.string.delete_status_failure);
    }

    @Override
    public void opAvatar() {

    }

    @Override
    public void opContent(Status status) {

    }

    @Override
    public void opStar(Status status, int position) {

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
                    mAdapter.notifyDataSetChanged();
                    presenter.deleteMessage(status, position);
                })
                .show();
    }

    @Override
    public Retrofit getRetrofit() {
        return mRetrofit;
    }
}
