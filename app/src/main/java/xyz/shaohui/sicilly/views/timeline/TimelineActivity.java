package xyz.shaohui.sicilly.views.timeline;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Retrofit;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.base.HasComponent;
import xyz.shaohui.sicilly.views.create_status.DialogController;
import xyz.shaohui.sicilly.views.timeline.di.DaggerTimelineComponent;
import xyz.shaohui.sicilly.views.timeline.di.TimelineComponent;
import xyz.shaohui.sicilly.views.timeline.di.TimelineModule;

public class TimelineActivity extends BaseActivity implements HasComponent<TimelineComponent>,
        DialogController {

    public static final int DATA_TYPE_TIMELINE = 1;
    public static final int DATA_TYPE_STAR = 2;

    @Inject
    EventBus mBus;

    @Inject
    Retrofit mRetrofit;

    private TimelineComponent mComponent;

    public static Intent newIntent(Context context, String userId, int dataType) {
        Intent intent = new Intent(context, TimelineActivity.class);
        intent.putExtra("user_id", userId);
        intent.putExtra("data_type", dataType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new TimelineFragment())
                .commit();
    }

    @Override
    public void initializeInjector() {
        String userId = getIntent().getStringExtra("user_id");
        int dataType = getIntent().getIntExtra("data_type", DATA_TYPE_TIMELINE);
        mComponent = DaggerTimelineComponent.builder()
                .appComponent(getAppComponent())
                .timelineModule(new TimelineModule(userId, dataType))
                .build();
        mComponent.inject(this);
    }

    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public TimelineComponent getComponent() {
        if (mComponent == null) {
            String userId = getIntent().getStringExtra("user_id");
            int dataType = getIntent().getIntExtra("data_type", DATA_TYPE_TIMELINE);
            mComponent = DaggerTimelineComponent.builder()
                    .appComponent(getAppComponent())
                    .timelineModule(new TimelineModule(userId, dataType))
                    .build();
        }
        return mComponent;
    }

    @Override
    public Retrofit getRetrofit() {
        return mRetrofit;
    }
}
