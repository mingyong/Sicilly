package xyz.shaohui.sicilly.views.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.SicillyApplication;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.utils.ErrorUtils;

public class UserFragment extends BaseFragment {

    @BindView(R.id.count_follow)TextView countFollow;
    @BindView(R.id.count_follower)TextView countFollower;
    @BindView(R.id.count_status)TextView countStatus;
    @BindView(R.id.user_name)TextView name;
    @BindView(R.id.user_avatar)CircleImageView avatar;
    @BindView(R.id.user_location)TextView location;
    @BindView(R.id.user_brief)TextView brief;
    @BindView(R.id.user_bg)ImageView userBackground;

    private User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_user, container, false);
        ButterKnife.bind(this, v);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fetchUserInfo();
    }

    private void fetchUserInfo() {
        SicillyApplication.getRetrofitService()
                .getUserService().userInfoSelf()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<User>() {
                    @Override
                    public void call(User user) {
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
        Glide.with(getActivity())
                .load(user.getProfile_image_url_large())
                .into(avatar);
        Glide.with(getActivity())
                .load(user.getProfile_background_image_url())
                .into(userBackground);

    }

    @OnClick(R.id.action_status_list)
    void showStatusList() {

    }

    @OnClick(R.id.action_star_list)
    void showStarList() {

    }

    @OnClick(R.id.action_feedback)
    void feedback() {

    }

    @OnClick(R.id.action_toolbox)
    void showToolbox() {

    }

    @OnClick(R.id.btn_setting)
    void setting() {

    }

    @OnClick(R.id.btn_switch)
    void swtichAccount() {

    }
}
