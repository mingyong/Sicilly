package xyz.shaohui.sicilly.views.public_square.mvp;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;
import xyz.shaohui.sicilly.data.models.Status;

/**
 * Created by shaohui on 2016/10/27.
 */

public interface PublicSquareMVP {

    interface View extends MvpView {

        void showStatus(List<Status> statuses);

        void loadFail();

        void opStarFailure(int position);
    }

    abstract class Presenter extends MvpBasePresenter<View> {

        public abstract void loadStatus(Integer id);

        public abstract void opStar(Status status, int position);
    }
}
