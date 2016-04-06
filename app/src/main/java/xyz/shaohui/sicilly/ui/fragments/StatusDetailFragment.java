package xyz.shaohui.sicilly.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
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
import rx.functions.Action0;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyFactory;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.data.services.RetrofitService;
import xyz.shaohui.sicilly.data.services.user.UserService;
import xyz.shaohui.sicilly.ui.activities.CreateStatusActivity;
import xyz.shaohui.sicilly.ui.adapters.StatusDetailAdapter;
import xyz.shaohui.sicilly.utils.MyToast;


public class StatusDetailFragment extends Fragment {

    @Bind(R.id.recycler)RecyclerView recyclerView;

    private LinearLayoutManager layoutManager;
    private StatusDetailAdapter mAdapter;
    private List<Status> dataList;

    private Status status;

    private RetrofitService service;

    public static StatusDetailFragment newInstance(String statusJson) {
        StatusDetailFragment fragment = new StatusDetailFragment();
        Bundle args = new Bundle();
        args.putString("status", statusJson);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String json = getArguments().getString("status");
        status = new Gson().fromJson(json, Status.class);

        service = SicillyFactory.getRetrofitService();

        dataList = new ArrayList<>();
        mAdapter = new StatusDetailAdapter(dataList);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataList.add(status);
        mAdapter.notifyDataSetChanged();

        fetchStatusInfo();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_status_detail, container, false);
        ButterKnife.bind(this, v);

        initRecycler();

        return v;
    }

    private void initRecycler() {
        layoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
    }

    private void fetchStatusInfo() {
        service.getStatusService().contextTimeline(status.getId())
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
                .doOnCompleted(new Action0() {
                    @Override
                    public void call() {
                        dataList.remove(0);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Status>() {
                    @Override
                    public void onCompleted() {
                        mAdapter.notifyDataSetChanged();
                        adjustRecycler();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        MyToast.showToast(getContext(), "显示上下文失败");
                    }

                    @Override
                    public void onNext(Status item) {
                        dataList.add(item);
                    }
                });

    }

    private void adjustRecycler() {
        int currentId = 0;
        for (int i = 0; i < dataList.size(); i++) {
            if (dataList.get(i).getId().equals(status.getId())) {
                currentId = i;
            }
        }
        layoutManager.scrollToPositionWithOffset(currentId, 10);
    }


    @OnClick(R.id.action_favorite)
    void createFavorite() {
        if (status.isFavorited()) {
            UserService.destroyFavorite(status.getId(), new UserService.CallBack() {
                @Override
                public void success() {
                    MyToast.showToast(getContext(), "取消收藏成功");
                }

                @Override
                public void failure() {
                    MyToast.showToast(getContext(), "取消收藏失败");
                }
            });
        } else {
            UserService.createFavorite(status.getId(), new UserService.CallBack() {
                @Override
                public void success() {
                    MyToast.showToast(getContext(), "收藏成功");
                }

                @Override
                public void failure() {
                    MyToast.showToast(getContext(), "收藏失败");
                }
            });
        }
    }

    @OnClick(R.id.action_repost)
    void repostStatus() {
        CreateStatusActivity.newIntent(getContext(),
                CreateStatusActivity.TYPE_REPOST, status.getId(), status.getText());
    }

    @OnClick(R.id.action_reply)
    void replyStatus() {
        CreateStatusActivity.newIntent(getContext(),
                CreateStatusActivity.TYPE_REPLY, status.getId(), status.getText());
    }



}
