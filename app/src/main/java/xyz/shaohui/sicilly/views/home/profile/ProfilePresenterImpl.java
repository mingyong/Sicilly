package xyz.shaohui.sicilly.views.home.profile;

import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.data.network.api.UserAPI;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.views.home.profile.mvp.ProfilePresenter;

/**
 * Created by shaohui on 16/9/20.
 */

public class ProfilePresenterImpl extends ProfilePresenter {

    private EventBus mBus;

    private UserAPI mUserService;

    @Inject
    ProfilePresenterImpl(EventBus bus, UserAPI userService) {
        mUserService = userService;
        mBus = bus;
    }

    @Override
    public void fetchUserInfo() {
        mUserService.userInfoSelf()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(user -> {
                    if (isViewAttached()) {
                        getView().placeUserInfo(user);
                    }
                }, throwable -> {
                    ErrorUtils.catchException(throwable);
                    if (isViewAttached()) {
                        getView().loadFailure();
                    }
                });
    }
}
