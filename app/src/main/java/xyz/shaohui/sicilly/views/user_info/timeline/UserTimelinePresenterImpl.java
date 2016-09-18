package xyz.shaohui.sicilly.views.user_info.timeline;

import javax.inject.Inject;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.greenrobot.eventbus.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.api.FavoriteAPI;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.views.user_info.timeline.mvp.UserTimelinePresenter;

/**
 * Created by shaohui on 16/9/18.
 */

public class UserTimelinePresenterImpl extends UserTimelinePresenter {

    private EventBus mBus;

    private StatusAPI mStatusService;

    private FavoriteAPI mFavoriteService;

    @Inject
    UserTimelinePresenterImpl(EventBus bus, StatusAPI statusAPI, FavoriteAPI favoriteService) {
        mBus = bus;
        mStatusService = statusAPI;
        mFavoriteService = favoriteService;
    }

    @Override
    public void loadStatus(String userId) {
        mStatusService.userTimeline(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(statuses -> {
                    if (isViewAttached()) {
                        getView().showMessage(statuses);
                    }
                }, throwable -> {
                    ErrorUtils.catchException(throwable);
                    if (isViewAttached()) {
                        getView().networkError();
                    }
                });
    }

    @Override
    public void loadMoreStatus(String userId, int page, Status lastStatus) {
        mStatusService.userTimelineNext(userId, page, lastStatus.rawid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(statuses -> {
                    if (isViewAttached()) {
                        getView().showMoreMessage(statuses);
                    }
                }, throwable -> {
                    ErrorUtils.catchException(throwable);
                    if (isViewAttached()) {
                        getView().loadMoreFailure();
                    }
                });
    }

    @Override
    public void opStar(Status status, int position) {
        if (status.favorited()) {
            unStarMessage(status.id(), position);
        } else {
            starMessage(status.id(), position);
        }
    }

    private void starMessage(String messageId, int position) {
        mFavoriteService.createFavorite(messageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status -> {}, throwable -> {
                    if (isViewAttached()) {
                        getView().opStarFailure(position);
                    }
                });
    }

    private void unStarMessage(String messageId, int position) {
        mFavoriteService.destroyFavorite(messageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status -> {}, throwable -> {
                    if (isViewAttached()) {
                        getView().opStarFailure(position);
                    }
                });
    }
    @Override
    public void deleteStatus(Status status, int position) {
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
