package xyz.shaohui.sicilly.views.search.di;

import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import xyz.shaohui.sicilly.views.search.timeline.SearchTimelineMVP;
import xyz.shaohui.sicilly.views.search.timeline.SearchTimelinePresenterImpl;
import xyz.shaohui.sicilly.views.search.user.SearchUserMVP;
import xyz.shaohui.sicilly.views.search.user.SearchUserPresenterImpl;

/**
 * Created by shaohui on 2016/10/26.
 */

@Module
public class SearchActivityModule {

    public static final String SEARCH_KEY = "search_key";

    private String mKey;

    public SearchActivityModule(String key) {
        mKey = key;
    }

    @Named(SEARCH_KEY)
    @Provides
    String provideSearchKey() {
        return mKey;
    }

}
