package xyz.shaohui.sicilly.views.public_square;

import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.api.FavoriteAPI;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.views.feed.BaseFeedPresenter;
import xyz.shaohui.sicilly.views.feed.FeedMVP;

/**
 * Created by shaohui on 2016/10/27.
 */

public class PublicSquarePresenterImpl extends BaseFeedPresenter<FeedMVP.View> {

    @Inject
    PublicSquarePresenterImpl(StatusAPI statusService, FavoriteAPI favoriteService) {
        super(favoriteService, statusService);
    }

    @Override
    protected Observable<List<Status>> loadStatus() {
        return mStatusService.publicStatus();
    }

    @Override
    public Observable<List<Status>> loadMoreStatus(int page, Status lastStatus) {
        return mStatusService.publicStatus();
    }
}
