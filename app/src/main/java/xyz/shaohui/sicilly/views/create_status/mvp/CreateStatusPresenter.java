package xyz.shaohui.sicilly.views.create_status.mvp;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import xyz.shaohui.sicilly.data.models.Status;

/**
 * Created by shaohui on 16/9/11.
 */

public abstract class CreateStatusPresenter extends MvpBasePresenter<CreateStatusView> {

    public abstract void sendStatus(String text, String path, Status originStatus, int type);

}
