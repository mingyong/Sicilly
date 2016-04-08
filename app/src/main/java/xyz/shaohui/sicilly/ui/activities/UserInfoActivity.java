package xyz.shaohui.sicilly.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.BindInt;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.data.services.user.UserService;
import xyz.shaohui.sicilly.presenters.UserInfoPresenter;
import xyz.shaohui.sicilly.ui.adapters.IndexPagerAdapter;
import xyz.shaohui.sicilly.ui.adapters.StatusListAdapter;
import xyz.shaohui.sicilly.ui.adapters.UserInfoViewPagerAdapter;
import xyz.shaohui.sicilly.utils.MyToast;
import xyz.shaohui.sicilly.utils.imageUtils.CircleTransform;

public class UserInfoActivity extends AppCompatActivity {

    @Bind(R.id.tool_bar)Toolbar toolBar;
    @Bind(R.id.main_pager)ViewPager viewPager;
    @Bind(R.id.tab_bar)TabLayout tabBar;

    @Bind(R.id.user_name)TextView userName;
    @Bind(R.id.user_gender)ImageView userGender;
    @Bind(R.id.user_brief)TextView userBrief;
    @Bind(R.id.user_img)ImageView userImg;
    @Bind(R.id.user_followed)TextView userFollowed;
    @Bind(R.id.user_follower)TextView userFollower;
    @Bind(R.id.user_status)TextView userStatus;
    @Bind(R.id.user_bg_image)ImageView bgImage;
    @Bind(R.id.action_follow)ImageView follow;

    private UserInfoPresenter presenter;
    private UserInfoViewPagerAdapter mAdapter;

    private User user;

    private ProgressDialog mDialog;

    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, UserInfoActivity.class);
        intent.putExtra("id", id);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        String id = getIntent().getStringExtra("id");
        presenter = new UserInfoPresenter(this, id);

        initToolbar();
        initDialog();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        presenter.fetchBaseInfo();
    }

    private void initToolbar() {
        setSupportActionBar(toolBar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initViewPager(boolean isSecret) {
        mAdapter = new UserInfoViewPagerAdapter(this, getSupportFragmentManager(), user, isSecret);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(1);
        tabBar.setupWithViewPager(viewPager);
//        tabBar.setTabsFromPagerAdapter(mAdapter);
    }

    private void initDialog() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage(getResources().getString(R.string.user_loading));
        mDialog.setCancelable(false);
        mDialog.setProgress(ProgressDialog.STYLE_SPINNER);
    }

    public void showDilog() {
        if (!mDialog.isShowing()) {
            mDialog.show();
        }
    }

    public void dismissDialog() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    public void setUpInfo(User user) {
        this.user = user;

        // 成功返回后 加载viewpager
        if (!user.isFollowing() && user.isSecreted()) {
            initViewPager(true);
        } else {
            initViewPager(false);
        }

        userName.setText(user.getNickName());
        userBrief.setText(user.getDescription());

        Picasso.with(this)
                .load(Uri.parse(user.getProfileImageLargeUrl()))
                .transform(new CircleTransform())
                .into(userImg);

        Picasso.with(this)
                .load(Uri.parse(user.getBgImg()))
                .into(bgImage);
        int followText = user.isFollowing()? R.drawable.ic_followed_white : R.drawable.ic_follow_white;
        follow.setImageResource(followText);

        // 设置性别信息
        if (user.getGender().equals("男")) {
            userGender.setImageResource(R.drawable.ic_gender_male);
        } else if (user.getGender().equals("女")) {
            userGender.setImageResource(R.drawable.ic_gender_female);
        }

        userStatus.setText(user.getStatusesCount() + "");
        userFollower.setText(user.getFollowersCount() + "");
        userFollowed.setText(user.getFriendCount() + "");

    }

    @OnClick(R.id.action_message)
    void sendMessage() {
        startActivity(ChatActivity.newIntent(this, user.getId(), user.getName()));
    }

    @OnClick(R.id.action_follow)
    void updateFollow() {
        if (user.isFollowing()) {
            destroyFollow();
        } else {
            UserService.createFollow(user.getId(), new UserService.CallBack() {
                @Override
                public void success() {
                    MyToast.iconSuccess(getApplicationContext(), "关注成功");
                    follow.setImageResource(R.drawable.ic_followed_white);
                    user.setFollowing(true);
                }

                @Override
                public void failure() {
                    MyToast.showToast(getApplicationContext(), "操作失败,请重试");
                }
            });
        }
    }

    @OnClick({R.id.user_brief, R.id.user_brief_expand})
    void expandUserBrief() {
        expandBrief();
    }

    @OnClick(R.id.user_img)
    void showAvatar() {
        startActivity(PhotoActivity.newIntent(this,
                user.getProfileImageUrl(), user.getProfileImageLargeUrl()));
    }

    private void expandBrief() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_MessageDialog);
        TextView textView = new TextView(this);
        textView.setText(userBrief.getText());
        textView.setTextSize(12);
        textView.setPadding(20,20,20,20);
        textView.setTextColor(getResources().getColor(R.color.white));
        builder.setView(textView);
        builder.create().show();
    }

    private void destroyFollow() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppTheme_AlertDialog);
        builder.setMessage("确定取消关注?")
                .setPositiveButton("取消关注", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UserService.destroyFollow(user.getId(), new UserService.CallBack() {
                            @Override
                            public void success() {
                                MyToast.showToast(getApplicationContext(), "已取消关注");
                                follow.setImageResource(R.drawable.ic_follow_white);
                                user.setFollowing(false);
                            }

                            @Override
                            public void failure() {
                                MyToast.showToast(getApplicationContext(), "操作失败,请重试");
                            }
                        });
                    }
                })
                .setNegativeButton("暂不", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            MyToast.showToast(this, "setting");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
