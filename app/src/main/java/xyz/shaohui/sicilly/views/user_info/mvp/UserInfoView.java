package xyz.shaohui.sicilly.views.user_info.mvp;

import com.hannesdorfmann.mosby.mvp.MvpView;
import xyz.shaohui.sicilly.data.models.User;

/**
 * Created by shaohui on 16/9/18.
 */

public interface UserInfoView extends MvpView {

    void placeUserInfo(User user, boolean isProtected);

    void loadUserInfoFailure();

    void opFollow();

    void opRequestFollow();

    void showUnFollowConfirmDialog();

    void followError();

    void requestSuccess();

    void unFollowError();


}
