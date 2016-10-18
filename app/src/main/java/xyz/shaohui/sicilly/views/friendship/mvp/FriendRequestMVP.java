package xyz.shaohui.sicilly.views.friendship.mvp;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;
import xyz.shaohui.sicilly.data.models.User;

/**
 * Created by shaohui on 16/10/18.
 */

public interface FriendRequestMVP {

    interface View extends MvpView {

        void loadRequestSuccess(List<User> users);

        void loadError();

        void loadEmpty();

        void loadNoMore();

        void loadMoreError();

        void acceptRequestSuccess(int position, String uid);

        void denyRequestSuccess(int position, String uid);

        void opFailed();

    }

    abstract class Presenter extends MvpBasePresenter<View> {

        public abstract void loadRequest(int page);

        public abstract void acceptRequest(int position, String uid);

        public abstract void denyRequest(int position, String uid);
    }
}
