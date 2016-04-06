package xyz.shaohui.sicilly.ui.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyFactory;
import xyz.shaohui.sicilly.data.cache.DiskCache;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.data.services.RetrofitService;
import xyz.shaohui.sicilly.data.services.api.StatusAPI;
import xyz.shaohui.sicilly.ui.adapters.StatusListAdapter;
import xyz.shaohui.sicilly.utils.MyToast;


public class StatusListFragment extends Fragment {

    @Bind(R.id.swipe_refresh)SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler)RecyclerView recyclerView;


    private StatusAPI statusService;
    private StatusListAdapter mAdapter;
    private List<Status> dataList;
    private int dataCode;
    private String q;
    private String userId;

    private int mPage = 1;

    private final int PRELOAD_SIZE = 6;

    public static final int DATA_HOME = 1;
    public static final int DATA_ABOUT_ME = 2;
    public static final int DATA_PUBLIC = 3;
    public static final int DATA_USER_HOME = 4;
    public static final int DATA_SEARCH = 5;

    public static StatusListFragment newInstance(int dataCode) {
        StatusListFragment fragment = new StatusListFragment();
        Bundle args = new Bundle();
        args.putInt("data_code", dataCode);
        fragment.setArguments(args);
        return fragment;
    }

    public static StatusListFragment newInstance(String q) {
        StatusListFragment fragment = new StatusListFragment();
        Bundle args = new Bundle();
        args.putInt("data_code", DATA_SEARCH);
        args.putString("q", q);
        fragment.setArguments(args);
        return fragment;
    }

    public static StatusListFragment newInstanceForUser(String id) {
        StatusListFragment fragment = new StatusListFragment();
        Bundle args = new Bundle();
        args.putInt("data_code", DATA_USER_HOME);
        args.putString("user_id", id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        dataCode = args.getInt("data_code");
        q = args.getString("q", "");
        userId = args.getString("user_id", "");

        statusService = SicillyFactory.getRetrofitService().getStatusService();

        dataList = new ArrayList<>();
        mAdapter = new StatusListAdapter(dataList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_status_list, container, false);
        ButterKnife.bind(this, v);

        initRecycler();
        initSwipeRefresh();

        return v;
    }

    private void initRecycler() {
        final LinearLayoutManager layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.bottom = 16;
                outRect.top = 16;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                loadMore((LinearLayoutManager) recyclerView.getLayoutManager());
            }
        });

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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchCache();
        fetchData(true);
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

    public boolean isRefreshing() {
        return swipeRefreshLayout.isRefreshing();
    }

    public void fetchData(final boolean isIndex) {
        int page = isIndex ? 1:mPage;

        Observable<JsonArray> observable ;
        RetrofitService service = SicillyFactory.getRetrofitService();
        switch (dataCode) {
            case StatusListFragment.DATA_HOME:
                observable = statusService.homeData(SicillyFactory.PAGE_COUNT, page);
                break;
            case StatusListFragment.DATA_ABOUT_ME:
                observable = statusService.aboutMeData(SicillyFactory.PAGE_COUNT, page);
                break;
            case StatusListFragment.DATA_PUBLIC:
                if (isIndex) {
                    observable = statusService.publicData(SicillyFactory.PAGE_COUNT);
                } else {
                    String id = dataList.get(dataList.size() - 1).getId();
                    observable = statusService.publicDataMore(SicillyFactory.PAGE_COUNT, id);
                }
                break;
            case StatusListFragment.DATA_SEARCH:
                if (isIndex) {
                    observable = service.getSearchService().searchStatus(q);
                } else {
                    String id = dataList.get(dataList.size() - 1).getId();
                    observable = service.getSearchService().searchStatusMore(q, id);
                }
                break;
            case StatusListFragment.DATA_USER_HOME:
                if (TextUtils.isEmpty(userId)) {
                    observable = service.getUserService().homePage(page);
                } else {
                    observable = service.getUserService().userTimeline(userId, page);
                }
                break;
            default:
                observable = statusService.homeData(SicillyFactory.PAGE_COUNT, page);
        }

        Log.i("TAG_page", page + "");

        observable
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (isIndex) {
                            mPage = 1;
                        }
                        if (isIndex) {
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
                        saveCache(jsonElements.toString());
                        return Observable.from(jsonElements);
                    }
                })
                .map(new Func1<JsonElement, Status>() {
                    @Override
                    public Status call(JsonElement jsonElement) {
                        return Status.toObject(jsonElement);
                    }
                })
                .doOnNext(new Action1<Status>() {
                    @Override
                    public void call(Status status) {
//                        Log.i("TAG", status.getText());
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Status>() {
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
                        MyToast.showToast(getActivity(), "处理错误,请刷新重试");
                    }

                    @Override
                    public void onNext(Status status) {
                        dataList.add(status);
                    }
                });
    }

    private void fetchCache() {
        Observable.create(new Observable.OnSubscribe<JsonArray>() {
            @Override
            public void call(Subscriber<? super JsonArray> subscriber) {
                String json = "";
                switch (dataCode) {
                    case DATA_HOME:
                        json = DiskCache.readCache("home_status_list");
                        break;
                    case DATA_PUBLIC:
                        json = DiskCache.readCache("public_status_list");
                        break;
                    case DATA_ABOUT_ME:
                        json = DiskCache.readCache("me_status_list");
                        break;
                }
                Log.i("TAG", json);
                if (!TextUtils.isEmpty(json)) {
                    JsonArray jsonElements = new Gson().fromJson(json, JsonArray.class);
                    subscriber.onNext(jsonElements);
                }
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .flatMap(new Func1<JsonArray, Observable<JsonElement>>() {
                    @Override
                    public Observable<JsonElement> call(JsonArray jsonElements) {
                        return Observable.from(jsonElements);
                    }
                })
                .map(new Func1<JsonElement, Status>() {
                    @Override
                    public Status call(JsonElement jsonElement) {
                        return Status.toObject(jsonElement);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Status>() {
                    @Override
                    public void onCompleted() {
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Status status) {
                        dataList.add(status);
                    }
                });

    }

    private void saveCache(String json) {
        switch(dataCode) {
            case DATA_PUBLIC:
                DiskCache.saveCache(json, "public_status_list");
                break;
            case DATA_HOME:
                DiskCache.saveCache(json, "home_status_list");
                break;
            case DATA_ABOUT_ME:
                DiskCache.saveCache(json, "me_status_list");
        }
    }

    public void loadMore(LinearLayoutManager layoutManager) {

        boolean isBottom = layoutManager.findLastCompletelyVisibleItemPosition() >=
                mAdapter.getItemCount() - PRELOAD_SIZE;
        if (!isRefreshing() && isBottom) {
            Log.i("TAG", "调用一次recycler的检测底部事件");
            showRefresh();
            swipeRefreshLayout.setRefreshing(true);
            fetchData(false);
        }
    }

    public void swipeFresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                fetchData(true);
            }
        }, 100);
    }

    private Status toObject(JsonElement element) {
        JsonObject json = element.getAsJsonObject();

        Status status = new Status();
        status.setCreatedAt(json.get("created_at").getAsString());
        status.setId(json.get("id").getAsString());
        status.setRawid(json.get("rawid").getAsInt());
        status.setText(json.get("text").getAsString());
        status.setSource(json.get("source").getAsString());
        status.setFavorited(json.get("favorited").getAsBoolean());

        JsonObject jsonUser = json.get("user").getAsJsonObject();
        User user = User.toObject(jsonUser);

        if (json.get("photo")!=null) {
            JsonObject jsonPhoto = json.get("photo").getAsJsonObject();
            status.setImageUrl(jsonPhoto.get("imageurl").getAsString());
            status.setImageLargeUrl(jsonPhoto.get("largeurl").getAsString());
        }

        status.setUser(user);
        status.setUserId(user.getId());

        return status;
    }

}
