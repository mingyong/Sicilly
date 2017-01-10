package xyz.shaohui.sicilly.views.photo;

import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.Target;
import java.io.File;
import java.io.IOException;
import me.shaohui.sicillylib.utils.ToastUtils;
import org.greenrobot.eventbus.EventBus;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoViewAttacher;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.base.BaseActivity;
import xyz.shaohui.sicilly.utils.FileUtils;

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
                ToastUtils.showToast(getApplicationContext(), "正在保存图片到本地...");
                saveLocalImage();
                return true;
            });
        }
    }

    private void saveLocalImage() {
        Observable.fromCallable(() -> Glide.with(this)
                .load(url)
                .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                .get())
                .map(srcFile -> {
                    //File dir = new File(Environment.getExternalStoragePublicDirectory(
                    //        Environment.DIRECTORY_PICTURES), "尚饭");
                    //dir.mkdirs();
                    //File result = new File(dir, System.currentTimeMillis() + ".jpg");
                    //try {
                    //    FileInputStream in = new FileInputStream(file1);
                    //    FileOutputStream out = new FileOutputStream(result);
                    //
                    //    byte[] buffer = new byte[1024];
                    //    while(in.read(buffer) > 0) {
                    //        out.write(buffer);
                    //    }
                    //    out.flush();
                    //    out.close();
                    //    in.close();
                    //    return result.getAbsolutePath();
                    //} catch (IOException e) {
                    //    e.printStackTrace();
                    //    return null;
                    //}
                    try {
                        return FileUtils.saveImage(this, srcFile);
                    } catch (IOException e) {
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(path -> {
                    if (!TextUtils.isEmpty(path)) {
                        ToastUtils.showToast(this, "图片已保存至：" + path);
                    } else {
                        ToastUtils.showToast(this, "图片保存失败， 请重试");
                    }
                }, throwable -> {
                    ToastUtils.showToast(this, "图片保存失败， 请重试");
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
    public void finishAfterTransition() {
        if (mAttacher != null) {
            mAttacher.cleanup();
            mAttacher = null;
        }
        super.finishAfterTransition();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
