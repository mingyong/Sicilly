package xyz.shaohui.sicilly.views.home.test;

import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.api.FavoriteAPI;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.views.feed.BaseFeedPresenter;
import xyz.shaohui.sicilly.views.home.test.mvp.TimelineMVP;

/**
 * Created by shaohui on 2016/11/27.
 */

public class TimelinePresenterImpl extends BaseFeedPresenter<TimelineMVP.View>
        implements TimelineMVP.Presenter {

    @Inject
    protected TimelinePresenterImpl(FavoriteAPI favoriteService, StatusAPI statusService) {
        super(favoriteService, statusService);
    }

    @Override
    protected Observable<List<Status>> loadStatus() {
        return mStatusService.homeStatus();
    }

    @Override
    public Observable<List<Status>> loadMoreStatus(int page, Status lastStatus) {
        return mStatusService.homeStatusNext(page, lastStatus.id());
    }
}
