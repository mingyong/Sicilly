package xyz.shaohui.sicilly.views.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.hannesdorfmann.fragmentargs.annotation.Arg;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.shaohui.scrollablelayout.ScrollableHelper;
import me.shaohui.vistarecyclerview.OnMoreListener;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import me.shaohui.vistarecyclerview.decoration.SpacingDecoration;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.RetrofitService;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.views.adapters.IndexStatusAdapter;


public class TimelineFragment extends BaseFragment implements ScrollableHelper.ScrollableContainer {
    public static final String TAG = "TimelineFragment";

    @BindView(R.id.recycler)VistaRecyclerView recyclerView;

    private int action;
    private String userId;
    private int page = 1;

    public static final int ACTION_INDEX = 1;
    public static final int ACTION_ABOUT_ME = 2;
    public static final int ACTION_USER = 3;

    private List<Status> dataList;

    public static TimelineFragment newInstance(int action) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putInt("action", action);
        fragment.setArguments(args);
        return fragment;
    }

    public static TimelineFragment newInstance(int action, String userId) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putInt("action", action);
        args.putString("user_id", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        action = getArguments().getInt("action");
        if (action == ACTION_USER) {
            userId = getArguments().getString("user_id");
        }
        dataList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timeline, container, false);
        ButterKnife.bind(this, v);
        initRecycler();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch (action) {
            case ACTION_INDEX:
                fetchStatus();
                recyclerView.setRefreshing(true);
                break;
            case ACTION_USER:
                fetchUserStatus(false);
                recyclerView.showProgressView();
                break;
            case ACTION_ABOUT_ME:
                fetchAboutMeStatus(false);
                recyclerView.setRefreshing(true);
                break;
        }
    }

    private void initRecycler() {
        IndexStatusAdapter adapter = new IndexStatusAdapter(dataList);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpacingDecoration(8));
        recyclerView.setOnMoreListener(new OnMoreListener() {
            @Override
            public void noMoreAsked(int total, int left, int current) {
                switch (action) {
                    case ACTION_INDEX:
                        fetchNextPage();
                        break;
                    case ACTION_USER:
                        fetchUserStatus(true);
                        break;
                    case ACTION_ABOUT_ME:
                        fetchAboutMeStatus(true);
                        break;
                }
            }
        }, 6);

        if (action ==  ACTION_INDEX || action == ACTION_ABOUT_ME) {
            recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    switch (action) {
                        case ACTION_INDEX:
                            fetchStatus();
                            break;
                        case ACTION_ABOUT_ME:
                            fetchAboutMeStatus(false);
                            break;
                    }
                }
            });
        }
    }

    private void fetchStatus() {
        SicillyApplication.getRetrofitService()
                .getStatusService().homeStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Status>>() {
                    @Override
                    public void call(List<Status> statuses) {
                        page = 2;
                        dataList.clear();
                        dataList.addAll(statuses);
                        recyclerView.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ErrorUtils.catchException(throwable);
                        recyclerView.showErrorView();
                    }
                });
    }

    private void fetchNextPage() {
        String lastStatusId = dataList.get(dataList.size() - 1).id();

        SicillyApplication.getRetrofitService()
                .getStatusService().homeStatusNext(page , 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Status>>() {
                    @Override
                    public void call(List<Status> statuses) {
                        if (statuses.size() > 0) {
                            page++;
                            dataList.addAll(statuses);
                            recyclerView.notifyDataSetChanged();
                        } else {
                            recyclerView.loadNoMore();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ErrorUtils.catchException(throwable);
                        recyclerView.loadMoreFailure();
                    }
                });
    }

    private void fetchAboutMeStatus(final boolean isMore) {
        if (!isMore) {
            page = 1;
        }
        SicillyApplication.getRetrofitService()
                .getStatusService().mentionsStatus(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Status>>() {
                    @Override
                    public void call(List<Status> statuses) {
                        if (isMore) {
                            if (statuses.size() == 0) {
                                recyclerView.loadNoMore();
                                return;
                            }
                            page++;
                        } else {
                            page = 2;
                            dataList.clear();
                        }
                        dataList.addAll(statuses);
                        recyclerView.notifyDataSetChanged();
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

    private void fetchUserStatus(final boolean isMore) {
        if (!isMore) {
            page = 1;
        }
        SicillyApplication.getRetrofitService()
                .getStatusService().userTimeline(userId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Status>>() {
                    @Override
                    public void call(List<Status> statuses) {
                        if (isMore) {
                            if (statuses.size() == 0) {
                                recyclerView.loadNoMore();
                                return;
                            }
                            page++;
                        } else {
                            page = 2;
                            dataList.clear();
                        }
                        dataList.addAll(statuses);
                        recyclerView.notifyDataSetChanged();
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

    @Override
    public View getScrollableView() {
        return recyclerView.getRecycler();
    }
}
