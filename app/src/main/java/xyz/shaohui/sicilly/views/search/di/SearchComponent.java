package xyz.shaohui.sicilly.views.search.di;

import dagger.Component;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.data.network.di.FavoriteModule;
import xyz.shaohui.sicilly.data.network.di.SearchModule;
import xyz.shaohui.sicilly.data.network.di.StatusModule;
import xyz.shaohui.sicilly.views.search.SearchActivity;
import xyz.shaohui.sicilly.views.search.SearchFragment;
import xyz.shaohui.sicilly.views.search.timeline.SearchTimelineFragment;
import xyz.shaohui.sicilly.views.search.timeline.SearchTimelinePresenterImpl;
import xyz.shaohui.sicilly.views.search.user.SearchUserFragment;

/**
 * Created by shaohui on 2016/10/26.
 */

@Component(dependencies = AppComponent.class, modules = {
        SearchActivityModule.class, StatusModule.class, FavoriteModule.class, SearchModule.class
})
public interface SearchComponent {

    void inject(SearchActivity activity);

    void inject(SearchFragment fragment);

    void inject(SearchTimelineFragment fragment);

    void inject(SearchUserFragment fragment);

    SearchTimelinePresenterImpl timelinePresenter();
}
