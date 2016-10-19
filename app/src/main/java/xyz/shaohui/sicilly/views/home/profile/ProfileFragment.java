package xyz.shaohui.sicilly.views.home.profile;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import de.hdodenhof.circleimageview.CircleImageView;
import javax.inject.Inject;
import me.shaohui.sicillylib.utils.ToastUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import rx.android.schedulers.AndroidSchedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.data.database.FeedbackDbAccessor;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.event.FriendRequestEvent;
import xyz.shaohui.sicilly.views.feedback.FeedbackActivity;
import xyz.shaohui.sicilly.views.friend_list.FriendListActivity;
import xyz.shaohui.sicilly.views.friend_request.FriendRequestActivity;
import xyz.shaohui.sicilly.views.friendship.FriendshipActivity;
import xyz.shaohui.sicilly.views.home.di.HomeComponent;
import xyz.shaohui.sicilly.views.home.profile.mvp.ProfilePresenter;
import xyz.shaohui.sicilly.views.home.profile.mvp.ProfileView;
import xyz.shaohui.sicilly.views.login.SwitchAccountDialog;
import xyz.shaohui.sicilly.views.setting.SettingActivity;
import xyz.shaohui.sicilly.views.timeline.TimelineActivity;

public class ProfileFragment extends BaseFragment<ProfileView, ProfilePresenter>
        implements ProfileView {

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

    @BindView(R.id.feedback_count)
    TextView feedbackCount;

    @BindView(R.id.friend_request_count)
    TextView friendRequestCount;

    @Inject
    EventBus mBus;

    @Inject
    FeedbackDbAccessor mFeedbackDbAccessor;

    @NonNull
    @Override
    public EventBus getBus() {
        return mBus;
    }

    @Override
    public void injectDependencies() {
        HomeComponent component = getComponent(HomeComponent.class);
        component.inject(this);
        presenter = component.profilePresenter();
    }

    @Override
    public void bindViews(View view) {
        presenter.fetchUserInfo();

        mFeedbackDbAccessor.unreadCount()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> {
                    if (integer > 0) {
                        feedbackCount.setText(String.valueOf(integer));
                        feedbackCount.setVisibility(View.VISIBLE);
                    } else {
                        feedbackCount.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public int layoutRes() {
        return R.layout.fragment_user;
    }

    @Override
    public void placeUserInfo(User user) {
        countFollow.setText(String.valueOf(user.friends_count()));
        countFollower.setText(String.valueOf(user.followers_count()));
        countStatus.setText(String.valueOf(user.statuses_count()));

        name.setText(user.screen_name());
        if (!TextUtils.isEmpty(user.location())) {
            location.setText(user.location());
        }
        brief.setText(user.description());
        Glide.with(getActivity()).load(user.profile_image_url_large()).into(avatar);
        Glide.with(getActivity()).load(user.profile_background_image_url()).into(userBackground);
    }

    @Override
    public void loadFailure() {
        ToastUtils.showToast(getActivity(), R.string.load_user_failure);
    }

    @OnClick(R.id.user_profile_follower)
    void userProfileFollower() {
        startActivity(FriendListActivity.newIntent(getContext(), null,
                FriendListActivity.DATA_TYPE_FOLLOWER, FriendListActivity.VIEW_TYPE_FULL));
    }

    @OnClick(R.id.user_profile_friend)
    void userProfileFriend() {
        startActivity(FriendListActivity.newIntent(getContext(), null,
                FriendListActivity.DATA_TYPE_FRIEND, FriendListActivity.VIEW_TYPE_FULL));
    }

    @OnClick(R.id.action_status_list)
    void showStatusList() {
        startActivity(TimelineActivity.newIntent(getContext(), SicillyApplication.currentUId(),
                TimelineActivity.DATA_TYPE_TIMELINE));
    }

    @OnClick(R.id.action_star_list)
    void showStarList() {
        startActivity(TimelineActivity.newIntent(getContext(), SicillyApplication.currentUId(),
                TimelineActivity.DATA_TYPE_STAR));
    }

    @OnClick(R.id.action_request)
    void showFriendRequest() {
        startActivity(new Intent(getContext(), FriendshipActivity.class));
    }

    @OnClick(R.id.action_feedback)
    void feedback() {
        getContext().startActivity(new Intent(getContext(), FeedbackActivity.class));
    }

    @OnClick(R.id.action_toolbox)
    void showToolbox() {

    }

    @OnClick(R.id.btn_setting)
    void setting() {
        startActivity(new Intent(getContext(), SettingActivity.class));
    }

    @OnClick(R.id.btn_switch)
    void switchAccount() {
        //new LoginDialogFragment().show(getFragmentManager(), "login");
        new SwitchAccountDialog().show(getFragmentManager(), "dialog");
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void subscribeFriendRequest(FriendRequestEvent event) {
        if (event.getCount() > 0) {
            friendRequestCount.setText(String.valueOf(event.getCount()));
            friendRequestCount.setVisibility(View.VISIBLE);
        } else {
            friendRequestCount.setVisibility(View.GONE);
        }
    }
}
