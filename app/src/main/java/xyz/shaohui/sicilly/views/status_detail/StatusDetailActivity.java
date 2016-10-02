package xyz.shaohui.sicilly.views.status_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.base.HasComponent;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.views.status_detail.di.DaggerStatusDetailComponent;
import xyz.shaohui.sicilly.views.status_detail.di.StatusDetailComponent;

public class StatusDetailActivity extends BaseActivity
        implements HasComponent<StatusDetailComponent> {

    @Inject
    EventBus mBus;

    StatusDetailComponent mComponent;

    public static Intent newIntent(Context context, Status status) {
        Intent intent = new Intent(context, StatusDetailActivity.class);
        intent.putExtra("status", status);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, StatusDetailFragmentBuilder.newStatusDetailFragment(
                        getIntent().getParcelableExtra("status")))
                .commit();
    }

    @Override
    public void initializeInjector() {
        mComponent = DaggerStatusDetailComponent.builder().appComponent(getAppComponent()).build();
        mComponent.inject(this);
    }

    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public StatusDetailComponent getComponent() {
        return mComponent;
    }
}
