package xyz.shaohui.sicilly.views.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import javax.inject.Inject;
import javax.inject.Named;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.HasComponent;
import xyz.shaohui.sicilly.views.search.di.SearchActivityModule;
import xyz.shaohui.sicilly.views.search.di.SearchComponent;
import xyz.shaohui.sicilly.views.search.event.SearchTimelineEvent;
import xyz.shaohui.sicilly.views.search.event.SearchUserEvent;

/**
 * Created by shaohui on 2016/10/26.
 */

public class SearchFragment extends Fragment {

    @Inject
    EventBus mBus;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    @BindView(R.id.tab_layout)
    TabLayout mTabLayout;

    @BindView(R.id.edit_search)
    EditText mSearch;

    @Named(SearchActivityModule.SEARCH_KEY)
    @Inject
    String mCatalogKey;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectDependencies();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(layoutRes(), container, false);
        ButterKnife.bind(this, v);
        bindViews(v);
        return v;
    }

    public void injectDependencies() {
        SearchComponent component = getComponent(SearchComponent.class);
        component.inject(this);
    }

    public <C> C getComponent(Class<C> componentType) {
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

    public int layoutRes() {
        return R.layout.activity_search;
    }

    public void bindViews(View view) {
        SearchAdapter adapter = new SearchAdapter(getFragmentManager(), getContext());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        mSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                preformSearch();
                hideSoftInput();
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

        if (!TextUtils.isEmpty(mCatalogKey)) {
            mSearch.setText(mCatalogKey);
            mSearch.postDelayed(() -> {
                preformSearch();
                hideSoftInput();
            }, 100);
        }
    }

    private void hideSoftInput() {
        InputMethodManager inputMethodManager =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
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
