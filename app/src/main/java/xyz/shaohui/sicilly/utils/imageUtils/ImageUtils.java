package xyz.shaohui.sicilly.utils.imageUtils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.format.Formatter;
import android.util.Log;
import android.util.Size;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import xyz.shaohui.sicilly.utils.MyToast;

/**
 * Created by kpt on 16/3/29.
 */
public class ImageUtils {

    public static void saveImg(final String url, final Context context) {
        Observable.create(new Observable.OnSubscribe<Bitmap>() {
            @Override
            public void call(Subscriber<? super Bitmap> subscriber) {
                try {
                    Bitmap bitmap = Picasso.with(context).load(url).get();
                    subscriber.onNext(bitmap);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map(new Func1<Bitmap, String>() {
                    @Override
                    public String call(Bitmap bitmap) {
                        try {
                            File appFile = new File(Environment.getExternalStorageDirectory(), "fan");
                            if (!appFile.exists()) {
                                appFile.mkdir();
                            }
                            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                            final String fileName = "SF" + timeStamp + ".jpg";
                            File file = new File(appFile, fileName);

                            FileOutputStream fileOutputStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                            fileOutputStream.flush();
                            fileOutputStream.close();

                            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

                            // 通知图库更新
                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file//" + file.getAbsolutePath()));
                            context.sendBroadcast(intent);
                            return file.getAbsolutePath();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        if (s != null) {
                            MyToast.showToast(context, "图片成功保存在:" + s);
                        } else {
                            MyToast.showToast(context, "保存失败");
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        MyToast.showToast(context, "保存失败");
                        throwable.printStackTrace();
                    }
                });
    }

    public static byte[] comp(Uri uri) {
        Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());

        double size = bitmap.getByteCount() / 1024/1024.0;
        double fold = 1;
        if (size > 2) {
            fold = size / 2;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, (int) Math.floor(100/fold), outputStream);
        return outputStream.toByteArray();
    }

    public static Bitmap compSize(String path) {
        final double SIZE = 512.0;
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        newOpts.inJustDecodeBounds = true;

        Bitmap bitmap = BitmapFactory.decodeFile(path, newOpts);
        newOpts.inJustDecodeBounds = false;

        // 将高度 宽度限制在一定范围之内
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;

        if ( w < SIZE && h < SIZE) {
            return BitmapFactory.decodeFile(path, newOpts);
        }

        double fold = 1;
        if (w > h) {
            fold = w / SIZE;
        } else {
            fold = h / SIZE;
        }

        newOpts.inSampleSize = (int) Math.ceil(fold);
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;

        bitmap = BitmapFactory.decodeFile(path, newOpts);
        Log.i("TAG_bitmap_size", bitmap.getByteCount() /1024/1024.0 + "");
        return bitmap;
    }






}
