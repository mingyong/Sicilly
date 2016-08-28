package xyz.shaohui.sicilly.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.shaohui.vistarecyclerview.OnMoreListener;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import me.shaohui.vistarecyclerview.decoration.DividerDecoration;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.models.Conversation;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.views.adapters.MessageListAdapter;

public class MessageListFragment extends Fragment {

    @BindView(R.id.recycler)VistaRecyclerView recyclerView;

    private List dataList;
    private int page = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataList = new ArrayList();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message_list, container, false);
        ButterKnife.bind(this, v);
        initRecyclerView();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView.setRefreshing(true);
        fetchMessageList(false);
    }

    private void initRecyclerView() {
        MessageListAdapter adapter = new MessageListAdapter(dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerDecoration(getResources().getColor(R.color.divider), 2));
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchMessageList(false);
            }
        });
        recyclerView.setOnMoreListener(new OnMoreListener() {
            @Override
            public void noMoreAsked(int total, int left, int current) {
                recyclerView.loadNoMore();
            }
        }, 4);
    }

    private void fetchMessageList(final boolean isMore) {
        if (!isMore) {
            page = 1;
        }
        SicillyApplication.getRetrofitService()
                .getMessageService().conversationList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Conversation>>() {
                    @Override
                    public void call(List<Conversation> conversations) {
                        if (dataList.size() <= 0 && conversations.size() == 0) {
                            recyclerView.showEmptyView();
                        } else if (conversations.size() == 0) {
                            recyclerView.loadNoMore();
                        } else {
                            if (!isMore) {
                                dataList.clear();
                            }
                            dataList.addAll(conversations);
                            recyclerView.notifyDataSetChanged();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ErrorUtils.catchException(throwable);
                        recyclerView.showErrorView();
                    }
                });
    }

}
