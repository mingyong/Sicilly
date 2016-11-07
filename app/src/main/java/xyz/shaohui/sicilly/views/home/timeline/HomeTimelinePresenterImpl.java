package xyz.shaohui.sicilly.views.home.timeline;

import java.util.List;
import javax.inject.Inject;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.greenrobot.eventbus.EventBus;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.api.FavoriteAPI;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.views.home.timeline.mvp.HomeTimelinePresenter;

/**
 * Created by shaohui on 16/9/10.
 */

public class HomeTimelinePresenterImpl extends HomeTimelinePresenter {

    private EventBus mBus;

    private StatusAPI statusService;
    private FavoriteAPI favoriteService;

    @Inject
    HomeTimelinePresenterImpl(StatusAPI statusService, FavoriteAPI favoriteService,  EventBus enentBus) {
        this.statusService = statusService;
        this.favoriteService = favoriteService;
        mBus = enentBus;
    }

    @Override
    public void loadMessage(int type) {
        Observable<List<Status>> observable;
        switch (type) {
            case HomeTimelineFragment.TYPE_HOME:
                observable = statusService.homeStatus();
                break;
            case HomeTimelineFragment.TYPE_ABOUT_ME:
                observable = statusService.mentionsStatus();
                break;
            default:
                observable = statusService.homeStatus();
        }
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(statuses -> {
                    if (isViewAttached()) {
                        getView().showMessage(statuses);
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().networkError();
                    }
                    throwable.printStackTrace();
                });
    }

    @Override
    public void loadMoreMessage(int page, Status status, int type) {
        Observable<List<Status>> observable;
        switch (type) {
            case HomeTimelineFragment.TYPE_HOME:
                observable = statusService.homeStatusNext(page, status.id());
                break;
            case HomeTimelineFragment.TYPE_ABOUT_ME:
                observable = statusService.mentionsStatusNext(page, status.rawid());
                break;
            default:
                observable = statusService.homeStatusNext(page, status.id());
        }
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(statuses -> {
                    if (isViewAttached()) {
                        getView().showMoreMessage(statuses);
                    }
                }, throwable -> {
                    ErrorUtils.catchException(throwable);
                    if (isViewAttached()) {
                        getView().loadMoreFail();
                    }
                });
    }

    @Override
    public void loadNewMessage(Status firstStatus) {

    }

    @Override
    public void listenerNewMessage() {

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
        favoriteService.createFavorite(messageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status -> {}, throwable -> {
                    if (isViewAttached()) {
                        getView().opStarFailure(position);
                    }
                });
    }

    private void unStarMessage(String messageId, int position) {
        favoriteService.destroyFavorite(messageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status -> {}, throwable -> {
                    if (isViewAttached()) {
                        getView().opStarFailure(position);
                    }
                });
    }

    @Override
    public void deleteMessage(Status status, int position) {
        statusService.destroyStatus(RequestBody.create(MediaType.parse("text/plain"), status.id()))
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

    @Override
    public void commmentMessage(String messageId) {

    }

    @Override
    public void repostMessage(String messageId) {

    }
}
