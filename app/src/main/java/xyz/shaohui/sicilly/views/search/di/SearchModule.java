package xyz.shaohui.sicilly.views.search.di;

import dagger.Module;
import dagger.Provides;
import xyz.shaohui.sicilly.views.search.SearchPresenterImpl;
import xyz.shaohui.sicilly.views.search.mvp.SearchMVP;

/**
 * Created by shaohui on 2016/10/26.
 */

@Module
public class SearchModule {

    @Provides
    SearchMVP.Presenter providePresenter(SearchPresenterImpl presenter) {
        return presenter;
    }

}
