package xyz.shaohui.sicilly.views.search;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.views.home.timeline.HomeTimelineFragment;
import xyz.shaohui.sicilly.views.search.timeline.SearchTimelineFragment;
import xyz.shaohui.sicilly.views.search.user.SearchUserFragment;
import xyz.shaohui.sicilly.views.timeline.TimelineFragment;

/**
 * Created by shaohui on 2016/10/31.
 */

public class SearchAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public SearchAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new SearchTimelineFragment();
        } else {
            return new SearchUserFragment();
        }
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.search_tab_timeline);
        } else {
            return mContext.getString(R.string.search_tab_user);
        }
    }
}
