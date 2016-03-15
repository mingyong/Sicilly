package xyz.shaohui.sicilly.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyFactory;
import xyz.shaohui.sicilly.data.models.Message;
import xyz.shaohui.sicilly.data.services.RetrofitService;
import xyz.shaohui.sicilly.data.services.user.UserService;
import xyz.shaohui.sicilly.ui.adapters.ChatAdapter;
import xyz.shaohui.sicilly.utils.MyToast;

public class ChatActivity extends AppCompatActivity {

    @Bind(R.id.tool_bar)Toolbar toolbar;
    @Bind(R.id.recycler)RecyclerView recyclerView;
    @Bind(R.id.main_edit)EditText editText;

    private String userId;
    private String userName;
    private List<Message> dataList;
    private ChatAdapter mAdapter;

    public static Intent newIntent(Context context, String userId, String userName) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("id", userId);
        intent.putExtra("name", userName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        userId = getIntent().getStringExtra("id");
        userName = getIntent().getStringExtra("name");

        initToolbar();
        initRecycler();

        fetchList();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        toolbar.setTitle(userName);
    }

    private void initRecycler() {
        dataList = new ArrayList<>();
        mAdapter = new ChatAdapter(dataList, userId);
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void fetchList() {
        RetrofitService service = SicillyFactory.getRetrofitService();
        service.getMessageService().conversation(userId, 1)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<JsonArray, Observable<JsonElement>>() {
                    @Override
                    public Observable<JsonElement> call(JsonArray jsonElements) {
                        return Observable.from(jsonElements);
                    }
                })
                .map(new Func1<JsonElement, Message>() {
                    @Override
                    public Message call(JsonElement jsonElement) {
                        return Message.toObject(jsonElement.getAsJsonObject());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Message>() {
                    @Override
                    public void onCompleted() {
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Message message) {
                        dataList.add(message);
                    }
                });

    }

    @OnClick(R.id.submit)
    void sendMessage() {
        if (TextUtils.isEmpty(editText.getText().toString())) {
            MyToast.showToast(this, "发送消息不能为空");
            return;
        }
        UserService.createMessage(userId, editText.getText().toString(), new UserService.CallBack() {
            @Override
            public void success() {
                MyToast.showToast(getApplicationContext(), "发送成功");
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure() {
                MyToast.showToast(getApplicationContext(), "发送失败, 请重试");
            }
        });

    }

    private void addLocalMessage() {
        Message msg = new Message();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
