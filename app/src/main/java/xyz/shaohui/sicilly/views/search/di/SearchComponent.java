package xyz.shaohui.sicilly.views.search.di;

import dagger.Component;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.views.search.SearchActivity;
import xyz.shaohui.sicilly.views.search.SearchFragment;
import xyz.shaohui.sicilly.views.search.mvp.SearchMVP;
import xyz.shaohui.sicilly.views.search.timeline.SearchTimelineFragment;
import xyz.shaohui.sicilly.views.search.timeline.SearchTimelineMVP;
import xyz.shaohui.sicilly.views.search.user.SearchUserFragment;
import xyz.shaohui.sicilly.views.search.user.SearchUserMVP;

/**
 * Created by shaohui on 2016/10/26.
 */

@Component(dependencies = AppComponent.class,
        modules = { SearchModule.class, xyz.shaohui.sicilly.data.network.di.SearchModule.class })
public interface SearchComponent {

    void inject(SearchActivity activity);

    void inject(SearchFragment fragment);

    void inject(SearchTimelineFragment fragment);

    void inject(SearchUserFragment fragment);

    SearchMVP.Presenter presenter();

    SearchTimelineMVP.Presenter timelinePresenter();

    SearchUserMVP.Presenter userPresenter();
}
