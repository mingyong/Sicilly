package xyz.shaohui.sicilly.views.status_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Retrofit;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.base.HasComponent;
import xyz.shaohui.sicilly.data.SPDataManager;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.views.create_status.DialogController;
import xyz.shaohui.sicilly.views.status_detail.di.DaggerStatusDetailComponent;
import xyz.shaohui.sicilly.views.status_detail.di.StatusDetailComponent;
import xyz.shaohui.sicilly.views.status_detail.di.StatusDetailModule;

public class StatusDetailActivity extends BaseActivity
        implements HasComponent<StatusDetailComponent>, DialogController {

    @Inject
    EventBus mBus;

    @Inject
    Retrofit mRetrofit;

    StatusDetailComponent mComponent;

    public static Intent newIntent(Context context, Status status) {
        Intent intent = new Intent(context, StatusDetailActivity.class);
        intent.putExtra("status", status);
        return intent;
    }

    public static Intent newIntent(Context context, Status status, boolean forMention) {
        Intent intent = new Intent(context, StatusDetailActivity.class);
        intent.putExtra("status", status);
        intent.putExtra("for_mention", forMention);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new StatusDetailFragment())
                .commit();
        boolean forMention = getIntent().getBooleanExtra("for_mention", false);
        if (forMention) {
            int mention = SPDataManager.getInt(SPDataManager.SP_KEY_MENTION, 0);
            if (mention > 0) {
                SPDataManager.setInt(SPDataManager.SP_KEY_MENTION, mention - 1, true);
            }
        }
    }

    @Override
    public void initializeInjector() {
        Status status = getIntent().getParcelableExtra("status");
        mComponent = DaggerStatusDetailComponent.builder()
                .appComponent(getAppComponent())
                .statusDetailModule(new StatusDetailModule(status))
                .build();
        mComponent.inject(this);
    }

    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public StatusDetailComponent getComponent() {
        if (mComponent == null) {
            mComponent =
                    DaggerStatusDetailComponent.builder().appComponent(getAppComponent()).build();
        }
        return mComponent;
    }

    @Override
    public Retrofit getRetrofit() {
        return mRetrofit;
    }
}
