package xyz.shaohui.sicilly.views.search.timeline;

import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.network.api.FavoriteAPI;
import xyz.shaohui.sicilly.data.network.api.SearchAPI;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.views.feed.BaseFeedPresenter;
import xyz.shaohui.sicilly.views.feed.FeedMVP;

/**
 * Created by shaohui on 2016/11/1.
 */

public class SearchTimelinePresenterImpl extends BaseFeedPresenter<SearchTimelineMVP.View> {

    private final SearchAPI mSearchService;

    @Inject
    SearchTimelinePresenterImpl(SearchAPI searchService, FavoriteAPI favoriteService,
            StatusAPI statusService) {
        super(favoriteService, statusService);

        mSearchService = searchService;
    }

    @Override
    protected Observable<List<Status>> loadStatus() {
        if (isViewAttached()) {
            return mSearchService.searchStatus(getView().getKey(), null);
        }
        return Observable.empty();
    }

    @Override
    public Observable<List<Status>> loadMoreStatus(int page, Status lastStatus) {
        if (isViewAttached()) {
            return mSearchService.searchStatus(getView().getKey(), lastStatus.id());
        }
        return Observable.empty();
    }
}
