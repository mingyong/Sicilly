package xyz.shaohui.sicilly.views.search.user;

import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.data.network.api.SearchAPI;
import xyz.shaohui.sicilly.utils.ErrorUtils;

/**
 * Created by shaohui on 2016/11/1.
 */

public class SearchUserPresenterImpl extends SearchUserMVP.Presenter {

    private final SearchAPI mSearchService;

    @Inject
    SearchUserPresenterImpl(SearchAPI searchService) {

        mSearchService = searchService;
    }

    @Override
    public void loadStatus(String key, int page) {
        mSearchService.searchUser(key, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(userListBean -> userListBean.getUsers())
                .subscribe(users -> {
                    if (isViewAttached()) {
                        if (page == 1) {
                            if (users.isEmpty()) {
                                getView().loadEmpty();
                            } else {
                                getView().loadDataSuccess(users);
                            }
                        } else {
                            if (users.isEmpty()) {
                                getView().loadNoMore();
                            } else {
                                getView().loadMoreSuccess(users);
                            }
                        }
                    }
                }, throwable -> {
                    ErrorUtils.catchException(throwable);
                    if (isViewAttached()) {
                        if (page == 1) {
                            getView().loadDataFailure();
                        } else {
                            getView().loadMoreError();
                        }
                    }
                });
    }
}
