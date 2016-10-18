package xyz.shaohui.sicilly.views.friendship.di;

import dagger.Module;
import dagger.Provides;
import xyz.shaohui.sicilly.views.friendship.FriendRequestPresenterImpl;
import xyz.shaohui.sicilly.views.friendship.mvp.FriendRequestMVP;

/**
 * Created by shaohui on 16/10/18.
 */

@Module
public class FriendshipModule {

    @Provides
    FriendRequestMVP.Presenter provideFriendRequestPresenter(FriendRequestPresenterImpl presenter) {
        return presenter;
    }

}
