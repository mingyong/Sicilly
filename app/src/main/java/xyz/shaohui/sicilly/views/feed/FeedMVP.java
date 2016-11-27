package xyz.shaohui.sicilly.views.feed;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;
import xyz.shaohui.sicilly.data.models.Status;

/**
 * Created by shaohui on 2016/11/26.
 */

public interface FeedMVP {

    public interface View extends MvpView {

        void showMessage(List<Status> statuses);

        void showMoreMessage(List<Status> statuses);

        void showRefresh();

        void networkError();

        void loadNoMore();

        void loadMoreError();

        void opStarFailure(int position);

        void opDeleteFailure(Status status, int position);

    }

    public interface Presenter<V extends View> extends MvpPresenter<V> {

        void loadMessage();

        void loadMoreMessage(int page, Status lastStatus);

        void opStar(Status status, int position);

        void opDelete(Status status, int position);

    }

}
