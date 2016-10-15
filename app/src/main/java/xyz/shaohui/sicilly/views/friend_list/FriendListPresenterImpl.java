package xyz.shaohui.sicilly.views.friend_list;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.data.network.api.UserAPI;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.views.friend_list.di.FriendListModule;
import xyz.shaohui.sicilly.views.friend_list.mvp.FriendListMVP;

/**
 * Created by shaohui on 16/10/14.
 */

public class FriendListPresenterImpl extends FriendListMVP.Presenter {

    private final UserAPI mUserService;

    private final int mDataType;

    private final String mUserId;

    @Inject
    FriendListPresenterImpl(UserAPI userService,
            @Named(FriendListModule.NAME_DATA_TYPE) int data_type,
            @Named(FriendListModule.NAME_USER_ID) String userId) {

        mUserService = userService;
        mDataType = data_type;
        mUserId = userId;
    }

    @Override
    public void loadUser(int page) {
        Observable<List<User>> observable;
        if (mDataType == FriendListActivity.DATA_TYPE_FRIEND) {
            observable = mUserService.userFriends(mUserId, page);
        } else {
            observable = mUserService.userFollowers(mUserId, page);
        }
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {
                    if (isViewAttached()) {
                        if (users.isEmpty() && page > 1) {
                            getView().loadNoMore();
                        } else {
                            getView().loadUserSuccess(users);
                        }
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        ErrorUtils.catchException(throwable);
                        if (page == 1) {
                            getView().loadError();
                        } else {
                            getView().loadMoreError();
                        }
                    }
                });
    }

    @Override
    public void followUser(int position, User user) {

    }
}
