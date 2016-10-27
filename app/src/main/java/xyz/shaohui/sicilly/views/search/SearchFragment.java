package xyz.shaohui.sicilly.views.search;

import android.support.annotation.NonNull;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.views.search.di.SearchComponent;
import xyz.shaohui.sicilly.views.search.mvp.SearchMVP;

/**
 * Created by shaohui on 2016/10/26.
 */

public class SearchFragment extends BaseFragment<SearchMVP.View, SearchMVP.Presenter>
        implements SearchMVP.View {

    @Inject
    EventBus mBus;

    @NonNull
    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public void injectDependencies() {
        SearchComponent component = getComponent(SearchComponent.class);
        component.inject(this);
        presenter = component.presenter();
    }

    @Override
    public int layoutRes() {
        return R.layout.activity_search;
    }
}
