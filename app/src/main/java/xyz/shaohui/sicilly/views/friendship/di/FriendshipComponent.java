package xyz.shaohui.sicilly.views.friendship.di;

import dagger.Component;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.views.friendship.FriendRequestFragment;
import xyz.shaohui.sicilly.views.friendship.FriendshipActivity;
import xyz.shaohui.sicilly.views.friendship.mvp.FriendRequestMVP;

/**
 * Created by shaohui on 16/10/18.
 */

@Component(dependencies = AppComponent.class,
modules = {FriendshipModule.class, xyz.shaohui.sicilly.data.network.di.FriendshipModule.class})
public interface FriendshipComponent {

    void inject(FriendshipActivity activity);

    void inject(FriendRequestFragment fragment);

    FriendRequestMVP.Presenter friendRequestPresenter();

}
