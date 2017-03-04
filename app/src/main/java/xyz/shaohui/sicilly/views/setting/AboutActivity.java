package xyz.shaohui.sicilly.views.setting;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.shaohui.sicillylib.utils.ToastUtils;
import xyz.shaohui.sicilly.BuildConfig;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.views.user_info.UserActivity;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.text_version)
    TextView mTextVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);

        mTextVersion.setText(getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME);
    }


    @OnClick(R.id.btn_back)
    void btnBack() {
        finish();
    }

    @OnClick(R.id.frame_contact)
    void frameContact() {
        startActivity(UserActivity.newIntent(this, "huivista"));
    }

    @OnClick(R.id.frame_open_source)
    void frameOpenSource() {
        ToastUtils.showToast(this, "待完善");
    }

    @OnClick(R.id.frame_github)
    void frameGitHub() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(getString(R.string.about_github_site)));
        startActivity(intent);
    }
}
