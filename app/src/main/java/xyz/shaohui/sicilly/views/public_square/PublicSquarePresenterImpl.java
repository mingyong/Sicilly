package xyz.shaohui.sicilly.views.public_square;

import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.api.FavoriteAPI;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.views.public_square.mvp.PublicSquareMVP;

/**
 * Created by shaohui on 2016/10/27.
 */

public class PublicSquarePresenterImpl extends PublicSquareMVP.Presenter {

    private final StatusAPI mStatusService;

    private final FavoriteAPI mFavoriteService;

    @Inject
    PublicSquarePresenterImpl(StatusAPI statusService, FavoriteAPI favoriteService) {

        mStatusService = statusService;
        mFavoriteService = favoriteService;
    }

    @Override
    public void loadStatus(Integer id) {
        mStatusService.publicStatus()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(statuses -> {
                    if (isViewAttached()) {
                        getView().showStatus(statuses);
                    }
                }, throwable -> {
                    ErrorUtils.catchException(throwable);
                    if (isViewAttached()) {
                        getView().loadFail();
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

}
