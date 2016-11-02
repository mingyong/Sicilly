package xyz.shaohui.sicilly.views.search;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Retrofit;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.base.HasComponent;
import xyz.shaohui.sicilly.views.create_status.DialogController;
import xyz.shaohui.sicilly.views.search.di.DaggerSearchComponent;
import xyz.shaohui.sicilly.views.search.di.SearchComponent;

public class SearchActivity extends BaseActivity implements HasComponent<SearchComponent>,
        DialogController {

    @Inject
    EventBus mBus;

    @Inject
    Retrofit mRetrofit;

    SearchComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(android.R.id.content, new SearchFragment())
                    .commit();
        }
    }

    @Override
    public void initializeInjector() {
        mComponent = DaggerSearchComponent.builder().appComponent(getAppComponent()).build();
        mComponent.inject(this);
    }

    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public SearchComponent getComponent() {
        return mComponent;
    }

    @Override
    public Retrofit getRetrofit() {
        return mRetrofit;
    }
}
