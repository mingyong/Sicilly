package xyz.shaohui.sicilly.views.user_info;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TextAppearanceSpan;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import de.hdodenhof.circleimageview.CircleImageView;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import me.shaohui.scrollablelayout.ScrollableHelper;
import me.shaohui.scrollablelayout.ScrollableLayout;
import me.shaohui.sicillylib.utils.ToastUtils;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Retrofit;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.base.BaseMvpActivity;
import xyz.shaohui.sicilly.base.HasComponent;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.utils.HtmlUtils;
import xyz.shaohui.sicilly.views.chat.ChatActivity;
import xyz.shaohui.sicilly.views.create_status.CreateStatusActivity;
import xyz.shaohui.sicilly.views.create_status.CreateStatusDialogBuilder;
import xyz.shaohui.sicilly.views.create_status.DialogController;
import xyz.shaohui.sicilly.views.friend_list.FriendListActivity;
import xyz.shaohui.sicilly.views.timeline.TimelineActivity;
import xyz.shaohui.sicilly.views.user_info.di.DaggerUserInfoComponent;
import xyz.shaohui.sicilly.views.user_info.di.UserInfoComponent;
import xyz.shaohui.sicilly.views.user_info.di.UserInfoPresenterModule;
import xyz.shaohui.sicilly.views.user_info.mvp.UserInfoPresenter;
import xyz.shaohui.sicilly.views.user_info.mvp.UserInfoView;
import xyz.shaohui.sicilly.views.user_info.photo.UserPhotoFragmentBuilder;
import xyz.shaohui.sicilly.views.user_info.star.UserStarFragment;
import xyz.shaohui.sicilly.views.user_info.timeline.UserTimelineFragmentBuilder;

public class UserActivity extends BaseMvpActivity<UserInfoView, UserInfoPresenter>
        implements UserInfoView, HasComponent<UserInfoComponent>, DialogController {

    @BindView(R.id.count_follow)
    TextView countFollow;
    @BindView(R.id.count_follower)
    TextView countFollower;
    @BindView(R.id.count_status)
    TextView countStatus;
    @BindView(R.id.user_name)
    TextView name;
    @BindView(R.id.user_avatar)
    CircleImageView avatar;
    @BindView(R.id.user_location)
    TextView location;
    @BindView(R.id.user_brief)
    TextView brief;
    @BindView(R.id.user_bg)
    ImageView userBackground;

    @BindView(R.id.title_bar)
    RelativeLayout titleBar;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.btn_follow)
    ImageButton actionFollow;
    @BindView(R.id.btn_chat)
    ImageButton actionChat;
    @BindView(R.id.btn_at)
    ImageButton actionAt;
    @BindView(R.id.tab_layout)
    SegmentTabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.scrollableLayout)
    ScrollableLayout scrollableLayout;

    private User mUser;
    private String userId;
    private List<Fragment> fragmentList;
    private UserInfoComponent mComponent;

    @Inject
    EventBus mBus;

    @Inject
    Retrofit mRetrofit;

    public static Intent newIntent(Context context, String userId) {
        Intent intent = new Intent(context, UserActivity.class);
        intent.putExtra("user_id", userId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        ButterKnife.bind(this);
        fragmentList = new ArrayList<>();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("user_id", userId);
        super.onSaveInstanceState(outState);
    }

    @NonNull
    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public void injectDependencies() {
        if (getSavedInstanceState() == null) {
            if (getIntent().getData() != null) {
                userId = HtmlUtils.cleanUserScheme(getIntent().getData());
            } else {
                userId = getIntent().getStringExtra("user_id");
            }
        } else {
            userId = getSavedInstanceState().getString("user_id");
        }
        mComponent = DaggerUserInfoComponent.builder()
                .appComponent(getAppComponent())
                .userInfoPresenterModule(new UserInfoPresenterModule(userId))
                .build();
        mComponent.inject(this);
        presenter = mComponent.userInfoPresenter();
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        presenter.fetchUserInfo(userId);
    }

    private void initViewPager() {

        UserPagerAdapter adapter = new UserPagerAdapter(getSupportFragmentManager(), fragmentList);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                    int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                scrollableLayout.getHelper()
                        .setScrollableContainer(
                                (ScrollableHelper.ScrollableContainer) fragmentList.get(position));
                tabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setTabData(new String[] {
                getString(R.string.user_tab_1), getString(R.string.user_tab_2), getString(R.string.user_tab_3)
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
    }

    private void initScrollableLayout() {
        scrollableLayout.getHelper()
                .setScrollableContainer((ScrollableHelper.ScrollableContainer) fragmentList.get(0));
        scrollableLayout.setOnScrollListener((currentY, maxY) -> {
            int alpha = 0;
            float scale = currentY / (float) maxY;
            alpha = scale > 1 ? 255 : (int) (scale * 255);
            int color =
                    ColorUtils.setAlphaComponent(getResources().getColor(R.color.positive), alpha);
            titleBar.setBackgroundColor(color);
        });
    }

    @OnClick(R.id.user_profile_follower)
    void userProfileFollower() {
        startActivity(
                FriendListActivity.newIntent(this, userId, FriendListActivity.DATA_TYPE_FOLLOWER,
                        FriendListActivity.VIEW_TYPE_FULL));
    }

    @OnClick(R.id.user_profile_friend)
    void userProfileFriend() {
        startActivity(
                FriendListActivity.newIntent(this, userId, FriendListActivity.DATA_TYPE_FRIEND,
                        FriendListActivity.VIEW_TYPE_FULL));
    }

    @OnClick(R.id.user_profile_timeline)
    void userProfileTimeline() {
        startActivity(
                TimelineActivity.newIntent(this, userId, TimelineActivity.DATA_TYPE_TIMELINE));
    }

    @OnClick(R.id.btn_chat)
    void btnChat() {
        startActivity(ChatActivity.newIntent(this, mUser));
    }

    @OnClick(R.id.btn_at)
    void btnAt() {
        new CreateStatusDialogBuilder(CreateStatusActivity.TYPE_TEXT).originText(
                mUser.screen_name()).build().show(getSupportFragmentManager());
    }

    @Override
    public void placeUserInfo(User user, boolean isProtected) {
        mUser = user;
        countFollow.setText(String.valueOf(user.friends_count()));
        countFollower.setText(String.valueOf(user.followers_count()));
        countStatus.setText(String.valueOf(user.statuses_count()));

        name.setText(user.screen_name());
        if (!TextUtils.isEmpty(user.location())) {
            location.setText(user.location());
        }

        String description =
                user.description() != null ? user.description().replaceAll("\\n", " ") : "";
        SpannableString string = new SpannableString(
                (description.length() > 20 ? description.substring(0, 20) : description)
                        + getString(R.string.more_user_info));
        string.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.accent)),
                string.length() - 4, string.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        brief.setText(string);
        brief.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(user.description())) {
                new MaterialDialog.Builder(UserActivity.this)
                        .content(user.description())
                        .show();
            }
        });

        Glide.with(this).load(user.profile_image_url_large()).into(avatar);
        Glide.with(this).load(user.profile_background_image_url()).into(userBackground);

        if (SicillyApplication.isSelf(mUser.id())) {
            actionFollow.setVisibility(View.GONE);
            actionAt.setVisibility(View.GONE);
            actionChat.setVisibility(View.GONE);
        } else if (mUser.following()) {
            actionFollow.setImageResource(R.drawable.ic_followed);
        }

        // 针对设置隐私保护的user
        if (!mUser.following() && mUser.is_protected() && !SicillyApplication.isSelf(mUser.id())) {
            showPrivacyFragment();
        } else {
            showSimpleFragment();
        }
        initViewPager();
        initScrollableLayout();
    }

    private void showPrivacyFragment() {
        fragmentList.add(new PrivacyFragment());
        fragmentList.add(new PrivacyFragment());
    }

    private void showSimpleFragment() {
        fragmentList.add(UserTimelineFragmentBuilder.newUserTimelineFragment(userId));
        fragmentList.add(UserPhotoFragmentBuilder.newUserPhotoFragment(userId));
        fragmentList.add(new UserStarFragment());
    }

    @Override
    public void loadUserInfoFailure() {
        ToastUtils.showToast(this, R.string.load_user_failure);
        finish();
    }

    @Override
    public void opFollow() {
        // 用户没设置隐私, 直接显示关注成功
        if (!mUser.is_protected()) {
            mUser = mUser.updateFollow(true);
            actionFollow.setImageResource(R.drawable.ic_followed);
        }
    }

    @Override
    public void showUnFollowConfirmDialog() {
        new MaterialDialog.Builder(this).content(R.string.confirm_un_follow_message)
                .positiveText(R.string.yes)
                .negativeText(R.string.no)
                .onPositive((dialog, which) -> {
                    presenter.opUnFollow(mUser);
                    mUser = mUser.updateFollow(false);
                    actionFollow.setImageResource(R.drawable.ic_follow);
                })
                .show();
    }

    @Override
    public void followError() {
        ToastUtils.showToast(this, R.string.follow_error);
        mUser = mUser.updateFollow(false);
        actionFollow.setImageResource(R.drawable.ic_follow);
    }

    @Override
    public void requestSuccess() {
        ToastUtils.showToast(this, R.string.request_follow_success);
    }

    @Override
    public void unFollowError() {
        ToastUtils.showToast(this, R.string.follow_error);
        mUser = mUser.updateFollow(true);
        actionFollow.setImageResource(R.drawable.ic_followed);
    }

    @OnClick(R.id.btn_back)
    void btnBack() {
        finish();
    }

    @OnClick(R.id.btn_follow)
    void actionFollow() {
        if (mUser != null) {
            presenter.opFollowSelector(mUser);
        }
    }

    @Override
    public UserInfoComponent getComponent() {
        return mComponent;
    }

    @Override
    public Retrofit getRetrofit() {
        return mRetrofit;
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
            return 3;
        }
    }
}
