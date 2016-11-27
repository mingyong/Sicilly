package xyz.shaohui.sicilly.views.search.user;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import butterknife.BindView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import me.shaohui.vistarecyclerview.decoration.SpacingDecoration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.views.friend_list.FriendListAdapter;
import xyz.shaohui.sicilly.views.search.di.SearchComponent;
import xyz.shaohui.sicilly.views.search.event.SearchUserEvent;
import xyz.shaohui.sicilly.views.user_info.UserActivity;

/**
 * Created by shaohui on 2016/11/1.
 */

public class SearchUserFragment extends BaseFragment<SearchUserMVP.View, SearchUserMVP.Presenter>
        implements SearchUserMVP.View, FriendListAdapter.Action {

    @Inject
    EventBus mBus;

    @BindView(R.id.recycler)
    VistaRecyclerView mRecyclerView;

    private List<User> mStatusList;

    private String key;

    private int mPage = 1;

    @NonNull
    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public void injectDependencies() {
        SearchComponent component = getComponent(SearchComponent.class);
        component.inject(this);
    }

    @Override
    public void bindViews(View view) {
        mStatusList = new ArrayList<>();
        FriendListAdapter adapter = new FriendListAdapter(mStatusList, false, this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(new SpacingDecoration(8));
        mRecyclerView.setOnMoreListener((total, left, current) -> {
            if (mStatusList.size() > 0) {
                presenter.loadStatus(key, ++mPage);
            }
        }, 6);
        mRecyclerView.setRefreshListener(() -> {
            mPage = 1;
            presenter.loadStatus(key, mPage);
        });
    }

    private void performSearch() {
        mRecyclerView.getRecycler().scrollToPosition(0);
        mPage = 1;
        presenter.loadStatus(key, mPage);
        Log.i("TAG", "key: " + key);
        mRecyclerView.setRefreshing(true);
    }

    @Override
    public int layoutRes() {
        return R.layout.fragment_timeline;
    }

    @Override
    public void loadDataSuccess(List<User> statuses) {
        mStatusList.clear();
        mStatusList.addAll(statuses);
        mRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void loadMoreSuccess(List<User> statuses) {
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
    public void actionFollow(int position, User user) {

    }

    @Override
    public void actionClick(User user) {
        startActivity(UserActivity.newIntent(getContext(), user.id()));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void subscribeUserEvent(SearchUserEvent event) {
        if (!TextUtils.equals(key, event.getKey())) {
            key = event.getKey();
            performSearch();
        }
    }
}
