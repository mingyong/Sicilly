package xyz.shaohui.sicilly.views.home.di;

import dagger.Component;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.data.network.di.StatusModule;
import xyz.shaohui.sicilly.views.activities.IndexActivity;
import xyz.shaohui.sicilly.views.home.timeline.HomeTimelineFragment;
import xyz.shaohui.sicilly.views.home.timeline.mvp.HomeTimelinePresenter;

/**
 * Created by shaohui on 16/9/10.
 */

@Component(
        dependencies = AppComponent.class,
        modules = {
                StatusModule.class,

                TimelineModule.class,
                ChatModule.class,
                AboutMeModule.class,
                ProfileModule.class
        }
)
public interface HomeComponent {

    void inject(IndexActivity activity);

    void inject(HomeTimelineFragment fragment);

    HomeTimelinePresenter timelinePresenter();

}
