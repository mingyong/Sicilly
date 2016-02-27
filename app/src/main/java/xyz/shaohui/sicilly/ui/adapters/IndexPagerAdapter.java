package xyz.shaohui.sicilly.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.ui.fragments.StatusListFragment;

/**
 * Created by kpt on 16/2/24.
 */
public class IndexPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public IndexPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return StatusListFragment.newInstance(StatusListFragment.DATA_HOME);
            case 1:
                return StatusListFragment.newInstance(StatusListFragment.DATA_ABOUT_ME);
            case 2:
                return StatusListFragment.newInstance(StatusListFragment.DATA_HOME);
            default:
                return StatusListFragment.newInstance(StatusListFragment.DATA_HOME);
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.page_home);
            case 1:
                return context.getString(R.string.page_about_me);
            case 2:
                return context.getString(R.string.page_public);
        }
        return null;
    }

}
