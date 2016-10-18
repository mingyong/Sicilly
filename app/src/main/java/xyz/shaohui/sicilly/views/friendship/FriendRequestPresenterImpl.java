package xyz.shaohui.sicilly.views.friendship;

import javax.inject.Inject;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.data.network.api.FriendshipAPI;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.views.friendship.mvp.FriendRequestMVP;

/**
 * Created by shaohui on 16/10/18.
 */

public class FriendRequestPresenterImpl extends FriendRequestMVP.Presenter {

    private final FriendshipAPI mFriendshipService;

    @Inject
    FriendRequestPresenterImpl(FriendshipAPI friendshipService) {
        mFriendshipService = friendshipService;
    }

    @Override
    public void loadRequest(int page) {
        mFriendshipService.requestList(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(users -> {
                    if (isViewAttached()) {
                        if (users.isEmpty()) {
                            if (page == 1) {
                                getView().loadEmpty();
                            } else {
                                getView().loadNoMore();
                            }
                        } else {
                            getView().loadRequestSuccess(users);
                        }
                    }
                }, throwable -> {
                    if (isViewAttached()) {
                        if (page == 1) {
                            getView().loadError();
                        } else {
                            getView().loadMoreError();
                        }
                    }
                });
    }

    @Override
    public void acceptRequest(int position, String uid) {
        mFriendshipService.accept(RequestBody.create(MediaType.parse("text/plain"), uid))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (isViewAttached()) {
                        getView().acceptRequestSuccess(position, uid);
                    }
                }, throwable -> {
                    ErrorUtils.catchException(throwable);
                    if (isViewAttached()) {
                        getView().opFailed();
                    }
                });
    }

    @Override
    public void denyRequest(int position, String uid) {
        mFriendshipService.deny(RequestBody.create(MediaType.parse("text/plain"), uid))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (isViewAttached()) {
                        getView().denyRequestSuccess(position, uid);
                    }
                }, throwable -> {
                    ErrorUtils.catchException(throwable);
                    if (isViewAttached()) {
                        getView().opFailed();
                    }
                });
    }
}
