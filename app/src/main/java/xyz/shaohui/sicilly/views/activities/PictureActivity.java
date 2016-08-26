package xyz.shaohui.sicilly.views.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.senab.photoview.PhotoViewAttacher;
import xyz.shaohui.sicilly.R;

public class PictureActivity extends BaseActivity {

    @BindView(R.id.image_view)ImageView imageView;

    private String url;
    private PhotoViewAttacher mAttacher;

    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra("url", url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture);
        url = getIntent().getStringExtra("url");
        ButterKnife.bind(this);
        loadImage();
    }

    private void loadImage() {
        Glide.with(this)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
        if (!url.toLowerCase().endsWith(".gif")) {
            mAttacher = new PhotoViewAttacher(imageView);
        }
    }

    @OnClick(R.id.image_view)
    void clickImageView() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            finishAfterTransition();
        } else {
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        if (mAttacher != null) {
            mAttacher.cleanup();
        }
        super.onDestroy();
    }
}
