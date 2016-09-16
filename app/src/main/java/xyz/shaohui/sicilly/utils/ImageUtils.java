package xyz.shaohui.sicilly.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import xyz.shaohui.sicilly.SicillyFactory;

/**
 * Created by shaohui on 16/9/16.
 */

public class ImageUtils {


    public static File compressImage(File imageFile) throws Exception {
        return compressImage(imageFile, SicillyFactory.UPLOAD_IMG_MAX_HEIGHT,
                SicillyFactory.UPLOAD_IMG_MAX_WIDTH);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static File compressImage(File imageFile, int height, int width) throws Exception {
        if (imageFile == null || !imageFile.exists()) {
            return null;
        }

        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap bmp = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);

        float actualHeight = options.outHeight;
        float actualWidth = options.outWidth;
        float maxHeight = height;
        float maxWidth = width;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

        options.inSampleSize = calculateInSampleSize(options, (int) actualWidth,
                (int) actualHeight);
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inDither = false;
        options.inPurgeable = true;
        options.inInputShareable = true;
        //CHECKSTYLE:OFF

        options.inTempStorage = new byte[16 * 1024];

        try {
            bmp = BitmapFactory.decodeFile(imageFile.getAbsolutePath(), options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        scaledBitmap = transformRotation(imageFile.getAbsolutePath(), bmp);

        try {
            File tmpFile = CacheManager.createTempFileInCache("compressImageFile", ".jpg");
            if (tmpFile == null) {
                if (scaledBitmap != null && !scaledBitmap.isRecycled()) {
                    scaledBitmap.recycle();
                    scaledBitmap = null;
                }
                return null;
            }

            FileOutputStream out = new FileOutputStream(tmpFile);
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            if (scaledBitmap != null && !scaledBitmap.isRecycled()) {
                scaledBitmap.recycle();
                scaledBitmap = null;
            }

            return tmpFile;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static int getRotationForImage(String path) {
        int rotation = 0;

        try {
            ExifInterface exif = new ExifInterface(path);
            rotation = (int) exifOrientationToDegrees(
                    exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                            ExifInterface.ORIENTATION_NORMAL));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rotation;
    }


    private static Bitmap transformRotation(String filePath, Bitmap source) {
        int exifRotation = getRotationForImage(filePath);
        if (exifRotation != 0) {
            Matrix matrix = new Matrix();
            matrix.preRotate(exifRotation);

            Bitmap rotated =
                    Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix,
                            true);
            if (rotated != source) {
                source.recycle();
            }
            return rotated;
        }

        return source;
    }

    private static float exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }

}
