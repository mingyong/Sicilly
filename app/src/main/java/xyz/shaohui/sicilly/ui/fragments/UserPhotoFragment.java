package xyz.shaohui.sicilly.ui.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.services.RetrofitService;
import xyz.shaohui.sicilly.ui.adapters.ImageListAdapter;
import xyz.shaohui.sicilly.utils.MyToast;

public class UserPhotoFragment extends Fragment {

    @Bind(R.id.recycler)RecyclerView recyclerView;

    private String userId;

    private List<Status> dataList;
    private ImageListAdapter mAdapter;
    private boolean canLoad;
    private int mPage;

    private static final int PRELOAD_SIZE = 6;

    public static UserPhotoFragment newInstance(String userId) {
        UserPhotoFragment fragment = new UserPhotoFragment();
        Bundle args = new Bundle();
        args.putString("user_id", userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dataList = new ArrayList<>();

        userId = getArguments().getString("user_id");

        canLoad = true;
        mPage = 1;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_photo, container, false);
        ButterKnife.bind(this, v);

        initRecycler();

        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchInfo();
    }

    private void initRecycler() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ImageListAdapter(dataList);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(getOnBottomListener(layoutManager));
    }

    private void fetchInfo() {
        RetrofitService service = SicillyFactory.getRetrofitService();
        service.getUserService().showPhoto(userId, mPage)
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
                        canLoad = true;
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MyToast.showToast(getContext(), "加载失败, 请重试");
                    }

                    @Override
                    public void onNext(Status status) {
                        dataList.add(status);
                    }
                });

    }

    RecyclerView.OnScrollListener getOnBottomListener(final StaggeredGridLayoutManager layoutManager) {
        return new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                boolean isBottom = layoutManager.findLastCompletelyVisibleItemPositions(
                        new int[2])[1] >= mAdapter.getItemCount() - PRELOAD_SIZE;
                if (isBottom && canLoad) {
                    canLoad = false;
                    mPage = mPage + 1;
                    fetchInfo();
                }
            }
        };
    }




}
