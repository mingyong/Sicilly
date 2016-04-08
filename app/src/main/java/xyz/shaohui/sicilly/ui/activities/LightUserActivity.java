package xyz.shaohui.sicilly.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyFactory;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.data.services.RetrofitService;
import xyz.shaohui.sicilly.ui.adapters.LightUserAdapter;

public class LightUserActivity extends AppCompatActivity {

    @Bind(R.id.tool_bar)Toolbar toolbar;
    @Bind(R.id.recycler)RecyclerView recyclerView;

    private List<User> dataList;
    private LightUserAdapter mAdapter;

    private int mPage;
    private boolean canLoad;
    private String userId;
    private int code;

    public static final int CODE_FRIEND = 1;
    public static final int CODE_FOLLOWER = 2;
    public static final int PRELOAD_SIZE = 6;

    public static Intent newIntent(Context context, String userId, int code) {
        Intent intent = new Intent(context, LightUserActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("code", code);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_light_user);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        ButterKnife.bind(this);

        userId = getIntent().getStringExtra("user_id");
        code = getIntent().getIntExtra("code", 0);

        dataList = new ArrayList<>();
        mAdapter = new LightUserAdapter(dataList);
        mPage = 1;
        canLoad = true;

        initToolbar();
        initView();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        fetchData();
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

        switch(code) {
            case CODE_FOLLOWER:
                toolbar.setTitle("关注者");
                break;
            case CODE_FRIEND:
                toolbar.setTitle("好友");
                break;
        }
    }

    private void initView() {
        LinearLayoutManager layoutManager =
                new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        recyclerView.addOnScrollListener(getOnBottomListener(layoutManager));
    }

    private void fetchData() {
        Observable<JsonArray> observable = null;
        RetrofitService service = SicillyFactory.getRetrofitService();
        switch(code) {
            case CODE_FRIEND:
                observable = service.getUserService().showFriends(userId, mPage);
                break;
            case CODE_FOLLOWER:
                observable = service.getUserService().showFollowers(userId, mPage);
                break;
            default:
                finish();
        }
        if (observable == null) {
            return;
        }
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<JsonArray, Observable<JsonElement>>() {
                    @Override
                    public Observable<JsonElement> call(JsonArray jsonElements) {
                        return Observable.from(jsonElements);
                    }
                })
                .map(new Func1<JsonElement, User>() {
                    @Override
                    public User call(JsonElement jsonElement) {
                        return User.toObject(jsonElement.getAsJsonObject());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<User>() {
                    @Override
                    public void onCompleted() {
                        canLoad = true;
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(User user) {
                        dataList.add(user);
                    }
                });

    }

    RecyclerView.OnScrollListener getOnBottomListener(final LinearLayoutManager layoutManager) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                boolean isBottom = layoutManager.findLastCompletelyVisibleItemPosition()
                        >= mAdapter.getItemCount() - PRELOAD_SIZE;
                if (isBottom && canLoad) {
                    canLoad = false;
                    mPage = mPage + 1;
                    fetchData();
                }
            }
        };
    }
}
