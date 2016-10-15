package xyz.shaohui.sicilly.views.friend_list.di;

import dagger.Component;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.data.network.di.UserModule;
import xyz.shaohui.sicilly.views.friend_list.FriendListActivity;
import xyz.shaohui.sicilly.views.friend_list.FriendListFragment;
import xyz.shaohui.sicilly.views.friend_list.mvp.FriendListMVP;

/**
 * Created by shaohui on 16/10/14.
 */

@Component(dependencies = AppComponent.class, modules = {FriendListModule.class, UserModule.class})
public interface FriendListComponent {

    void inject(FriendListActivity activity);

    void inject(FriendListFragment fragment);

    FriendListMVP.Presenter presenter();

}

