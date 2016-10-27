package xyz.shaohui.sicilly.views.photo;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.common.io.Files;
import java.io.File;
import java.io.IOException;
import me.shaohui.sicillylib.utils.ToastUtils;
import org.greenrobot.eventbus.EventBus;
import uk.co.senab.photoview.PhotoViewAttacher;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseActivity;

public class PictureActivity extends BaseActivity {

    @BindView(R.id.image_view)
    ImageView imageView;

    private String url;
    private PhotoViewAttacher mAttacher;

    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra("url", url);
        return intent;
    }

    public static Intent newIntent(Context context, String url, String text) {
        Intent intent = new Intent(context, PictureActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("text", text);
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

    @Override
    public void initializeInjector() {

    }

    @Override
    public EventBus getBus() {
        return null;
    }

    private void loadImage() {
        Glide.with(this)
                .load(url)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(imageView);
        if (!url.toLowerCase().endsWith(".gif")) {
            mAttacher = new PhotoViewAttacher(imageView);
            mAttacher.setScaleType(ImageView.ScaleType.FIT_CENTER);
            mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                @Override
                public void onPhotoTap(View view, float x, float y) {
                    clickImageView();
                }

                @Override
                public void onOutsidePhotoTap() {
                    clickImageView();
                }
            });
            mAttacher.setOnLongClickListener(v -> {
                ToastUtils.showToast(getApplicationContext(), "TODO保存图片");
                return true;
            });
        }
    }

    private void saveLocalImage() {
        File dir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "尚饭");
        dir.mkdirs();
        File file = new File(dir, System.currentTimeMillis() + ".jpg");
        Glide.with(this).load(url).downloadOnly(new SimpleTarget<File>() {
            @Override
            public void onResourceReady(File resource,
                    GlideAnimation<? super File> glideAnimation) {
                try {
                    Files.copy(resource, file);
                    ToastUtils.showToast(PictureActivity.this,
                            String.format(getString(R.string.save_image), file.getPath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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
    public void supportFinishAfterTransition() {
        super.supportFinishAfterTransition();
        if (mAttacher != null) {
            mAttacher.cleanup();
            mAttacher = null;
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
