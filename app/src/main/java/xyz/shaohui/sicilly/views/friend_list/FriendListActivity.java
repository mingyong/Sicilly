package xyz.shaohui.sicilly.views.friend_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.base.HasComponent;
import xyz.shaohui.sicilly.views.friend_list.di.DaggerFriendListComponent;
import xyz.shaohui.sicilly.views.friend_list.di.FriendListComponent;
import xyz.shaohui.sicilly.views.friend_list.di.FriendListModule;

public class FriendListActivity extends BaseActivity implements HasComponent<FriendListComponent> {

    public static final int VIEW_TYPE_TINY = 1;
    public static final int VIEW_TYPE_FULL = 2;

    public static final int DATA_TYPE_FRIEND = 3;
    public static final int DATA_TYPE_FOLLOWER = 4;

    public static final String RESULT_DATA = "result_data";

    @Inject
    EventBus mBus;

    FriendListComponent mComponent;

    private int mDataType;

    private int mViewType;

    private String mUserId;

    public static Intent newIntent(Context context, String userId, int dataType, int viewType) {
        Intent intent = new Intent(context, FriendListActivity.class);
        intent.putExtra("view_type", viewType);
        intent.putExtra("data_type", dataType);
        intent.putExtra("user_id", userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new FriendListFragment())
                .commit();

    }

    @Override
    public void initializeInjector() {
        mViewType = getIntent().getIntExtra("view_type", 2);
        mDataType = getIntent().getIntExtra("data_type", 3);
        mUserId = getIntent().getStringExtra("user_id");
        if (mUserId == null) {
            mUserId = SicillyApplication.currentUId();
        }

        mComponent = DaggerFriendListComponent.builder()
                .appComponent(getAppComponent())
                .friendListModule(new FriendListModule(mUserId, mDataType, mViewType))
                .build();
        mComponent.inject(this);
    }

    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public FriendListComponent getComponent() {
        if (mComponent == null) {
            mComponent = DaggerFriendListComponent.builder()
                    .appComponent(getAppComponent())
                    .friendListModule(new FriendListModule(mUserId, mDataType, mViewType))
                    .build();
        }
        return mComponent;
    }
}
