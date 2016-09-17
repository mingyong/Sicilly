package xyz.shaohui.sicilly.views.user_info.mvp;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by shaohui on 16/9/18.
 */

public abstract class UserInfoPresenter extends MvpBasePresenter<UserInfoView> {

    public abstract void fetchUserInfo(String userId);

}
