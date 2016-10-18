package xyz.shaohui.sicilly.views.friendship;

import android.os.Bundle;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.base.HasComponent;
import xyz.shaohui.sicilly.views.friendship.di.DaggerFriendshipComponent;
import xyz.shaohui.sicilly.views.friendship.di.FriendshipComponent;

public class FriendshipActivity extends BaseActivity implements HasComponent<FriendshipComponent> {

    @Inject
    EventBus mBus;

    FriendshipComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new FriendRequestFragment())
                .commit();
    }

    @Override
    public void initializeInjector() {
        mComponent = DaggerFriendshipComponent.builder().appComponent(getAppComponent()).build();
        mComponent.inject(this);
    }

    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public FriendshipComponent getComponent() {
        return mComponent;
    }
}
