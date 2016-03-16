package xyz.shaohui.sicilly.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.widget.SearchView;

import xyz.shaohui.sicilly.R;

/**
 * Created by kpt on 16/3/16.
 */
public class SearchViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;

    public SearchViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getResources().getString(R.string.search_tab_title_status);
            case 1:
                return context.getResources().getString(R.string.search_tab_title_user);
            default:
                return context.getResources().getString(R.string.search_tab_title_status);
        }
    }
}
