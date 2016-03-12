package xyz.shaohui.sicilly.ui.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.presenters.StatusListPresenter;
import xyz.shaohui.sicilly.ui.adapters.StatusListAdapter;


public class StatusListFragment extends Fragment {

    @Bind(R.id.swipe_refresh)SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler)RecyclerView recyclerView;

    private StatusListPresenter presenter;

    public static final int DATA_HOME = 1;
    public static final int DATA_ABOUT_ME = 2;
    public static final int DATA_USER_HOME = 3;

    public static StatusListFragment newInstance(int dataCode) {
        StatusListFragment fragment = new StatusListFragment();
        Bundle args = new Bundle();
        args.putInt("data_code", dataCode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int dataCode = getArguments().getInt("dataCode");

        presenter = new StatusListPresenter(this, dataCode);

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

    @OnClick(R.id.main_btn)
    void btnClick() {
        if (swipeRefreshLayout.isRefreshing()) {
            dismissRefresh();
        } else {
            showRefresh();
        }
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
                presenter.loadMore((LinearLayoutManager) recyclerView.getLayoutManager());
            }
        });

        recyclerView.setAdapter(presenter.getAdapter());

    }

    private void initSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(R.color.main);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.swipeFresh();
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.fetchData(true);
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

    public interface StatusListInter {

        public void fetchData();

        public void fetchCache();

        public StatusListAdapter getAdapter();

        public void swipeFresh();

    }

    public interface ActivityCallBack {

        public void statusReply();

    }


}
