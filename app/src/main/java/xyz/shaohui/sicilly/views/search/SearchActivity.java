package xyz.shaohui.sicilly.views.search;

import android.os.Bundle;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Retrofit;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.base.HasComponent;
import xyz.shaohui.sicilly.utils.HtmlUtils;
import xyz.shaohui.sicilly.views.create_status.DialogController;
import xyz.shaohui.sicilly.views.search.di.DaggerSearchComponent;
import xyz.shaohui.sicilly.views.search.di.SearchActivityModule;
import xyz.shaohui.sicilly.views.search.di.SearchComponent;

public class SearchActivity extends BaseActivity
        implements HasComponent<SearchComponent>, DialogController {

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
        String key = "";
        if (getIntent().getData() != null) {
            key = HtmlUtils.cleanCatalogScheme(getIntent().getData());
            try {
                key = URLDecoder.decode(key, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                key = null;
                e.printStackTrace();
            }
        }
        mComponent = DaggerSearchComponent.builder()
                .appComponent(getAppComponent())
                .searchActivityModule(new SearchActivityModule(key))
                .build();
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
