package xyz.shaohui.sicilly.views.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gigamole.navigationtabbar.ntb.NavigationTabBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.views.fragments.MessageFragment;
import xyz.shaohui.sicilly.views.fragments.TimelineFragment;
import xyz.shaohui.sicilly.views.fragments.UserFragment;

public class IndexActivity extends BaseActivity {

    @BindView(R.id.bottom_tab)NavigationTabBar bottomTab;

    public static final int INDEX_HOME = 0;
    public static final int INDEX_MESSAGE = 1;
    public static final int INDEX_USER = 2;

    public static final String TAG_HOME = "home";
    public static final String TAG_MESSAGE = "message";
    public static final String TAG_USER = "user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);

        initBottomTab();
    }

    private void initBottomTab() {
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(new NavigationTabBar.Model.Builder(
                getResources().getDrawable(R.drawable.ic_home),
                getResources().getColor(R.color.bottom_1)
        ).title(getString(R.string.bottom_tab_home))
        .build());

        models.add(new NavigationTabBar.Model.Builder(
                getResources().getDrawable(R.drawable.ic_message),
                getResources().getColor(R.color.bottom_2)
        ).title(getString(R.string.bottom_tab_message))
                .build());

        models.add(new NavigationTabBar.Model.Builder(
                getResources().getDrawable(R.drawable.ic_user),
                getResources().getColor(R.color.bottom_3)
        ).title(getString(R.string.bottom_tab_user))
                .build());

        bottomTab.setModels(models);
        bottomTab.setOnTabBarSelectedIndexListener(new NavigationTabBar.OnTabBarSelectedIndexListener() {
            @Override
            public void onStartTabSelected(NavigationTabBar.Model model, int index) {
                switchFragment(index);
            }

            @Override
            public void onEndTabSelected(NavigationTabBar.Model model, int index) {

            }
        });
        bottomTab.onPageSelected(0);
    }

    // 切换Fragment
    private void switchFragment(int index) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        switch (index) {
            case INDEX_HOME:
                if (manager.findFragmentByTag(TAG_USER) != null) {
                    transaction.hide(manager.findFragmentByTag(TAG_USER));
                }

                if (manager.findFragmentByTag(TAG_MESSAGE) != null) {
                    transaction.hide(manager.findFragmentByTag(TAG_MESSAGE));
                }

                if (manager.findFragmentByTag(TAG_HOME) != null) {
                    transaction.show(manager.findFragmentByTag(TAG_HOME));
                } else {
                    transaction.add(R.id.main_frame,
                            TimelineFragment.newInstance(TimelineFragment.ACTION_INDEX), TAG_HOME);
                }
                break;
            case INDEX_MESSAGE:
                if (manager.findFragmentByTag(TAG_USER) != null) {
                    transaction.hide(manager.findFragmentByTag(TAG_USER));
                }

                if (manager.findFragmentByTag(TAG_HOME) != null) {
                    transaction.hide(manager.findFragmentByTag(TAG_HOME));
                }

                if (manager.findFragmentByTag(TAG_MESSAGE) != null) {
                    transaction.show(manager.findFragmentByTag(TAG_MESSAGE));
                } else {
                    transaction.add(R.id.main_frame,
                            new MessageFragment(), TAG_MESSAGE);
                }
                break;
            case INDEX_USER:
                if (manager.findFragmentByTag(TAG_HOME) != null) {
                    transaction.hide(manager.findFragmentByTag(TAG_HOME));
                }

                if (manager.findFragmentByTag(TAG_MESSAGE) != null) {
                    transaction.hide(manager.findFragmentByTag(TAG_MESSAGE));
                }

                if (manager.findFragmentByTag(TAG_USER) != null) {
                    transaction.show(manager.findFragmentByTag(TAG_USER));
                } else {
                    transaction.add(R.id.main_frame,
                            new UserFragment(), TAG_USER);
                }
                break;
        }
        transaction.commit();
    }


}
