package xyz.shaohui.sicilly.views.user_info.photo.mvp;

import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;
import xyz.shaohui.sicilly.data.models.Status;

/**
 * Created by shaohui on 16/9/18.
 */

public interface UserPhotoView extends MvpView {

    void showPhotoStatus(List<Status> statuses);

    void showMorePhotoStatus(List<Status> statuses);

    void loadEmpty();

    void loadError();

    void loadMoreError();

    void loadNoMore();

}
