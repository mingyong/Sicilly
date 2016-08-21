package xyz.shaohui.sicilly.views.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import xyz.shaohui.sicilly.R;

public class CreateActivity extends BaseActivity {

    @BindView(R.id.status_text)EditText editText;
    @BindView(R.id.status_image)ImageView image;
    @BindView(R.id.user_avatar)ImageView avatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_back)
    void btnBack() {
        finish();
    }

    @OnClick(R.id.action_camera)
    void selectImage() {

    }

    @OnClick(R.id.action_location)
    void fetchLocation() {

    }

    @OnClick(R.id.action_commit)
    void createStatus() {}
}
