package xyz.shaohui.sicilly.views.search.timeline;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;
import xyz.shaohui.sicilly.data.models.Status;

/**
 * Created by shaohui on 2016/11/1.
 */

public interface SearchTimelineMVP {

    interface View extends MvpView {

        void loadDataSuccess(List<Status> statuses);

        void loadMoreSuccess(List<Status> statuses);

        void loadDataFailure();

        void loadEmpty();

        void loadMoreError();

        void loadNoMore();

    }

    abstract class Presenter extends MvpBasePresenter<View> {

        public abstract void loadStatus(String key, String id);

    }


}
