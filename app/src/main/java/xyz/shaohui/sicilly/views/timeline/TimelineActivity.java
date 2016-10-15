package xyz.shaohui.sicilly.views.timeline;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseActivity;

public class TimelineActivity extends BaseActivity {

    public static final int DATA_TYPE_TIMELINE = 1;
    public static final int DATA_TYPE_STAR = 2;

    @Inject
    EventBus mBus;

    public static Intent newIntent(Context context, String userId, int dataType) {
        Intent intent = new Intent();
        intent.putExtra("user_id", userId);
        intent.putExtra("data_type", dataType);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
    }

    @Override
    public void initializeInjector() {

    }

    @Override
    public EventBus getBus() {
        return mBus;
    }
}
