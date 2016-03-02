package xyz.shaohui.sicilly.presenters;

import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.SicillyFactory;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.data.services.RetrofitService;
import xyz.shaohui.sicilly.data.services.api.StatusAPI;
import xyz.shaohui.sicilly.ui.adapters.StatusListAdapter;
import xyz.shaohui.sicilly.ui.fragments.StatusListFragment;
import xyz.shaohui.sicilly.utils.MyToast;

/**
 * Created by kpt on 16/2/22.
 */
public class StatusListPresenter implements StatusListFragment.StatusListInter {

    private StatusListFragment fragment;
    private StatusAPI statusService;
    private StatusListAdapter mAdapter;
    private List<Status> dataList;
    private int dataCode;

    private Handler mHandler;

    private int mPage = 1;

    private final int PRELOAD_SIZE = 6;

    public StatusListPresenter(StatusListFragment fragment, int dataCode) {
        RetrofitService service = SicillyFactory.getRetrofitService();
        statusService = service.getStatusService();
        this.fragment = fragment;
        this.dataCode = dataCode;

        dataList = new ArrayList<>();
        mAdapter = new StatusListAdapter(dataList);
        mHandler = new Handler();
    }

    @Override
    public void fetchData() {

    }

    public void fetchData(final boolean isIndex) {
        int page = isIndex ? 1:mPage;

        Observable<JsonArray> observable ;
        switch (dataCode) {
            case StatusListFragment.DATA_HOME:
                observable = statusService.homeData(SicillyFactory.PAGE_COUNT, page);
                break;
            case StatusListFragment.DATA_ABOUT_ME:
                observable = statusService.aboutMeData(SicillyFactory.PAGE_COUNT, page);
                break;
            default:
                observable = statusService.homeData(SicillyFactory.PAGE_COUNT, page);
        }

        observable
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        if (isIndex) {
                            mPage = 1;
                        }
                    }
                })
                .doOnRequest(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        fragment.showRefresh();
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
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
                        if (isIndex) {
                            dataList.clear();
                        }
                        return toObject(jsonElement);
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
                        fragment.dismissRefresh();
                        mAdapter.notifyDataSetChanged();
                        mPage++;
                        Log.i("TAG", "dataList.size = " + dataList.size());
                    }

                    @Override
                    public void onError(Throwable e) {
                        fragment.dismissRefresh();
                        e.printStackTrace();
                        MyToast.showToast(fragment.getActivity(), "处理错误,请刷新重试");
                    }

                    @Override
                    public void onNext(Status status) {
                        dataList.add(status);
                    }
                });
    }

    public void loadMore(LinearLayoutManager layoutManager) {

        boolean isBottom = layoutManager.findLastCompletelyVisibleItemPosition() >=
                mAdapter.getItemCount() - PRELOAD_SIZE;
        if (!fragment.isRefreshing() && isBottom) {
            fetchData(false);
        }
    }

    @Override
    public void fetchCache() {

    }

    @Override
    public StatusListAdapter getAdapter() {
        return mAdapter;
    }

    @Override
    public void swipeFresh() {
        mHandler.postDelayed(new Runnable() {
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

        JsonObject jsonUser = json.get("user").getAsJsonObject();
        User user = User.toObject(jsonUser);

        status.setUser(user);
        status.setUserId(user.getId());

        return status;
    }
}
