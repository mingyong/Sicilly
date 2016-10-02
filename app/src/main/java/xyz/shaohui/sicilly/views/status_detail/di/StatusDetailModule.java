package xyz.shaohui.sicilly.views.status_detail.di;

import dagger.Module;
import dagger.Provides;
import xyz.shaohui.sicilly.views.status_detail.StatusDetailPresenterImpl;
import xyz.shaohui.sicilly.views.status_detail.mvp.StatusDetailPresenter;

/**
 * Created by shaohui on 16/9/30.
 */

@Module
public class StatusDetailModule {
    @Provides
    StatusDetailPresenter provideStatusDetailPresenter(StatusDetailPresenterImpl presenter) {
        return presenter;
    }
}
