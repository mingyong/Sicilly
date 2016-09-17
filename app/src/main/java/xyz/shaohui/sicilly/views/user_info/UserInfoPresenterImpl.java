package xyz.shaohui.sicilly.views.user_info;

import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.data.network.api.UserAPI;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.views.user_info.mvp.UserInfoPresenter;

/**
 * Created by shaohui on 16/9/18.
 */

public class UserInfoPresenterImpl extends UserInfoPresenter {

    UserAPI mUserService;

    @Inject
    UserInfoPresenterImpl(UserAPI userService) {
        this.mUserService = userService;
    }

    @Override
    public void fetchUserInfo(String userId) {
        SicillyApplication.getRetrofitService()
                .getUserService().userInfoOther(userId)
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
}
