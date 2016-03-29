package xyz.shaohui.sicilly.ui.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
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

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import uk.co.senab.photoview.PhotoViewAttacher;
import xyz.shaohui.sicilly.R;
import xyz.shaohui.sicilly.utils.MyToast;
import xyz.shaohui.sicilly.utils.imageUtils.ImageUtils;

public class PhotoActivity extends AppCompatActivity {

    @Bind(R.id.main_img)ImageView mainImg;

    private String url;
    private PhotoViewAttacher mAttacher;

    public static Intent newIntent(Context context, String url) {
        Intent intent = new Intent(context, PhotoActivity.class);
        intent.putExtra("url", url);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);

        url = getIntent().getStringExtra("url");
    }

    @Override
    protected void onStart() {
        super.onStart();
        showImg();
    }

    private void showImg() {
        Picasso.with(this)
                .load(Uri.parse(url))
                .into(mainImg, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (mAttacher != null) {
                            mAttacher.update();
                        } else {
                            mAttacher = new PhotoViewAttacher(mainImg);
                            mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
                                @Override
                                public void onPhotoTap(View view, float v, float v1) {
                                    finish();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError() {

                    }
                });

    }

    @OnClick(R.id.main)
    void clickImg() {
        finish();
    }

    @OnClick(R.id.btn_save)
    void saveImg() {
        ImageUtils.saveImg(url, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
