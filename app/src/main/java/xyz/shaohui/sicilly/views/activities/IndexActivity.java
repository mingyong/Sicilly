package xyz.shaohui.sicilly.views.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.shaohui.sicillylib.utils.ToastUtils;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.views.fragments.IndexFragment;
import xyz.shaohui.sicilly.views.fragments.MessageFragment;
import xyz.shaohui.sicilly.views.fragments.TimelineFragment;
import xyz.shaohui.sicilly.views.fragments.UserFragment;

public class IndexActivity extends BaseActivity {

    @BindView(R.id.bottom_tab)CommonTabLayout bottomTab;

    private Fragment indexFragment = new IndexFragment();
    private Fragment messageFragment = new MessageFragment();
    private Fragment userFragment = new UserFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);
        bottomTab = (CommonTabLayout) findViewById(R.id.bottom_tab);

        initBottomTab(savedInstanceState);
    }

    private void initBottomTab(Bundle savedInstance) {
        ArrayList<CustomTabEntity> tabData = new ArrayList<>();
        tabData.add(new TabEntity(
                getString(R.string.bottom_tab_home),
                R.drawable.ic_home_selected,
                R.drawable.ic_home));
        tabData.add(new TabEntity(getString(R.string.bottom_tab_message),
                R.drawable.ic_message_selected,
                R.drawable.ic_message));
        tabData.add(new TabEntity(getString(R.string.bottom_tab_user),
                R.drawable.ic_user_selected,
                R.drawable.ic_user));

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(indexFragment);
        fragments.add(messageFragment);
        fragments.add(userFragment);

        bottomTab.setTabData(tabData, this, R.id.main_frame, fragments);
    }

    class TabEntity implements CustomTabEntity {

        private String title;
        private int selected;
        private int unSelected;

        public TabEntity(String title, int selected, int unSelected) {
            this.title = title;
            this.selected = selected;
            this.unSelected = unSelected;
        }

        @Override
        public String getTabTitle() {
            return title;
        }

        @Override
        public int getTabSelectedIcon() {
            return selected;
        }

        @Override
        public int getTabUnselectedIcon() {
            return unSelected;
        }
    }


}
