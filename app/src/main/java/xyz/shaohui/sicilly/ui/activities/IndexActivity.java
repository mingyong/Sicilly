package xyz.shaohui.sicilly.ui.activities;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.BindInt;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyFactory;
import xyz.shaohui.sicilly.data.services.user.UserService;
import xyz.shaohui.sicilly.ui.adapters.IndexPagerAdapter;
import xyz.shaohui.sicilly.ui.fragments.StatusListFragment;
import xyz.shaohui.sicilly.utils.MyToast;

public class IndexActivity extends AppCompatActivity{

    @Bind(R.id.tool_bar)Toolbar toolBar;
    @Bind(R.id.tab_bar)TabLayout tabBar;
    @Bind(R.id.main_pager)ViewPager viewPager;
    @Bind(R.id.status_text)EditText mainEdit;

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

    @OnClick(R.id.create_status)
    void createStatus() {
        UserService.createStatus(mainEdit.getText().toString(), new UserService.CallBack() {
            @Override
            public void success() {
                mainEdit.setText("");
                MyToast.showToast(getApplicationContext(), "发送成功");
            }

            @Override
            public void failure() {
                MyToast.showToast(getApplicationContext(), "发送失败, 请重试");
            }
        });
    }

    @OnClick(R.id.open)
    void openActivity() {
        Intent intent;
        if (mainEdit.getTag() != null) {
            intent = CreateStatusActivity.newIntent(this, CreateStatusActivity.TYPE_REPLY,
                    mainEdit.getTag().toString(), mainEdit.getText().toString());
        } else {
            intent = CreateStatusActivity.newIntent(this, CreateStatusActivity.TYPE_NULL, null, null);
        }
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_index, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                return true;
            case R.id.message:
                startActivity(new Intent(this, MessageActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
