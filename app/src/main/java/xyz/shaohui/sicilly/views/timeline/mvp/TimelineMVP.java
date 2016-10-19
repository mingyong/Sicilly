package xyz.shaohui.sicilly.views.timeline.mvp;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;
import xyz.shaohui.sicilly.data.models.Status;

/**
 * Created by shaohui on 2016/10/19.
 */

public interface TimelineMVP {

    interface View extends MvpView {

        void loadDataSuccess(List<Status> statuses);

        void loadDataFailure();

        void loadEmpty();

        void loadMoreError();

        void loadNoMore();

    }

    abstract class Presenter extends MvpBasePresenter<View> {

        public abstract void loadStatus(int page);

    }

}
