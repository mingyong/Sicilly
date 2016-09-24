package xyz.shaohui.sicilly.views.feedback;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.base.HasComponent;
import xyz.shaohui.sicilly.views.feedback.di.DaggerFeedbackComponent;
import xyz.shaohui.sicilly.views.feedback.di.FeedbackComponent;

public class FeedbackActivity extends BaseActivity implements HasComponent<FeedbackComponent> {

    @Inject
    EventBus mBus;

    FeedbackComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new FeedbackFragment())
                    .commit();
        }
    }

    @Override
    public void initializeInjector() {
        mComponent =
                DaggerFeedbackComponent.builder().appComponent(getAppComponent()).build();
        mComponent.inject(this);
    }

    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public FeedbackComponent getComponent() {
        return mComponent;
    }
}
