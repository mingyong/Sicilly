package xyz.shaohui.sicilly.views.search.di;

import dagger.Component;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.views.search.SearchActivity;
import xyz.shaohui.sicilly.views.search.SearchFragment;
import xyz.shaohui.sicilly.views.search.mvp.SearchMVP;

/**
 * Created by shaohui on 2016/10/26.
 */

@Component(dependencies = AppComponent.class, modules = SearchModule.class)
public interface SearchComponent {

    void inject(SearchActivity activity);

    void inject(SearchFragment fragment);

    SearchMVP.Presenter presenter();

}
