package xyz.shaohui.sicilly.views.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import me.shaohui.vistarecyclerview.OnMoreListener;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.models.Message;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.views.adapters.ChatAdapter;

public class ChatActivity extends BaseActivity {

    @BindView(R.id.recycler)VistaRecyclerView recyclerView;
    @BindView(R.id.customer_avatar)ImageView customerAvatar;
    @BindView(R.id.self_avatar)ImageView selfAvatar;
    @BindView(R.id.main_edit)EditText editText;
    @BindView(R.id.chat_title)TextView title;

    private List<Message> dataList;
    private User otherUser;
    private int page ;

    public static Intent newIntent(Context context, User user) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("user", user);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        otherUser = getIntent().getParcelableExtra("user");
        dataList = new ArrayList<>();
        initRecycler();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        fetchChatList(false);
    }

    private void initRecycler() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        ChatAdapter adapter = new ChatAdapter(otherUser.id(), dataList);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnMoreListener(new OnMoreListener() {
            @Override
            public void noMoreAsked(int total, int left, int current) {
                fetchChatList(true);
            }
        });
    }

    private void fetchChatList(final boolean isMore) {
        if (isMore) {
            page = 1;
        }
        SicillyApplication.getRetrofitService()
                .getMessageService().messageList(otherUser.id(), page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Message>>() {
                    @Override
                    public void call(List<Message> messages) {
                        if (messages.size() == 0) {
                            if (isMore) {
                                recyclerView.loadNoMore();
                            } else {
                                recyclerView.showEmptyView();
                            }
                            return;
                        }
                        dataList.addAll(messages);
                        recyclerView.notifyDataSetChanged();
                        page++;
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ErrorUtils.catchException(throwable);
                        if (isMore) {
                            recyclerView.loadMoreFailure();
                        } else {
                            recyclerView.showErrorView();
                        }
                    }
                });
    }
}
