package xyz.shaohui.sicilly.views.home.profile.mvp;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;

/**
 * Created by shaohui on 16/9/20.
 */

public abstract class ProfilePresenter extends MvpBasePresenter<ProfileView> {

    public abstract void fetchUserInfo();

}
