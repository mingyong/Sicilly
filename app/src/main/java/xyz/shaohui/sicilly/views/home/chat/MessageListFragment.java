package xyz.shaohui.sicilly.views.home.chat;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import butterknife.BindView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import me.shaohui.vistarecyclerview.decoration.DividerDecoration;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.data.models.Conversation;
import xyz.shaohui.sicilly.data.models.ConversationBean;
import xyz.shaohui.sicilly.views.adapters.MessageListAdapter;
import xyz.shaohui.sicilly.views.home.chat.mvp.MessageListPresenter;
import xyz.shaohui.sicilly.views.home.chat.mvp.MessageListView;
import xyz.shaohui.sicilly.views.home.di.HomeComponent;

public class MessageListFragment extends BaseFragment<MessageListView, MessageListPresenter>
        implements MessageListView {

    @BindView(R.id.recycler)
    VistaRecyclerView mRecyclerView;

    private List<ConversationBean> mDataList;
    private int mPage = 1;

    @Inject
    EventBus mBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataList = new ArrayList<>();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView.setRefreshing(true);
        presenter.fetchMessageList(1);
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
        MessageListAdapter adapter = new MessageListAdapter(mDataList);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addItemDecoration(
                new DividerDecoration(getResources().getColor(R.color.divider), 2));
        mRecyclerView.setRefreshListener(() -> {
            mPage = 1;
            presenter.fetchMessageList(mPage);
        });
        mRecyclerView.setOnMoreListener((total, left, current) -> {
            presenter.fetchMessageList(++mPage);
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
        if (mDataList.isEmpty()) {
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
        mRecyclerView.loadNoMore();
    }

    @Override
    public void loadMoreError() {
        mRecyclerView.loadMoreFailure();
    }
}
