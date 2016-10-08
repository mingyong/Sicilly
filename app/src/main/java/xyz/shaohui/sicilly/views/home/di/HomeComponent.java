package xyz.shaohui.sicilly.views.home.di;

import dagger.Component;
import xyz.shaohui.sicilly.app.di.AppComponent;
import xyz.shaohui.sicilly.data.network.di.AccountModule;
import xyz.shaohui.sicilly.data.network.di.FavoriteModule;
import xyz.shaohui.sicilly.data.network.di.MessageModule;
import xyz.shaohui.sicilly.data.network.di.StatusModule;
import xyz.shaohui.sicilly.data.network.di.UserModule;
import xyz.shaohui.sicilly.views.home.IndexActivity;
import xyz.shaohui.sicilly.views.home.chat.MessageListFragment;
import xyz.shaohui.sicilly.views.home.chat.mvp.MessageListPresenter;
import xyz.shaohui.sicilly.views.home.profile.ProfileFragment;
import xyz.shaohui.sicilly.views.home.profile.mvp.ProfilePresenter;
import xyz.shaohui.sicilly.views.home.timeline.HomeTimelineFragment;
import xyz.shaohui.sicilly.views.home.timeline.mvp.HomeTimelinePresenter;
import xyz.shaohui.sicilly.views.login.LoginDialogFragment;
import xyz.shaohui.sicilly.views.login.SwitchAccountDialog;

/**
 * Created by shaohui on 16/9/10.
 */

@Component(
        dependencies = AppComponent.class,
        modules = {
                StatusModule.class,
                FavoriteModule.class, UserModule.class, MessageModule.class, AccountModule.class,

                HomeModule.class
        }
)
public interface HomeComponent {

    void inject(IndexActivity activity);

    void inject(HomeTimelineFragment fragment);

    void inject(ProfileFragment fragment);

    void inject(MessageListFragment fragment);

    void inject(SwitchAccountDialog dialog);

    void inject(LoginDialogFragment fragment);

    HomeTimelinePresenter timelinePresenter();

    ProfilePresenter profilePresenter();

    MessageListPresenter messageListPresenter();

}
