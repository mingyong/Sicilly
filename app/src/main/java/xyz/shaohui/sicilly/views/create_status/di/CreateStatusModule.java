package xyz.shaohui.sicilly.views.create_status.di;

import dagger.Module;
import dagger.Provides;
import xyz.shaohui.sicilly.views.create_status.CreateStatusPresenterImpl;
import xyz.shaohui.sicilly.views.create_status.mvp.CreateStatusPresenter;

/**
 * Created by shaohui on 16/9/11.
 */

@Module
public class CreateStatusModule {
    @Provides
    CreateStatusPresenter providePresenter(CreateStatusPresenterImpl presenter) {
        return presenter;
    }
}
