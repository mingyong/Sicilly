package xyz.shaohui.sicilly.views.status_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.util.concurrent.Callable;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Retrofit;
import rx.Observable;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.base.HasComponent;
import xyz.shaohui.sicilly.data.SPDataManager;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.views.create_status.DialogController;
import xyz.shaohui.sicilly.views.status_detail.di.DaggerStatusDetailComponent;
import xyz.shaohui.sicilly.views.status_detail.di.StatusDetailComponent;

public class StatusDetailActivity extends BaseActivity
        implements HasComponent<StatusDetailComponent>, DialogController{

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
                .replace(android.R.id.content, StatusDetailFragmentBuilder.newStatusDetailFragment(
                        getIntent().getParcelableExtra("status")))
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

    @Override
    public Retrofit getRetrofit() {
        return mRetrofit;
    }
}
