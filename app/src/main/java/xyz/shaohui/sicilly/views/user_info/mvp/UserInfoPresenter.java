package xyz.shaohui.sicilly.views.user_info.mvp;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import xyz.shaohui.sicilly.data.models.User;

/**
 * Created by shaohui on 16/9/18.
 */

public abstract class UserInfoPresenter extends MvpBasePresenter<UserInfoView> {

    public abstract void fetchUserInfo(String userId);

    public abstract void fetchFriendShip(String userId);

    public abstract void opFollowSelector(User user);

    public abstract void opUnFollow(User user);

}
