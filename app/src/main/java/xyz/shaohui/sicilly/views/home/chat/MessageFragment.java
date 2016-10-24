package xyz.shaohui.sicilly.views.home.chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.widget.MsgView;
import javax.inject.Inject;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.SPDataManager;
import xyz.shaohui.sicilly.event.HomeMessageEvent;
import xyz.shaohui.sicilly.event.MentionEvent;
import xyz.shaohui.sicilly.event.MentionUpdateEvent;
import xyz.shaohui.sicilly.event.MessageEvent;
import xyz.shaohui.sicilly.provider.BusProvider;
import xyz.shaohui.sicilly.views.home.timeline.HomeTimelineFragment;
import xyz.shaohui.sicilly.views.home.timeline.HomeTimelineFragmentBuilder;

public class MessageFragment extends Fragment {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    SegmentTabLayout tabLayout;

    @Inject
    EventBus mBus;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBus = BusProvider.getBus();
        mBus.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, v);
        initViewPager();
        return v;
    }

    private void initViewPager() {
        MessageAdapter mAdapter = new MessageAdapter(getChildFragmentManager());
        viewPager.setAdapter(mAdapter);

        tabLayout.setTabData(new String[] {
                getString(R.string.message_tab_2), getString(R.string.message_tab_1)
        });
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                    mBus.post(new MentionUpdateEvent());
                    tabLayout.hideMsg(0);
                    clearMentionCount();
                    if (!checkMessageCount()) {
                        mBus.post(new HomeMessageEvent(false));
                    }
                }
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                    int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setCurrentTab(position);
                if (position == 1 && checkMessageCount()) {
                    tabLayout.hideMsg(1);
                    clearMessageCount();
                    if (!checkMentionCount()) {
                        mBus.post(new HomeMessageEvent(false));
                    }
                } else if (position == 0 && checkMentionCount()) {
                    tabLayout.hideMsg(0);
                    clearMentionCount();
                    if (!checkMessageCount()) {
                        mBus.post(new HomeMessageEvent(false));
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * 监听消息提示
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void subscribeMessageTab(MessageEvent event) {
        tabLayout.showDot(1);
        MsgView view = tabLayout.getMsgView(1);
        view.setBackgroundColor(getResources().getColor(R.color.red));
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void subscribeMentionTab(MentionEvent event) {
        tabLayout.showDot(0);
        MsgView view = tabLayout.getMsgView(0);
        view.setBackgroundColor(getResources().getColor(R.color.red));
    }

    @Override
    public void onResume() {
        super.onResume();

        if (SPDataManager.getInt(SPDataManager.SP_KEY_MESSAGE, 0) > 0) {
            tabLayout.showDot(1);
            MsgView view = tabLayout.getMsgView(1);
            view.setBackgroundColor(getResources().getColor(R.color.red));
        }

        if (viewPager.getCurrentItem() == 1) {
            tabLayout.hideMsg(1);
            if (checkMessageCount()) {
                clearMessageCount();
                if (!checkMentionCount()) {
                    mBus.post(new HomeMessageEvent(false));
                }
            }
        } else if (viewPager.getCurrentItem() == 0) {
            tabLayout.hideMsg(0);
            if (checkMentionCount()) {
                clearMentionCount();
                if (!checkMessageCount()) mBus.post(new HomeMessageEvent(false));
            }
        }
    }

    @Override
    public void onDestroyView() {
        try {
            mBus.unregister(this);
        } catch (Exception e) {

        }
        super.onDestroyView();
    }

    private void clearMessageCount() {
        SPDataManager.setInt(SPDataManager.SP_KEY_MESSAGE, 0, true);
    }

    private void clearMentionCount() {
        SPDataManager.setInt(SPDataManager.SP_KEY_MENTION, 0, true);
    }

    private boolean checkMessageCount() {
        int count = SPDataManager.getInt(SPDataManager.SP_KEY_MESSAGE, 0);
        return count > 0;
    }

    private boolean checkMentionCount() {
        int count = SPDataManager.getInt(SPDataManager.SP_KEY_MENTION, 0);
        return count > 0;
    }

    class MessageAdapter extends FragmentPagerAdapter {

        public MessageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return HomeTimelineFragmentBuilder.newHomeTimelineFragment(
                        HomeTimelineFragment.TYPE_ABOUT_ME);
            } else {
                return new MessageListFragment();
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
