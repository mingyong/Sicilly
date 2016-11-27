package xyz.shaohui.sicilly.views.timeline;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import rx.Observable;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.api.FavoriteAPI;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.views.feed.BaseFeedPresenter;
import xyz.shaohui.sicilly.views.feed.FeedMVP;
import xyz.shaohui.sicilly.views.timeline.di.TimelineModule;

/**
 * Created by shaohui on 2016/10/19.
 */

public class TimelinePresenterImpl extends BaseFeedPresenter<FeedMVP.View> {

    private final String mUserId;

    private final int mDataType;

    @Inject
    TimelinePresenterImpl(@Named(TimelineModule.TIMELINE_USER_ID) String userId,
            @Named(TimelineModule.TIMELINE_DATA_TYPE) int dataType, StatusAPI statusService,
            FavoriteAPI favoriteService) {
        super(favoriteService, statusService);

        mUserId = userId;
        mDataType = dataType;
    }

    @Override
    protected Observable<List<Status>> loadStatus() {
        if (mDataType == TimelineActivity.DATA_TYPE_TIMELINE) {
            return mStatusService.userTimeline(mUserId);
        } else {
            return mFavoriteService.favoriteStatusList(mUserId, 1);
        }
    }

    @Override
    public Observable<List<Status>> loadMoreStatus(int page, Status lastStatus) {
        if (mDataType == TimelineActivity.DATA_TYPE_TIMELINE) {
            return mStatusService.userTimelineNext(mUserId, page, lastStatus.id());
        } else {
            return mFavoriteService.favoriteStatusList(mUserId, page);
        }
    }
}
