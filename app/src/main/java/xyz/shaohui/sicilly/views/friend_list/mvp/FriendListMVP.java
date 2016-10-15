package xyz.shaohui.sicilly.views.friend_list.mvp;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;
import xyz.shaohui.sicilly.data.models.User;

/**
 * Created by shaohui on 16/10/14.
 */

public interface FriendListMVP {

    interface View extends MvpView {
        void loadUserSuccess(List<User> users);

        void followUserSuccess(int position, User user);

        void loadError();

        void loadMoreError();

        void loadNoMore();
    }

    abstract class Presenter extends MvpBasePresenter<View> {

        public abstract void loadUser(int page);

        public abstract void followUser(int position, User user);

    }
}
