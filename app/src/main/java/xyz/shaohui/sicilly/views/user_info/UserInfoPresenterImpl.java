package xyz.shaohui.sicilly.views.user_info;

import android.util.Pair;
import javax.inject.Inject;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.data.network.api.FriendshipAPI;
import xyz.shaohui.sicilly.data.network.api.UserAPI;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.utils.RxUtils;
import xyz.shaohui.sicilly.views.user_info.mvp.UserInfoPresenter;

/**
 * Created by shaohui on 16/9/18.
 */

public class UserInfoPresenterImpl extends UserInfoPresenter {

    private UserAPI mUserService;

    private FriendshipAPI mFriendshipService;

    @Inject
    UserInfoPresenterImpl(UserAPI userService, FriendshipAPI friendshipAPI) {
        this.mUserService = userService;
        mFriendshipService = friendshipAPI;
    }

    @Override
    public void fetchUserInfo(String userId) {

        Observable.zip(mUserService.userInfoOther(userId),
                mFriendshipService.exist(SicillyApplication.currentUId(), userId), (Pair::create))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pair -> {
                    if (isViewAttached()) {
                        getView().placeUserInfo(pair.first, false);
                    }
                }, throwable -> {
                    ErrorUtils.catchException(throwable);
                    if (isViewAttached()) {
                        getView().loadUserInfoFailure();
                    }
                });
    }

    @Override
    public void fetchFriendShip(String userId) {
    }

    @Override
    public void opFollowSelector(User user) {
        if (user.following()) {
            if (isViewAttached()) {
                getView().showUnFollowConfirmDialog();
            }
        } else {
            opFollow(user);
        }
    }

    private void opFollow(User user) {
        if (isViewAttached()) {
            getView().opFollow();
        }
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), user.id());
        mFriendshipService.create(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                }, throwable -> {
                    if (isViewAttached()) {
                        if (throwable instanceof HttpException) {
                            if (((HttpException) throwable).code() == 403) {
                                getView().requestSuccess();
                            }
                        } else {
                            ErrorUtils.catchException(throwable);
                            getView().followError();
                        }
                    }
                });
    }

    @Override
    public void opUnFollow(User user) {
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), user.id());
        mFriendshipService.destroy(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                }, throwable -> {
                    ErrorUtils.catchException(throwable);
                    if (isViewAttached()) {
                        getView().unFollowError();
                    }
                });
    }
}
