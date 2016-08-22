package xyz.shaohui.sicilly.views.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import xyz.shaohui.sicilly.R;


public class MessageFragment extends BaseFragment {

    @BindView(R.id.view_pager)ViewPager viewPager;

    private MessageAdapter mAdapter;

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
        mAdapter = new MessageAdapter(getChildFragmentManager());
        viewPager.setAdapter(mAdapter);
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
                return TimelineFragment.newInstance(TimelineFragment.ACTION_USER);
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }

}
