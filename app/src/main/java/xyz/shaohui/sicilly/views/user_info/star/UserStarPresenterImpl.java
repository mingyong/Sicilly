package xyz.shaohui.sicilly.views.user_info.star;

import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;
import rx.Observable;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.api.FavoriteAPI;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.views.feed.BaseFeedPresenter;
import xyz.shaohui.sicilly.views.feed.FeedMVP;
import xyz.shaohui.sicilly.views.user_info.di.UserInfoPresenterModule;

/**
 * Created by shaohui on 2017/1/11.
 */

public class UserStarPresenterImpl extends BaseFeedPresenter<FeedMVP.View> {

    private final String mUserId;

    @Inject
    UserStarPresenterImpl(FavoriteAPI favoriteService, StatusAPI statusService,
            @Named(UserInfoPresenterModule.USER_ID) String userId) {
        super(favoriteService, statusService);
        mUserId = userId;
    }

    @Override
    protected Observable<List<Status>> loadStatus() {
        return mFavoriteService.favoriteStatusList(mUserId, 1);
    }

    @Override
    public Observable<List<Status>> loadMoreStatus(int page, Status lastStatus) {
        return mFavoriteService.favoriteStatusList(mUserId, page);
    }
}
