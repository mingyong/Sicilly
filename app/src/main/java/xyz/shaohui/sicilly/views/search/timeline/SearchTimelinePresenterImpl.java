package xyz.shaohui.sicilly.views.search.timeline;

import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.data.network.api.SearchAPI;
import xyz.shaohui.sicilly.utils.ErrorUtils;

/**
 * Created by shaohui on 2016/11/1.
 */

public class SearchTimelinePresenterImpl extends SearchTimelineMVP.Presenter {

    private final SearchAPI mSearchService;

    @Inject
    SearchTimelinePresenterImpl(SearchAPI searchService) {

        mSearchService = searchService;
    }

    @Override
    public void loadStatus(String key, String id) {
        mSearchService.searchStatus(key, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(status -> {
                    if (isViewAttached()) {
                        if (id != null) {
                            if (status.isEmpty()) {
                                getView().loadNoMore();
                            } else {
                                getView().loadMoreSuccess(status);
                            }
                        } else {
                            if (status.isEmpty()) {
                                getView().loadEmpty();
                            } else {
                                getView().loadDataSuccess(status);
                            }
                        }
                    }
                }, throwable -> {
                    ErrorUtils.catchException(throwable);
                    if (isViewAttached()) {
                        if (id == null) {
                            getView().loadDataFailure();
                        } else {
                            getView().loadMoreError();
                        }
                    }
                });
    }
}
