package xyz.shaohui.sicilly.views.status_detail.di;

import dagger.Component;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.data.network.api.StatusAPI;
import xyz.shaohui.sicilly.data.network.di.StatusModule;
import xyz.shaohui.sicilly.views.status_detail.StatusDetailActivity;
import xyz.shaohui.sicilly.views.status_detail.StatusDetailFragment;
import xyz.shaohui.sicilly.views.status_detail.mvp.StatusDetailPresenter;

/**
 * Created by shaohui on 16/9/30.
 */

@Component(dependencies = AppComponent.class,
        modules = {
                StatusDetailModule.class, StatusModule.class
        })
public interface StatusDetailComponent {

    void inject(StatusDetailActivity activity);

    void inject(StatusDetailFragment fragment);

    StatusDetailPresenter presenter();
}
