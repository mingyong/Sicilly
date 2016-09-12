package xyz.shaohui.sicilly.views.create_status;

import android.os.Bundle;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.base.HasComponent;
import xyz.shaohui.sicilly.views.create_status.di.CreateStatusComponent;
import xyz.shaohui.sicilly.views.create_status.di.DaggerCreateStatusComponent;

public class CreateStatusActivity extends BaseActivity implements HasComponent<CreateStatusComponent>{

    @Inject
    EventBus mBus;

    CreateStatusComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new CreateStatusFragment())
                    .commit();
        }
    }

    @Override
    public void initializeInjector() {
        mComponent = DaggerCreateStatusComponent.builder()
                .appComponent(getAppComponent())
                .build();
        mComponent.inject(this);
    }

    @Override
    public CreateStatusComponent getComponent() {
        return mComponent;
    }
}
