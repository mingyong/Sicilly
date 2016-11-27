package xyz.shaohui.sicilly.views.status_detail;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.api.FavoriteAPI;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.views.feed.BaseFeedPresenter;
import xyz.shaohui.sicilly.views.feed.FeedMVP;

/**
 * Created by shaohui on 16/9/17.
 */

public class StatusDetailPresenterImpl extends BaseFeedPresenter<FeedMVP.View> {

    private final Status mOriginStatus;

    @Inject
    StatusDetailPresenterImpl(StatusAPI statusService, FavoriteAPI favoriteService, Status status) {
        super(favoriteService, statusService);
        mOriginStatus = status;
    }

    @Override
    protected Observable<List<Status>> loadStatus() {
        List<Status> origin = new ArrayList<>();
        origin.add(mOriginStatus);
        return Observable.concat(Observable.just(origin),
                mStatusService.context(mOriginStatus.id()).filter(statuses -> statuses.size() > 1));
    }

    @Override
    public Observable<List<Status>> loadMoreStatus(int page, Status lastStatus) {
        return Observable.empty();
    }
}
