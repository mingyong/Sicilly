package xyz.shaohui.sicilly.views.home.di;

import dagger.Module;
import dagger.Provides;
import xyz.shaohui.sicilly.views.home.timeline.HomeTimelinePresenterImpl;
import xyz.shaohui.sicilly.views.home.timeline.mvp.HomeTimelinePresenter;

/**
 * Created by shaohui on 16/9/10.
 */

@Module
class TimelineModule {

    @Provides
    HomeTimelinePresenter homeTimelinePresenter(HomeTimelinePresenterImpl presenter) {
        return presenter;
    }
}
