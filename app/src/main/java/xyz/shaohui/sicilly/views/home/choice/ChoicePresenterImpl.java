package xyz.shaohui.sicilly.views.home.choice;

import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import xyz.shaohui.sicilly.base.BasePresenter;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.api.FavoriteAPI;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.views.feed.BaseFeedPresenter;
import xyz.shaohui.sicilly.views.feed.FeedMVP;

/**
 * Created by shaohui on 2017/1/10.
 */

public class ChoicePresenterImpl extends BaseFeedPresenter<FeedMVP.View> {

    @Inject
    protected ChoicePresenterImpl(FavoriteAPI favoriteService, StatusAPI statusService) {
        super(favoriteService, statusService);
    }

    @Override
    protected Observable<List<Status>> loadStatus() {
        return mStatusService.publicStatus();
    }

    @Override
    public Observable<List<Status>> loadMoreStatus(int page, Status lastStatus) {
        return Observable.empty();
    }
}
