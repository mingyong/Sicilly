package xyz.shaohui.sicilly.views.home.chat;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.view.View;
import butterknife.BindView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import me.shaohui.sicillylib.utils.ToastUtils;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import me.shaohui.vistarecyclerview.decoration.DividerDecoration;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.data.models.Conversation;
import xyz.shaohui.sicilly.data.models.ConversationBean;
import xyz.shaohui.sicilly.event.HomeMessageEvent;
import xyz.shaohui.sicilly.event.MessageEvent;
import xyz.shaohui.sicilly.views.home.chat.adapter.MessageListAdapter;
import xyz.shaohui.sicilly.views.home.chat.mvp.MessageListPresenter;
import xyz.shaohui.sicilly.views.home.chat.mvp.MessageListView;
import xyz.shaohui.sicilly.views.home.di.HomeComponent;
import xyz.shaohui.sicilly.views.home.event.NoMentionEvent;
import xyz.shaohui.sicilly.views.home.event.NoMessageEvent;

public class MessageListFragment extends BaseFragment<MessageListView, MessageListPresenter>
        implements MessageListView {

    @BindView(R.id.recycler)
    VistaRecyclerView mRecyclerView;

    private List<ConversationBean> mDataList;
    private int mPage = 1;

    @Inject
    EventBus mBus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setRefreshing(true);
        presenter.fetchMessageList();
    }

    @NonNull
    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public void injectDependencies() {
        HomeComponent component = getComponent(HomeComponent.class);
        component.inject(this);
        presenter = component.messageListPresenter();
    }

    @Override
    public int layoutRes() {
        return R.layout.fragment_message_list;
    }

    @Override
    public void bindViews(View view) {
        mDataList = new ArrayList<>();
        MessageListAdapter adapter = new MessageListAdapter(mDataList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(
                new DividerDecoration(Color.parseColor("#F8F8F8"), 2, 40, 40));
        mRecyclerView.setRefreshListener(() -> {
            mPage = 1;
            presenter.fetchMessageList();
        });
        mRecyclerView.setOnMoreListener((total, left, current) -> {
            if (mDataList.size() > 0) {
                presenter.fetchMessageListNext(++mPage);
            }
        }, 4);
    }

    @Override
    public void showEmpty() {
        mRecyclerView.showEmptyView();
    }

    @Override
    public void showNetError() {
        mRecyclerView.showErrorView();
    }

    @Override
    public void showConversation(List<Conversation> conversations) {
        if (!mDataList.isEmpty()) {
            mDataList.clear();
        }
        mDataList.addAll(conversations);
        mRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void showMoreConversation(List<Conversation> conversations) {
        mDataList.addAll(conversations);
        mRecyclerView.notifyDataSetChanged();
    }

    @Override
    public void loadNoMore() {
        //mRecyclerView.loadNoMore();
        mRecyclerView.removeOnMoreListtener();
    }

    @Override
    public void loadMoreError() {
        mRecyclerView.loadMoreFailure();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void subscribe(MessageEvent event) {
        presenter.fetchMessageList();
    }
}
