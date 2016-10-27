package xyz.shaohui.sicilly.views.user_info.photo;

import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.api.UserAPI;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.views.user_info.photo.mvp.UserPhotoPresenter;

/**
 * Created by shaohui on 16/9/18.
 */

public class UserPhotoPresenterImpl extends UserPhotoPresenter {

    UserAPI mUserService;

    @Inject
    UserPhotoPresenterImpl(UserAPI userService) {
        mUserService = userService;
    }

    @Override
    public void fetchPhoto(String userId) {
        mUserService.userPhotoOther(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(statuses -> {
                    if (isViewAttached()) {
                        if (statuses.size() > 0) {
                            getView().showPhotoStatus(statuses);
                        } else {
                            getView().loadEmpty();
                        }
                    }
                }, throwable -> {
                    ErrorUtils.catchException(throwable);
                    if (isViewAttached()) {
                        getView().loadError();
                    }
                });
    }

    @Override
    public void fetchMorePhoto(String userId, int page, Status lastStatus) {
        mUserService.userPhotoOtherNext(userId, page, lastStatus.rawid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(statuses -> {
                    if (isViewAttached()) {
                        if (statuses.size() > 0) {
                            getView().showMorePhotoStatus(statuses);
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
}
