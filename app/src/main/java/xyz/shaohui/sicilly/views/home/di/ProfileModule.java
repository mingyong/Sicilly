package xyz.shaohui.sicilly.views.home.di;

import dagger.Module;
import dagger.Provides;
import xyz.shaohui.sicilly.views.home.profile.ProfilePresenterImpl;
import xyz.shaohui.sicilly.views.home.profile.mvp.ProfilePresenter;

/**
 * Created by shaohui on 16/9/10.
 */

@Module
class ProfileModule {
    @Provides
    ProfilePresenter provideProfilePresenter(ProfilePresenterImpl presenter) {
        return presenter;
    }
}
