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
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.views.home.timeline.HomeTimelineFragment;
import xyz.shaohui.sicilly.views.home.timeline.HomeTimelineFragmentBuilder;

public class MessageFragment extends Fragment {

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.tab_layout)
    SegmentTabLayout tabLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
