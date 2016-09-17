package xyz.shaohui.sicilly.views.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import java.util.ArrayList;
import org.greenrobot.eventbus.EventBus;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.base.HasComponent;
import xyz.shaohui.sicilly.views.fragments.MessageFragment;
import xyz.shaohui.sicilly.views.fragments.UserFragment;
import xyz.shaohui.sicilly.views.home.di.DaggerHomeComponent;
import xyz.shaohui.sicilly.views.home.di.HomeComponent;
import xyz.shaohui.sicilly.views.home.timeline.HomeTimelineFragment;

public class IndexActivity extends BaseActivity implements HasComponent<HomeComponent> {

    @BindView(R.id.bottom_tab)
    CommonTabLayout bottomTab;

    private Fragment indexFragment = new HomeTimelineFragment();
    private Fragment messageFragment = new MessageFragment();
    private Fragment userFragment = new UserFragment();

    private HomeComponent mComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        ButterKnife.bind(this);
        bottomTab = (CommonTabLayout) findViewById(R.id.bottom_tab);

        initBottomTab(savedInstanceState);
    }

    @Override
    public void initializeInjector() {
        mComponent = DaggerHomeComponent.builder().appComponent(getAppComponent()).build();
        mComponent.inject(this);
    }

    @Override
    public EventBus getBus() {
        return null;
    }

    private void initBottomTab(Bundle savedInstance) {
        ArrayList<CustomTabEntity> tabData = new ArrayList<>();
        tabData.add(new TabEntity(getString(R.string.bottom_tab_home), R.drawable.ic_home_selected,
                R.drawable.ic_home));
        tabData.add(new TabEntity(getString(R.string.bottom_tab_message),
                R.drawable.ic_message_selected, R.drawable.ic_message));
        tabData.add(new TabEntity(getString(R.string.bottom_tab_user), R.drawable.ic_user_selected,
                R.drawable.ic_user));

        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(indexFragment);
        fragments.add(messageFragment);
        fragments.add(userFragment);

        bottomTab.setTabData(tabData, this, R.id.main_frame, fragments);
    }

    @Override
    public HomeComponent getComponent() {
        return mComponent;
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
