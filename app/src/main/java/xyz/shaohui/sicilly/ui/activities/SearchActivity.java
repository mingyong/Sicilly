package xyz.shaohui.sicilly.ui.activities;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.ui.fragments.StatusListFragment;
import xyz.shaohui.sicilly.ui.fragments.UserListFragment;

public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.tool_bar)Toolbar toolbar;
    @Bind(R.id.tab_layout)TabLayout tabLayout;
    @Bind(R.id.search_edit)EditText searchEdit;

    private FragmentManager fm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        fm = getSupportFragmentManager();

        initToolbar();
    }

    private void initToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tabLayout.setTabsFromPagerAdapter(new TabAdapter(this));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                searchMore();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @OnClick(R.id.search_submit)
    void searchMore() {
        String q = searchEdit.getText().toString();
        if (TextUtils.isEmpty(q)) {
            return;
        }
        switch (tabLayout.getSelectedTabPosition()) {
            case 0:
                fm.beginTransaction()
                        .replace(R.id.main_frame, StatusListFragment.newInstance(q))
                        .commit();
                break;
            case 1:
                fm.beginTransaction()
                        .replace(R.id.main_frame, UserListFragment.newInstance(q))
                        .commit();
                break;
        }
    }

    class TabAdapter extends PagerAdapter {

        private Context context;

        public TabAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return false;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return context.getResources().getString(R.string.search_tab_title_status);
                case 1:
                    return context.getResources().getString(R.string.search_tab_title_user);
                default:
                    return context.getResources().getString(R.string.search_tab_title_status);
            }
        }
    }
}
