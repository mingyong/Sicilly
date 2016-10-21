package xyz.shaohui.sicilly.views.status_detail.mvp;

import com.hannesdorfmann.mosby.mvp.MvpView;
import java.util.List;
import xyz.shaohui.sicilly.data.models.Status;

/**
 * Created by shaohui on 16/9/17.
 */

public interface StatusDetailView extends MvpView {

    void showStatus(List<Status> statuses);

    void deleteStatusFailure(Status status, int position);
}
