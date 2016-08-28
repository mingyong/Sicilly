package xyz.shaohui.sicilly.views.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import me.shaohui.scrollablelayout.ScrollableHelper;
import me.shaohui.scrollablelayout.ScrollableLayout;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.utils.ErrorUtils;
import xyz.shaohui.sicilly.utils.HtmlUtils;
import xyz.shaohui.sicilly.views.fragments.PhotoListFragment;
import xyz.shaohui.sicilly.views.fragments.TimelineFragment;

public class UserActivity extends BaseActivity {

    @BindView(R.id.count_follow)TextView countFollow;
    @BindView(R.id.count_follower)TextView countFollower;
    @BindView(R.id.count_status)TextView countStatus;
    @BindView(R.id.user_name)TextView name;
    @BindView(R.id.user_avatar)CircleImageView avatar;
    @BindView(R.id.user_location)TextView location;
    @BindView(R.id.user_brief)TextView brief;
    @BindView(R.id.user_bg)ImageView userBackground;

    @BindView(R.id.title_bar)RelativeLayout titleBar;
    @BindView(R.id.title)TextView title;
    @BindView(R.id.btn_follow)TextView actionFollow;
    @BindView(R.id.tab_layout)SegmentTabLayout tabLayout;
    @BindView(R.id.view_pager)ViewPager viewPager;
    @BindView(R.id.scrollableLayout)ScrollableLayout scrollableLayout;

    private User user;
    private String userId;
    private List<Fragment> fragmentList;

    public static Intent newIntent(Context context, String userId) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra("user_id", userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        if (getIntent().getData() != null) {
            userId = HtmlUtils.cleanUserScheme(getIntent().getData());
        } else {
            userId = getIntent().getStringExtra("user_id");
        }
        ButterKnife.bind(this);
        initViewPager();
        initScrollableLayout();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        fetchUserInfo();
    }

    private void initViewPager() {
        fragmentList = new ArrayList<>();
        fragmentList.add(TimelineFragment.newInstance(TimelineFragment.ACTION_USER, userId));
        fragmentList.add(PhotoListFragment.newInstance(userId));

        UserPagerAdapter adapter = new UserPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                scrollableLayout.getHelper().setScrollableContainer((ScrollableHelper.ScrollableContainer) fragmentList.get(position));
                tabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setTabData(new String[]{getString(R.string.user_tab_1),
                getString(R.string.user_tab_2)});
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    private void initScrollableLayout() {
        scrollableLayout.getHelper().setScrollableContainer((ScrollableHelper.ScrollableContainer) fragmentList.get(0));
        scrollableLayout.setOnScrollListener(new ScrollableLayout.OnScrollListener() {
            @Override
            public void onScroll(int currentY, int maxY) {
                if (currentY >= maxY) {
                    titleBar.setBackgroundColor(getResources().getColor(R.color.positive));
                } else {
                    titleBar.setBackgroundColor(getResources().getColor(R.color.transparent));
                }
            }
        });
    }

    private void fetchUserInfo() {
        SicillyApplication.getRetrofitService()
                .getUserService().userInfoOther(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User resultUser) {
                        user = resultUser;
                        placeUserInfo(user);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        ErrorUtils.catchException(throwable);
                    }
                });
    }

    private void placeUserInfo(User user) {
        countFollow.setText(String.valueOf(user.getFriends_count()));
        countFollower.setText(String.valueOf(user.getFollowers_count()));
        countStatus.setText(String.valueOf(user.getStatuses_count()));

        name.setText(user.getScreen_name());
        if (!TextUtils.isEmpty(user.getLocation())) {
            location.setText(user.getLocation());
        }
        brief.setText(user.getDescription());
        Glide.with(this)
                .load(user.getProfile_image_url_large())
                .into(avatar);
        Glide.with(this)
                .load(user.getProfile_background_image_url())
                .into(userBackground);

    }

    @OnClick(R.id.btn_back)
    void btnBack() {
        finish();
    }

    @OnClick(R.id.btn_follow)
    void actionFollow() {

    }

    @OnClick(R.id.btn_chat)
    void newChat() {

    }

    class UserPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentList;

        public UserPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return 2;
        }

    }

}
