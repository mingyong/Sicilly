package xyz.shaohui.sicilly.views.status_detail.mvp;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import xyz.shaohui.sicilly.data.models.Status;

/**
 * Created by shaohui on 16/9/17.
 */

public abstract class StatusDetailPresenter extends MvpBasePresenter<StatusDetailView> {

    public abstract void loadStatus(Status origin);

}
