package xyz.shaohui.sicilly.views.home.chat.mention;

import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.api.FavoriteAPI;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.views.feed.BaseFeedPresenter;
import xyz.shaohui.sicilly.views.feed.FeedMVP;

/**
 * Created by shaohui on 2016/11/27.
 */

public class MentionPresenter extends BaseFeedPresenter<FeedMVP.View> {

    @Inject
    MentionPresenter(FavoriteAPI favoriteService, StatusAPI statusService) {
        super(favoriteService, statusService);
    }

    @Override
    protected Observable<List<Status>> loadStatus() {
        return mStatusService.mentionsStatus();
    }

    @Override
    public Observable<List<Status>> loadMoreStatus(int page, Status lastStatus) {
        return mStatusService.mentionsStatusNext(page, lastStatus.id());
    }
}
