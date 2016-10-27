package xyz.shaohui.sicilly.views.public_square;

import android.support.annotation.NonNull;
import android.view.View;
import butterknife.BindView;
import butterknife.OnClick;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.inject.Inject;
import me.shaohui.sicillylib.utils.ToastUtils;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.views.home.timeline.TimelineItemListener;
import xyz.shaohui.sicilly.views.home.timeline.adapter.IndexStatusAdapter;
import xyz.shaohui.sicilly.views.public_square.di.PublicSquareComponent;
import xyz.shaohui.sicilly.views.public_square.mvp.PublicSquareMVP;

/**
 * Created by shaohui on 2016/10/27.
 */

public class PublicSquareFragment
        extends BaseFragment<PublicSquareMVP.View, PublicSquareMVP.Presenter>
        implements PublicSquareMVP.View, TimelineItemListener {

    @Inject
    EventBus mBus;

    @BindView(R.id.recycler)
    VistaRecyclerView mRecyclerView;

    private List<Status> mStatusList;

    @NonNull
    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public void injectDependencies() {
        PublicSquareComponent component = getComponent(PublicSquareComponent.class);
        component.inject(this);
        presenter = component.presenter();
    }

    @Override
    public void bindViews(View view) {
        mStatusList = new ArrayList<>();
        IndexStatusAdapter adapter =
                new IndexStatusAdapter(mStatusList, this, getFragmentManager());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setRefreshListener(() -> presenter.loadStatus(null));

        // 首次加载
        mRecyclerView.setRefreshing(true);
        presenter.loadStatus(null);
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
        mRecyclerView.setRefreshing(true);
        presenter.loadStatus(null);
    }

    @Override
    public void opStar(Status status, int position) {
        mStatusList.set(position, Status.updateStatusStar(status));
        mRecyclerView.notifyDataSetChanged();
        presenter.opStar(status, position);
    }

    @Override
    public void opDelete(Status status, int position) {
        ToastUtils.showToast(getContext(), "随便看看暂不支持删除，请到个人中心操作");
    }

    @Override
    public void showStatus(List<Status> statuses) {
        if (!mStatusList.isEmpty()) {
            mStatusList.clear();
        }
        mStatusList.addAll(statuses);
        mRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void loadFail() {
        mRecyclerView.showErrorView();
    }

    @Override
    public void opStarFailure(int position) {
        mStatusList.set(position, Status.updateStatusStar(mStatusList.get(position)));
        mRecyclerView.notifyDataSetChanged();
        ToastUtils.showToast(getActivity(), R.string.op_star_failure);
    }
}
