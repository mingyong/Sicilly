package xyz.shaohui.sicilly.ui.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.BindInt;
import butterknife.ButterKnife;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyFactory;
import xyz.shaohui.sicilly.ui.adapters.IndexPagerAdapter;
import xyz.shaohui.sicilly.ui.fragments.StatusListFragment;

public class IndexActivity extends AppCompatActivity {

    @Bind(R.id.tool_bar)Toolbar toolBar;
    @Bind(R.id.tab_bar)TabLayout tabBar;
    @Bind(R.id.main_pager)ViewPager viewPager;

    private IndexPagerAdapter mAdatper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);

        // 初始化
        initToolbar();
        initViewPager();
    }

    private void initToolbar() {
        setSupportActionBar(toolBar);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setTitle("尚饭");
    }

    private void initViewPager() {
        mAdatper = new IndexPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(mAdatper);
        viewPager.setCurrentItem(0);
        tabBar.setupWithViewPager(viewPager);
        tabBar.setTabsFromPagerAdapter(mAdatper);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
