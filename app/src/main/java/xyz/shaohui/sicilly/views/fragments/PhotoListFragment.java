package xyz.shaohui.sicilly.views.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.shaohui.scrollablelayout.ScrollableHelper;
import me.shaohui.vistarecyclerview.OnMoreListener;
import me.shaohui.vistarecyclerview.VistaRecyclerView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.views.adapters.UserPhotoAdapter;

public class PhotoListFragment extends Fragment implements ScrollableHelper.ScrollableContainer {
    public static final String TAG = "PhotoListFragment";

    @BindView(R.id.recycler)VistaRecyclerView recyclerView;

    private String userId;
    private List<Status> statusList;

    public static PhotoListFragment newInstance(String userId) {
        PhotoListFragment fragment = new PhotoListFragment();
        Bundle args = new Bundle();
        args.putString("user_id", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userId = getArguments().getString("user_id");
        statusList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_photo_list, container, false);
        ButterKnife.bind(this, v);
        initRecycler();
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchPhotoList();
    }

    private void initRecycler() {
        UserPhotoAdapter adapter = new UserPhotoAdapter(statusList);
        recyclerView.setAdapter(adapter);
        recyclerView.setOnMoreListener(new OnMoreListener() {
            @Override
            public void noMoreAsked(int total, int left, int current) {
                fetchNextPhotoList();
            }
        }, 6);
    }

    private void fetchPhotoList() {
        SicillyApplication.getRetrofitService()
                .getUserService().userPhotoOther(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Status>>() {
                    @Override
                    public void call(List<Status> statuses) {
                        statusList.addAll(statuses);
                        recyclerView.notifyDataSetChanged();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ErrorUtils.catchException();
                    }
                });
    }

    private void fetchNextPhotoList() {
        SicillyApplication.getRetrofitService()
                .getUserService().userPhotoOtherNext(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Status>>() {
                    @Override
                    public void call(List<Status> statuses) {
                        if (statuses.size() > 0) {
                            statusList.addAll(statuses);
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

    @Override
    public View getScrollableView() {
        return recyclerView.getRecycler();
    }
}
