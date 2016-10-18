package xyz.shaohui.sicilly.views.friendship;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import butterknife.BindView;
import butterknife.OnClick;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.shaohui.sicillylib.utils.ToastUtils;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import me.shaohui.vistarecyclerview.decoration.DividerDecoration;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.views.friendship.di.FriendshipComponent;
import xyz.shaohui.sicilly.views.friendship.mvp.FriendRequestMVP;

/**
 * Created by shaohui on 16/10/18.
 */

public class FriendRequestFragment
        extends BaseFragment<FriendRequestMVP.View, FriendRequestMVP.Presenter>
        implements FriendRequestMVP.View {

    @Inject
    EventBus mBus;

    @BindView(R.id.recycler)
    VistaRecyclerView mRecyclerView;

    private List<User> mUserList;

    private int mPage = 1;

    @NonNull
    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public void injectDependencies() {
        FriendshipComponent component = getComponent(FriendshipComponent.class);
        component.inject(this);
        presenter = component.friendRequestPresenter();
    }

    @Override
    public int layoutRes() {
        return R.layout.fragment_friend_request;
    }

    @Override
    public void bindViews(View view) {
        mUserList = new ArrayList<>();
        FriendRequestAdapter adapter =
                new FriendRequestAdapter(mUserList, new FriendRequestAdapter.OpListener() {

                    @Override
                    public void acceptRequest(int position, User user) {
                        presenter.acceptRequest(position, user.id());
                    }

                    @Override
                    public void denyRequest(int position, User user) {
                        presenter.denyRequest(position, user.id());
                    }
                });
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(
                new DividerDecoration(Color.parseColor("#F8F8F8"), 2, 8, 8));
        mRecyclerView.setOnMoreListener((total, left, current) -> presenter.loadRequest(++mPage),
                6);

        presenter.loadRequest(mPage);
    }

    @OnClick(R.id.btn_back)
    void btnBack() {
        getActivity().finish();
    }

    @Override
    public void loadRequestSuccess(List<User> users) {
        mUserList.addAll(users);
    }

    @Override
    public void loadError() {
        mRecyclerView.showErrorView();
    }

    @Override
    public void loadEmpty() {
        mRecyclerView.showEmptyView();
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
    public void acceptRequestSuccess(int position, String uid) {
        mUserList.remove(position);
        mRecyclerView.getAdapter().notifyItemRemoved(position);
    }

    @Override
    public void denyRequestSuccess(int position, String uid) {
        mUserList.remove(position);
        mRecyclerView.getAdapter().notifyItemRemoved(position);
    }

    @Override
    public void opFailed() {
        ToastUtils.showToast(getContext(), R.string.friend_request_op_failed);
    }
}
