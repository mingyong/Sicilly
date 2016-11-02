package xyz.shaohui.sicilly.views.search.user;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.models.User;

/**
 * Created by shaohui on 2016/11/1.
 */

public interface SearchUserMVP {

    interface View extends MvpView {

        void loadDataSuccess(List<User> statuses);

        void loadMoreSuccess(List<User> statuses);

        void loadDataFailure();

        void loadEmpty();

        void loadMoreError();

        void loadNoMore();

    }

    abstract class Presenter extends MvpBasePresenter<View> {

        public abstract void loadStatus(String key, int page);

    }

}
