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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.shaohui.vistarecyclerview.OnMoreListener;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import xyz.shaohui.sicilly.R;


public class TimelineFragment extends BaseFragment {

    @BindView(R.id.recycler)VistaRecyclerView recyclerView;

    public static final String TAG = "TimelineFragment";
    private int action;

    public static final int ACTION_INDEX = 1;

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

    }

    private void fetchNextPage() {

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
