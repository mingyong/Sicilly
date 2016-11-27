package xyz.shaohui.sicilly.views.home.timeline;

import java.util.List;
import javax.inject.Inject;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import org.greenrobot.eventbus.EventBus;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.api.FavoriteAPI;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.views.feed.BaseFeedPresenter;
import xyz.shaohui.sicilly.views.feed.FeedMVP;
import xyz.shaohui.sicilly.views.home.timeline.mvp.HomeTimelinePresenter;

/**
 * Created by shaohui on 16/9/10.
 */

public class HomeTimelinePresenterImpl extends BaseFeedPresenter<FeedMVP.View> {

    @Inject
    HomeTimelinePresenterImpl(StatusAPI statusService, FavoriteAPI favoriteService) {
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
