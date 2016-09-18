package xyz.shaohui.sicilly.views.user_info.photo.mvp;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import xyz.shaohui.sicilly.data.models.Status;

/**
 * Created by shaohui on 16/9/18.
 */

public abstract class UserPhotoPresenter extends MvpBasePresenter<UserPhotoView>{

    public abstract void fetchPhoto(String userId);

    public abstract void fetchMorePhoto(String userId, int page, Status lastStatus);

}
