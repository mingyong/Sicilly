package xyz.shaohui.sicilly.views.search.di;

import dagger.Module;
import dagger.Provides;
import xyz.shaohui.sicilly.views.search.SearchPresenterImpl;
import xyz.shaohui.sicilly.views.search.mvp.SearchMVP;
import xyz.shaohui.sicilly.views.search.timeline.SearchTimelineMVP;
import xyz.shaohui.sicilly.views.search.timeline.SearchTimelinePresenterImpl;
import xyz.shaohui.sicilly.views.search.user.SearchUserMVP;
import xyz.shaohui.sicilly.views.search.user.SearchUserPresenterImpl;

/**
 * Created by shaohui on 2016/10/26.
 */

@Module
public class SearchModule {

    @Provides
    SearchMVP.Presenter providePresenter(SearchPresenterImpl presenter) {
        return presenter;
    }

    @Provides
    SearchTimelineMVP.Presenter provideTimelinePresenter(SearchTimelinePresenterImpl presenter) {
        return presenter;
    }

    @Provides
    SearchUserMVP.Presenter provideUserPresenter(SearchUserPresenterImpl presenter) {
        return presenter;
    }

}
