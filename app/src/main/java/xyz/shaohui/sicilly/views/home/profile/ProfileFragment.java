package xyz.shaohui.sicilly.views.home.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseFragment;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.views.feedback.FeedbackActivity;
import xyz.shaohui.sicilly.views.home.di.HomeComponent;
import xyz.shaohui.sicilly.views.home.profile.mvp.ProfilePresenter;
import xyz.shaohui.sicilly.views.home.profile.mvp.ProfileView;
import xyz.shaohui.sicilly.views.login.LoginDialogFragment;
import xyz.shaohui.sicilly.views.login.SwitchAccountDialog;

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

    @Inject
    EventBus mBus;

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
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.fetchUserInfo();
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

    @OnClick(R.id.action_status_list)
    void showStatusList() {

    }

    @OnClick(R.id.action_star_list)
    void showStarList() {

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

    }

    @OnClick(R.id.btn_switch)
    void switchAccount() {
        //new LoginDialogFragment().show(getFragmentManager(), "login");
        new SwitchAccountDialog().show(getFragmentManager(), "dialog");
    }
}
