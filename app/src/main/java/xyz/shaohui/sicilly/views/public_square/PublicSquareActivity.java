package xyz.shaohui.sicilly.views.public_square;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Retrofit;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.base.HasComponent;
import xyz.shaohui.sicilly.views.create_status.DialogController;
import xyz.shaohui.sicilly.views.public_square.di.DaggerPublicSquareComponent;
import xyz.shaohui.sicilly.views.public_square.di.PublicSquareComponent;

public class PublicSquareActivity extends BaseActivity implements HasComponent<PublicSquareComponent>,
        DialogController {

    @Inject
    EventBus mBus;

    @Inject
    Retrofit mRetrofit;

    PublicSquareComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PublicSquareFragment())
                .commit();
    }

    @Override
    public void initializeInjector() {
        mComponent = DaggerPublicSquareComponent.builder().appComponent(getAppComponent()).build();
        mComponent.inject(this);
    }

    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public PublicSquareComponent getComponent() {
        return mComponent;
    }

    @Override
    public Retrofit getRetrofit() {
        return mRetrofit;
    }
}
