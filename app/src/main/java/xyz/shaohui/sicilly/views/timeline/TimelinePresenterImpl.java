package xyz.shaohui.sicilly.views.timeline;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.api.FavoriteAPI;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.views.timeline.di.TimelineModule;
import xyz.shaohui.sicilly.views.timeline.mvp.TimelineMVP;

/**
 * Created by shaohui on 2016/10/19.
 */

public class TimelinePresenterImpl extends TimelineMVP.Presenter {

    private final String mUserId;

    private final int mDataType;

    private final StatusAPI mStatusService;

    private final FavoriteAPI mFavoriteService;

    @Inject
    TimelinePresenterImpl(@Named(TimelineModule.TIMELINE_USER_ID) String userId,
            @Named(TimelineModule.TIMELINE_DATA_TYPE) int dataType, StatusAPI statusService,
            FavoriteAPI favoriteService) {

        mUserId = userId;
        mDataType = dataType;
        mStatusService = statusService;
        mFavoriteService = favoriteService;
    }

    @Override
    public void loadStatus(int page) {
        Observable<List<Status>> observable;
        if (mDataType == TimelineActivity.DATA_TYPE_TIMELINE) {
            observable = mStatusService.userTimelineSimple(mUserId, page);
        } else {
            observable = mFavoriteService.favoriteStatusList(mUserId, page);
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(statuses -> {
                    if (isViewAttached()) {
                        if (statuses.isEmpty()) {
                            if (page == 1) {
                                getView().loadEmpty();
                            } else {
                                getView().loadNoMore();
                            }
                        } else {
                            getView().loadDataSuccess(statuses);
                        }
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        if (page == 1) {
                            getView().loadDataFailure();
                        } else {
                            getView().loadMoreError();
                        }
                    }
                });
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
