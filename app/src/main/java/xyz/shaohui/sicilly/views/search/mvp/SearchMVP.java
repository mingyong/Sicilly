package xyz.shaohui.sicilly.views.search.mvp;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by shaohui on 2016/10/26.
 */

public interface SearchMVP {

    interface View extends MvpView {

    }

    abstract class Presenter extends MvpBasePresenter<View> {

    }

}
