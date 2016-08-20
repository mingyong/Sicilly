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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.shaohui.vistarecyclerview.OnMoreListener;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.RetrofitService;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.views.adapters.IndexStatusAdapter;


public class TimelineFragment extends BaseFragment {

    @BindView(R.id.recycler)VistaRecyclerView recyclerView;

    public static final String TAG = "TimelineFragment";
    private int action;

    public static final int ACTION_INDEX = 1;

    private List<Status> dataList;

    public static TimelineFragment newInstance(int action) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putInt("action", action);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        action = getArguments().getInt("action");
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
        recyclerView.setRefreshing(true);
        fetchStatus();
    }

    private void initRecycler() {
        IndexStatusAdapter adpter = new IndexStatusAdapter(dataList);
        recyclerView.setOnMoreListener(new OnMoreListener() {
            @Override
            public void noMoreAsked(int total, int left, int current) {
                fetchNextPage();
            }
        });
        recyclerView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchStatus();
            }
        });
    }

    private void fetchStatus() {
        SicillyApplication.getRetrofitService()
                .getStatusService().homeStatus()
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Status>>() {
                    @Override
                    public void call(List<Status> statuses) {
                        dataList.addAll(statuses);
                        recyclerView.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ErrorUtils.catchException();
                        recyclerView.showErrorView();
                    }
                });
    }

    private void fetchNextPage() {
        String lastStatusId = dataList.get(dataList.size() - 1).getId();

        SicillyApplication.getRetrofitService()
                .getStatusService().homeStatusNext(lastStatusId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Status>>() {
                    @Override
                    public void call(List<Status> statuses) {
                        if (statuses.size() > 0) {
                            dataList.addAll(statuses);
                            recyclerView.notifyDataSetChanged();
                        } else {
                            recyclerView.loadNoMore();
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ErrorUtils.catchException();
                        recyclerView.loadMoreFailure();
                    }
                });
    }

    @OnClick(R.id.btn_add)
    void createStatus() {

    }

    @OnClick(R.id.btn_search)
    void actionSearch() {

    }

    @OnClick(R.id.img_icon)
    void scrollTop() {

    }



}
