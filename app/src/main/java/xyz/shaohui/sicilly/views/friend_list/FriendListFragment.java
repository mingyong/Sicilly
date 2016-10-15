package xyz.shaohui.sicilly.views.friend_list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import me.shaohui.sicillylib.utils.ToastUtils;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import me.shaohui.vistarecyclerview.decoration.DividerDecoration;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.views.friend_list.di.FriendListComponent;
import xyz.shaohui.sicilly.views.friend_list.di.FriendListModule;
import xyz.shaohui.sicilly.views.friend_list.mvp.FriendListMVP;
import xyz.shaohui.sicilly.views.user_info.UserActivity;

/**
 * Created by shaohui on 16/10/14.
 */

@FragmentWithArgs
public class FriendListFragment extends BaseFragment<FriendListMVP.View, FriendListMVP.Presenter>
        implements FriendListMVP.View {

    @Inject
    EventBus mBus;

    @Inject
    @Named(FriendListModule.NAME_VIEW_TYPE)
    int mViewType;

    @Inject
    @Named(FriendListModule.NAME_DATA_TYPE)
    int mDataType;

    @Inject
    @Named(FriendListModule.NAME_USER_ID)
    String mUserId;

    @BindView(R.id.recycler)
    VistaRecyclerView mRecyclerView;

    @BindView(R.id.text_title)
    TextView title;

    List<User> mUserList;

    private int mPage = 1;

    @NonNull
    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public void injectDependencies() {
        FriendListComponent component = getComponent(FriendListComponent.class);
        component.inject(this);
        presenter = component.presenter();
    }

    @Override
    public void bindViews(View view) {
        mUserList = new ArrayList<>();
        FriendListAdapter adapter =
                new FriendListAdapter(mUserList, mViewType == FriendListActivity.VIEW_TYPE_TINY,
                        new FriendListAdapter.Action() {

                            @Override
                            public void actionFollow(int position, User user) {
                                presenter.followUser(position, user);
                            }

                            @Override
                            public void actionClick(User user) {
                                onItemClick(user);
                            }
                        });
        mRecyclerView.setAdapter(adapter);

        mRecyclerView.addItemDecoration(new DividerDecoration(R.color.window_background, 3));

        mRecyclerView.setOnMoreListener((total, left, current) -> {
            presenter.loadUser(++mPage);
        }, 6);

        // title
        String name;
        if (mUserId.equals(SicillyApplication.currentUId())) {
            name = getString(R.string.me);
        } else {
            name = getString(R.string.other);
        }
        if (mDataType == FriendListActivity.DATA_TYPE_FRIEND) {
            title.setText(String.format(getString(R.string.user_title_friend), name));
        } else {
            title.setText(String.format(getString(R.string.user_title_follower), name));
        }

        presenter.loadUser(mPage);
    }

    @Override
    public int layoutRes() {
        return R.layout.activity_friend_list;
    }

    private void onItemClick(User user) {
        if (mViewType == FriendListActivity.VIEW_TYPE_TINY) {
            Intent result = new Intent();
            result.putExtra(FriendListActivity.RESULT_DATA, user.screen_name());
            getActivity().setResult(Activity.RESULT_OK, result);
            getActivity().finish();
        } else {
            startActivity(UserActivity.newIntent(getContext(), user.id()));
        }
    }

    @OnClick(R.id.btn_back)
    void btnBack() {
        getActivity().finish();
    }

    @Override
    public void loadUserSuccess(List<User> users) {
        mUserList.addAll(users);
        mRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void followUserSuccess(int position, User user) {
        ToastUtils.showToast(getContext(), R.string.follow_success);
        user.updateFollow();
        mUserList.set(position, user);
        mRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void loadError() {
        mRecyclerView.showErrorView();
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
