package xyz.shaohui.sicilly.ui.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.ui.fragments.StatusListFragment;
import xyz.shaohui.sicilly.ui.fragments.UserInfoFragment;
import xyz.shaohui.sicilly.ui.fragments.UserPhotoFragment;
import xyz.shaohui.sicilly.ui.fragments.UserSecretFragment;

/**
 * Created by kpt on 16/3/16.
 */
public class UserInfoViewPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private User user;
    private boolean isSecret;

    public UserInfoViewPagerAdapter(Context context, FragmentManager fm, User user, boolean isSecret) {
        super(fm);
        this.context = context;
        this.user = user;
        this.isSecret = isSecret;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return UserInfoFragment.newInstance(user);
            case 1:
                if (isSecret) {
                    return UserSecretFragment.newInstance();
                }
                return StatusListFragment.newInstanceForUser(user.getId());
            case 2:
                if (isSecret) {
                    return UserSecretFragment.newInstance();
                }
                return UserPhotoFragment.newInstance(user.getId());
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return context.getString(R.string.user_info_pager_info);
            case 1:
                return context.getString(R.string.user_info_pager_status);
            case 2:
                return context.getString(R.string.user_info_pager_photo);
        }
        return null;
    }
}
