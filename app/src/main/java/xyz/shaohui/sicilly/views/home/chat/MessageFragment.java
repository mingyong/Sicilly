package xyz.shaohui.sicilly.views.home.chat;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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
import xyz.shaohui.sicilly.event.HomeMessageEvent;
import xyz.shaohui.sicilly.event.MentionEvent;
import xyz.shaohui.sicilly.event.MessageSumEvent;
import xyz.shaohui.sicilly.notification.NotificationUtils;
import xyz.shaohui.sicilly.provider.BusProvider;
import xyz.shaohui.sicilly.views.home.event.NoMessageEvent;
import xyz.shaohui.sicilly.views.home.timeline.HomeTimelineFragment;
import xyz.shaohui.sicilly.views.home.timeline.HomeTimelineFragmentBuilder;

public class MessageFragment extends Fragment {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    SegmentTabLayout tabLayout;

    @Inject
    EventBus mBus;

    // 检测Mention用
    private boolean hasMention;

    private boolean hasMessage;

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
                getString(R.string.message_tab_1), getString(R.string.message_tab_2)
        });
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

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
                if (position == 0) {
                    hasMessage = false;
                } else {
                    hasMention = false;
                    // 清除MentionNotification
                    NotificationUtils.clearMentionNoti(getContext());
                }
                tabLayout.hideMsg(position);
                if (!hasMessage && !hasMention) {
                    mBus.post(new HomeMessageEvent(-1));
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
    public void subscribeMessageTab(MessageSumEvent event) {
        if (event.count > 0) {
            tabLayout.showDot(0);
            MsgView view = tabLayout.getMsgView(0);
            view.setBackgroundColor(getResources().getColor(R.color.red));
            hasMessage = true;
        } else {
            tabLayout.hideMsg(0);
            hasMessage = false;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void subscribeMentionTab(MentionEvent event) {
        if (event.count > 0) {
            hasMention = true;
            tabLayout.showDot(1);
            MsgView view = tabLayout.getMsgView(1);
            view.setBackgroundColor(getResources().getColor(R.color.red));
            hasMention = true;
        } else {
            hasMention = false;
            tabLayout.hideMsg(1);
        }
    }

    class MessageAdapter extends FragmentPagerAdapter {

        public MessageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return new MessageListFragment();
            } else {
                return HomeTimelineFragmentBuilder.newHomeTimelineFragment(
                        HomeTimelineFragment.TYPE_ABOUT_ME);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
