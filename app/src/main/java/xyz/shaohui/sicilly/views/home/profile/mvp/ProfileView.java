package xyz.shaohui.sicilly.views.home.profile.mvp;

import com.hannesdorfmann.mosby.mvp.MvpView;
import xyz.shaohui.sicilly.data.models.User;

/**
 * Created by shaohui on 16/9/20.
 */

public interface ProfileView extends MvpView {

    void placeUserInfo(User user);

    void loadFailure();

}
