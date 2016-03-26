package xyz.shaohui.sicilly.ui.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.BindInt;
import butterknife.ButterKnife;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.data.models.Status;
import xyz.shaohui.sicilly.data.models.User;
import xyz.shaohui.sicilly.presenters.UserInfoPresenter;
import xyz.shaohui.sicilly.ui.adapters.IndexPagerAdapter;
import xyz.shaohui.sicilly.ui.adapters.StatusListAdapter;
import xyz.shaohui.sicilly.utils.MyToast;
import xyz.shaohui.sicilly.utils.imageUtils.CircleTransform;

public class UserInfoActivity extends AppCompatActivity {

    @Bind(R.id.tool_bar)Toolbar toolBar;
    @Bind(R.id.main_pager)ViewPager viewPager;
    @Bind(R.id.tab_bar)TabLayout tabBar;

    @Bind(R.id.user_name)TextView userName;
    @Bind(R.id.user_brief)TextView userBrief;
    @Bind(R.id.user_img)ImageView userImg;
    @Bind(R.id.user_followed)TextView userFollowed;
    @Bind(R.id.user_follower)TextView userFollower;
    @Bind(R.id.user_status)TextView userStatus;
    @Bind(R.id.user_bg_image)ImageView bgImage;

    private UserInfoPresenter presenter;
    private IndexPagerAdapter mAdapter;

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
        initViewPager();
        initDialog();
    }

    @Override
    protected void onStart() {
        super.onStart();
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

    private void initViewPager() {
        mAdapter = new IndexPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(mAdapter);
        viewPager.setCurrentItem(0);
        tabBar.setupWithViewPager(viewPager);
        tabBar.setTabsFromPagerAdapter(mAdapter);
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
        userName.setText(user.getNickName());
        userBrief.setText(user.getDescription());

        Picasso.with(this)
                .load(Uri.parse(user.getProfileImageLargeUrl()))
                .transform(new CircleTransform())
                .into(userImg);

        Picasso.with(this)
                .load(Uri.parse(user.getBgImg()))
                .into(bgImage);

        userStatus.setText(user.getStatusesCount() + "");
        userFollower.setText(user.getFollowersCount() + "");
        userFollowed.setText(user.getFriendCount() + "");

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
