package xyz.shaohui.sicilly.views.status_detail;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.utils.RxUtils;
import xyz.shaohui.sicilly.views.status_detail.mvp.StatusDetailPresenter;

/**
 * Created by shaohui on 16/9/17.
 */

public class StatusDetailPresenterImpl extends StatusDetailPresenter {

    private final StatusAPI mStatusService;

    @Inject
    StatusDetailPresenterImpl(StatusAPI statusService) {

        mStatusService = statusService;
    }

    @Override
    public void loadStatus(Status origin) {
        List<Status> originList = new ArrayList<>();
        originList.add(origin);
        Observable<List<Status>> local = Observable.just(new ArrayList<>());

        Observable<List<Status>> remote = mStatusService.context(origin.id());

        Observable.concat(local, remote)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(statuses -> {
                    if (isViewAttached()) {
                        getView().showStatus(statuses);
                    }
                }, RxUtils.ignoreNetError);

    }

    @Override
    public void deleteMessage(Status status, int position) {
        mStatusService.destroyStatus(RequestBody.create(MediaType.parse("text/plain"), status.id()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status1 -> {
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().deleteStatusFailure(status, position);
                    }
                    throwable.printStackTrace();
                });
    }
}
