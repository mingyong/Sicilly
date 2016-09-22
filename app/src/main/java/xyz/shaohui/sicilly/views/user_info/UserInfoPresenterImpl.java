package xyz.shaohui.sicilly.views.user_info;

import javax.inject.Inject;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.data.network.api.FriendshipAPI;
import xyz.shaohui.sicilly.data.network.api.UserAPI;
import xyz.shaohui.sicilly.utils.ErrorUtils;
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
        mUserService.userInfoOther(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (isViewAttached()) {
                        getView().placeUserInfo(user);
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
    public void opFollow(User user) {
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), user.id());
        if (user.following()) {
            if (isViewAttached()) {
                getView().showUnFollowConfirmDialog();
            }
        } else {
            mFriendshipService.create(id)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(result -> {

                    }, throwable -> {
                        ErrorUtils.catchException(throwable);
                        if (isViewAttached()) {
                            getView().followError();
                        }
                    });
        }
    }

    @Override
    public void opUnFollow(User user) {
        RequestBody id = RequestBody.create(MediaType.parse("text/plain"), user.id());
        mFriendshipService.destroy(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {}, throwable -> {
                    if (isViewAttached()) {
                        getView().unFollowError();
                    }
                });
    }

}
