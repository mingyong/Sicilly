package xyz.shaohui.sicilly.views.user_info.mvp;

import com.hannesdorfmann.mosby.mvp.MvpView;
import xyz.shaohui.sicilly.data.models.User;

/**
 * Created by shaohui on 16/9/18.
 */

public interface UserInfoView extends MvpView {

    void placeUserInfo(User user);

    void loadUserInfoFailure();

    void opFollow();

    void showUnFollowConfirmDialog();

    void followError();

    void unFollowError();


}
