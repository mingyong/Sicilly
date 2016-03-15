package xyz.shaohui.sicilly.ui.activities;

import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyFactory;
import xyz.shaohui.sicilly.data.models.Message;
import xyz.shaohui.sicilly.data.models.MessageList;
import xyz.shaohui.sicilly.data.services.RetrofitService;
import xyz.shaohui.sicilly.ui.adapters.MessageListAdapter;
import xyz.shaohui.sicilly.utils.MyToast;
import xyz.shaohui.sicilly.utils.TimeFormat;

public class MessageActivity extends AppCompatActivity {

    @Bind(R.id.tool_bar)Toolbar toolbar;
    @Bind(R.id.recycler)RecyclerView recyclerView;
    @Bind(R.id.swipe_refresh)SwipeRefreshLayout swipeRefreshLayout;

    private List<MessageList> dataList;
    private MessageListAdapter mAdapter;
    private int mPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        initToolbar();
        initRecyclerView();
        initSwipeRefresh();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initRecyclerView() {
        dataList = new ArrayList<>();
        mAdapter = new MessageListAdapter(dataList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void initSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(R.color.main);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeFresh();
            }
        });
    }

    public void swipeFresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchData(true);
            }
        }, 100);
    }

    private void fetchData(final boolean isFirst) {
        RetrofitService service = SicillyFactory.getRetrofitService();
        service.getMessageService().conversationList(mPage)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (isFirst) {
                            mPage = 1;
                        }
                        if (isFirst) {
                            dataList.clear();
                        }
                    }
                })
                .doOnRequest(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        showRefresh();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<JsonArray, Observable<JsonElement>>() {
                    @Override
                    public Observable<JsonElement> call(JsonArray jsonElements) {
                        return Observable.from(jsonElements);
                    }
                })
                .map(new Func1<JsonElement, MessageList>() {
                    @Override
                    public MessageList call(JsonElement jsonElement) {
                        return toObject(jsonElement);
                    }
                })
                .doOnNext(new Action1<MessageList>() {
                    @Override
                    public void call(MessageList messageList) {
//                        Log.i("TAG", status.getText());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<MessageList>() {
                    @Override
                    public void onCompleted() {
                        dismissRefresh();
                        mAdapter.notifyDataSetChanged();
                        mPage++;
                        Log.i("TAG", "dataList.size = " + dataList.size());
                    }

                    @Override
                    public void onError(Throwable e) {
                        dismissRefresh();
                        e.printStackTrace();
                        MyToast.showToast(getApplicationContext(), "发生了一些错误");
                    }

                    @Override
                    public void onNext(MessageList messageList) {
                        dataList.add(messageList);
                    }
                });
    }

    /**
     * 显示隐藏swipeFreshLayout
     */
    public void showRefresh() {
        if (!swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            }, 1);
        }
    }

    public void dismissRefresh() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 500);
        }
    }

    private MessageList toObject(JsonElement jsonElement) {
        JsonObject json = jsonElement.getAsJsonObject();
        MessageList item = new MessageList();
        item.setHaveNew(json.get("new_conv").getAsBoolean());
        item.setOtherId(json.get("otherid").getAsString());
        item.setMsgNum(json.get("msg_num").getAsInt());

        Message message = Message.toObject(json.get("dm").getAsJsonObject());

        item.setMessage(message);
        item.setUpdateTime(TimeFormat.toLong(message.getCreatedTime()));
        return item;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
