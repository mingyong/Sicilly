package xyz.shaohui.sicilly.views.search;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.OnClick;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.views.search.di.SearchComponent;
import xyz.shaohui.sicilly.views.search.event.SearchTimelineEvent;
import xyz.shaohui.sicilly.views.search.event.SearchUserEvent;
import xyz.shaohui.sicilly.views.search.mvp.SearchMVP;

/**
 * Created by shaohui on 2016/10/26.
 */

public class SearchFragment extends BaseFragment<SearchMVP.View, SearchMVP.Presenter>
        implements SearchMVP.View {

    @Inject
    EventBus mBus;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.edit_search)
    EditText mSearch;

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

    @Override
    public void bindViews(View view) {
        SearchAdapter adapter = new SearchAdapter(getFragmentManager(), getContext());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                preformSearch();
                return true;
            }
            return false;
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                    int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                preformSearch();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void preformSearch() {
        String key = mSearch.getText().toString();
        if (TextUtils.isEmpty(key)) {
            return;
        }

        if (mViewPager.getCurrentItem() == 0) {
            mBus.post(new SearchTimelineEvent(key));
        } else {
            mBus.post(new SearchUserEvent(key));
        }

    }

    @OnClick(R.id.btn_back)
    void btnBack() {
        getActivity().finish();
    }
}
