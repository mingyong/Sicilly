package xyz.shaohui.sicilly.views.timeline.di;

import dagger.Component;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.data.network.di.FavoriteModule;
import xyz.shaohui.sicilly.data.network.di.StatusModule;
import xyz.shaohui.sicilly.views.timeline.TimelineActivity;
import xyz.shaohui.sicilly.views.timeline.TimelineFragment;
import xyz.shaohui.sicilly.views.timeline.TimelinePresenterImpl;

/**
 * Created by shaohui on 2016/10/19.
 */

@Component(dependencies = AppComponent.class,
modules = {TimelineModule.class, StatusModule.class, FavoriteModule.class})
public interface TimelineComponent {

    void inject(TimelineActivity activity);

    void inject(TimelineFragment fragment);

    TimelinePresenterImpl presenter();

}
