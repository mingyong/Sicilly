package xyz.shaohui.sicilly.views.feed;

import java.lang.ref.WeakReference;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.api.FavoriteAPI;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.utils.ErrorUtils;

/**
 * Created by shaohui on 2016/11/26.
 */

public abstract class BaseFeedPresenter<V extends FeedMVP.View> implements FeedMVP.Presenter<V> {

    private WeakReference<V> mViewRef;

    public final FavoriteAPI mFavoriteService;
    public final StatusAPI mStatusService;

    protected BaseFeedPresenter(FavoriteAPI favoriteService, StatusAPI statusService) {
        this.mFavoriteService = favoriteService;
        this.mStatusService = statusService;
    }

    protected abstract Observable<List<Status>> loadStatus();

    public abstract Observable<List<Status>> loadMoreStatus(int page, Status lastStatus);

    @Override
    public void loadMessage() {
        loadStatus()
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
                    ErrorUtils.catchException(throwable);
                });
    }

    @Override
    public void loadMoreMessage(int page, Status lastStatus) {
        loadMoreStatus(page, lastStatus)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(statuses -> {
                    if (isViewAttached()) {
                        if (statuses.isEmpty()) {
                            getView().showMoreMessage(statuses);
                        } else {
                            getView().loadNoMore();
                        }
                    }
                }, throwable -> {
                    ErrorUtils.catchException(throwable);
                    if (isViewAttached()) {
                        getView().loadMoreError();
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
                .subscribe(status -> {
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().opStarFailure(position);
                    }
                });
    }

    private void unStarMessage(String messageId, int position) {
        mFavoriteService.destroyFavorite(messageId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status -> {
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().opStarFailure(position);
                    }
                });
    }

    @Override
    public void opDelete(Status status, int position) {
        mStatusService.destroyStatus(RequestBody.create(MediaType.parse("text/plain"), status.id()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status1 -> {
                }, throwable -> {
                    if (isViewAttached()) {
                        getView().opDeleteFailure(status, position);
                    }
                    throwable.printStackTrace();
                });
    }

    @Override
    public void attachView(V view) {
        mViewRef = new WeakReference<>(view);
    }

    public FeedMVP.View getView() {
        return mViewRef.get();
    }

    public Boolean isViewAttached() {
        return mViewRef != null && mViewRef.get() != null;
    }

    @Override
    public void detachView(boolean retainInstance) {
        if (mViewRef != null) {
            mViewRef.clear();
            mViewRef = null;
        }
    }
}
