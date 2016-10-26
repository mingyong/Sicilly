package xyz.shaohui.sicilly.views.create_status;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.base.HasComponent;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.views.create_status.di.CreateStatusComponent;
import xyz.shaohui.sicilly.views.create_status.di.DaggerCreateStatusComponent;

public class CreateStatusActivity extends BaseActivity
        implements HasComponent<CreateStatusComponent> {

    public static final int TYPE_NONE = 0;
    public static final int TYPE_REPOST = 1;
    public static final int TYPE_REPLY = 2;
    public static final int TYPE_RESTORE = 3;

    @Inject
    EventBus mBus;

    CreateStatusComponent mComponent;

    public static Intent newIntent(Context context, Status status, int type) {
        Intent intent = new Intent(context, CreateStatusActivity.class);
        intent.putExtra("status", status);
        intent.putExtra("type", type);
        return intent;
    }

    public static Intent newIntent(Context context, String text, String path) {
        Intent intent = new Intent(context, CreateStatusActivity.class);
        intent.putExtra("type", TYPE_RESTORE);
        intent.putExtra("text", text);
        intent.putExtra("path", path);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            Status status = getIntent().getParcelableExtra("status");
            int type = getIntent().getIntExtra("type", TYPE_NONE);
            String restoreText = getIntent().getStringExtra("text");
            String restorePath = getIntent().getStringExtra("path");
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new CreateStatusFragmentBuilder().status(status)
                            .type(type)
                            .restoreText(restoreText)
                            .restorePath(restorePath)
                            .build())
                    .commit();
        }
    }

    @Override
    public void initializeInjector() {
        mComponent = DaggerCreateStatusComponent.builder().appComponent(getAppComponent()).build();
        mComponent.inject(this);
    }

    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public CreateStatusComponent getComponent() {
        if (mComponent == null) {
            mComponent =
                    DaggerCreateStatusComponent.builder().appComponent(getAppComponent()).build();
        }
        return mComponent;
    }
}
