package xyz.shaohui.sicilly.utils;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.webkit.MimeTypeMap;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.Comparator;
import java.util.regex.Matcher;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.functions.Func1;
/**
 * Created by PromeG on 2014/6/22.
 */
//CHECKSTYLE:OFF
public final class FileUtils {

    protected static Context mContext = null;

    public static void setContext(Context context) {
        mContext = context;
    }

    /** TAG for log messages. */
    static final String TAG = "FileUtils";

    private static final boolean DEBUG = false; // Set to true to enable logging

    public static final String MIME_TYPE_AUDIO = "audio/*";

    public static final String MIME_TYPE_TEXT = "text/*";

    public static final String MIME_TYPE_IMAGE = "image/*";

    public static final String MIME_TYPE_VIDEO = "video/*";

    public static final String MIME_TYPE_APP = "application/*";

    public static final String HIDDEN_PREFIX = ".";

    private FileUtils() {
        //no instance
    }

    public static String saveImage(Context context, File srcFile) throws IOException {
        File dir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "尚饭");
        dir.mkdirs();
        File dest = new File(dir, System.currentTimeMillis() + getImageExtension(srcFile));
        org.apache.commons.io.FileUtils.copyFile(srcFile, dest);
        MediaScannerConnection.scanFile(context, new String[]{dest.getAbsolutePath()}, null, null);
        return dest.getAbsolutePath();
    }

    private static String getImageExtension(File file) {
        int index = file.getName().lastIndexOf(".");
        if (index > 0) {
            return file.getName().substring(index);
        } else {
            return ".jpg";
        }
    }

    /**
     *  待删除代码
     *
     */

    public static Uri res2Uri(int resId) {
        return Uri.parse("res://" + mContext.getPackageName() + "/" + resId);
    }

    public static Uri path2Uri(String path) {
        return Uri.parse("file://" + path);
    }

    //public static Func1<String, Observable<File>> flatmapUrl2File(OkHttpClient client,
    //        String savePath) {
    //    return url1 -> {
    //        Request request = new Request.Builder().url(url1).build();
    //        FileOutputStream outputStream = null;
    //        InputStream inputStream = null;
    //        try {
    //            File file = new File(savePath);
    //            Response response = client.newCall(request).execute();
    //            outputStream = new FileOutputStream(file);
    //            inputStream = response.body().byteStream();
    //            IOUtils.copy(inputStream, outputStream);
    //            return Observable.just(file);
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //            return Observable.just(null);
    //        } finally {
    //            IOUtils.closeQuietly(inputStream);
    //            IOUtils.closeQuietly(outputStream);
    //        }
    //    };
    //}

    //public static Func1<String, File> mapUrl2File(OkHttpClient client, String savePath) {
    //    return url1 -> {
    //        Request request = new Request.Builder().url(url1).build();
    //        FileOutputStream outputStream = null;
    //        InputStream inputStream = null;
    //        try {
    //            File file = new File(savePath);
    //            file.createNewFile();
    //
    //            Response response = client.newCall(request).execute();
    //            outputStream = new FileOutputStream(file);
    //            inputStream = response.body().byteStream();
    //            IOUtils.copy(inputStream, outputStream);
    //            return file;
    //        } catch (IOException e) {
    //            e.printStackTrace();
    //            return null;
    //        } finally {
    //            IOUtils.closeQuietly(inputStream);
    //            IOUtils.closeQuietly(outputStream);
    //        }
    //    };
    //}

    /**
     * Gets the extension of a file name, like ".png" or ".jpg".
     *
     * @return Extension including the dot("."); "" if there is no extension;
     * null if uri was null.
     */
    public static
    @NonNull
    String getExtension(String uri) {
        if (uri == null) {
            return "";
        }

        int dot = uri.lastIndexOf(".");
        if (dot >= 0) {
            return uri.substring(dot);
        } else {
            // No extension.
            return "";
        }
    }

    /**
     * @return Whether the URI is a local one.
     */
    public static boolean isLocal(String url) {
        if (url != null && !url.startsWith("http://") && !url.startsWith("https://")) {
            return true;
        }
        return false;
    }

    /**
     * @return True if Uri is a MediaStore Uri.
     * @author paulburke
     */
    public static boolean isMediaUri(Uri uri) {
        return "media".equalsIgnoreCase(uri.getAuthority());
    }

    /**
     * Convert File into Uri.
     *
     * @return uri
     */
    public static Uri getUri(File file) {
        if (file != null) {
            return Uri.fromFile(file);
        }
        return null;
    }

    /**
     * Returns the path only (without file name).
     */
    public static File getPathWithoutFilename(File file) {
        if (file != null) {
            if (file.isDirectory()) {
                // no file to be split off. Return everything
                return file;
            } else {
                String filename = file.getName();
                String filepath = file.getAbsolutePath();

                // Construct path without file name.
                String pathwithoutname =
                        filepath.substring(0, filepath.length() - filename.length());
                if (pathwithoutname.endsWith("/")) {
                    pathwithoutname = pathwithoutname.substring(0, pathwithoutname.length() - 1);
                }
                return new File(pathwithoutname);
            }
        }
        return null;
    }

    /**
     * @return The MIME type for the given file.
     */
    public static String getMimeType(File file) {
        if (file != null) {
            String extension = getExtension(file.getName());
            // 06 bug fix: can't find Upper case getMimeTypeFromExtension, return null, to
            // lowercase manually
            // NOTE2 is upper case, N5 is lower case
            extension = extension.toLowerCase();

            if (extension.length() > 0) {
                //            String tmp1 = MimeTypeMap.getSingleton()
                //                    .getMimeTypeFromExtension(extension.substring(1));
                //            String tmp2 = MimeTypeMap.getSingleton().getMimeTypeFromExtension
                // ("jpg");
                //            String tmp3 = MimeTypeMap.getSingleton().getMimeTypeFromExtension
                // ("jpeg");
                //            String tmp4 = MimeTypeMap.getSingleton().getMimeTypeFromExtension
                // ("gif");
                //            Timber.d("FOR TEST MimeTypeMap.getSingleton()
                // .getMimeTypeFromExtension(\"JPG\"):"
                //                    + MimeTypeMap
                //                    .getSingleton().getMimeTypeFromExtension("JPG"));

                return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));
            }
        }

        return "application/octet-stream";
    }

    public static String getMimeType(String filename) {

        String extension = getExtension(filename);
        extension = extension.toLowerCase();

        if (extension.length() > 0) {
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));
        }

        return "application/octet-stream";
    }

    /**
     * check gif file, only check .gif suffix
     */
    public static boolean isGif(File file) {
        if (file != null && file.getAbsolutePath().endsWith(".gif")) {
            return true;
        }
        return false;
    }

    /**
     * check url for emoji
     */
    public static boolean isEmojicon(String url) {
        if (!TextUtils.isEmpty(url) && url.startsWith("http://emotion.xueba.mobi/")) {
            return true;
        }
        return false;
    }

    /**
     * @return The MIME type for the give Uri.
     */
    public static String getMimeType(Context context, Uri uri) {
        File file = new File(getPath(context, uri));
        return getMimeType(file);
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     * @author paulburke
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     * @author paulburke
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     * @author paulburke
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     * @author paulburke
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
            String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver()
                    .query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                if (DEBUG) {
                    DatabaseUtils.dumpCursor(cursor);
                }

                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.<br>
     * <br>
     * Callers should check whether the path is local before assuming it
     * represents a local file.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     * @see #isLocal(String)
     * @see #getFile(Context, Uri)
     */
    public static String getPath(final Context context, final Uri uri) {

        if (DEBUG) {
            Log.d(TAG + " File -", "Authority: " + uri.getAuthority() +
                    ", Fragment: " + uri.getFragment() +
                    ", Port: " + uri.getPort() +
                    ", Query: " + uri.getQuery() +
                    ", Scheme: " + uri.getScheme() +
                    ", Host: " + uri.getHost() +
                    ", Segments: " + uri.getPathSegments().toString());
        }

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {

            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.parseLong(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Convert Uri into File, if possible.
     *
     * @return file A local file that the Uri was pointing to, or null if the
     * Uri is unsupported or pointed to a remote resource.
     * @author paulburke
     * @see #getPath(Context, Uri)
     */
    public static File getFile(Context context, Uri uri) {
        if (uri != null) {
            String path = getPath(context, uri);
            if (path != null && isLocal(path)) {
                return new File(path);
            }
        }
        return null;
    }

    /**
     * Get the file size in a human-readable string.
     *
     * @author paulburke
     */
    public static String getReadableFileSize(int size) {
        final int BYTES_IN_KILOBYTES = 1024;
        final DecimalFormat dec = new DecimalFormat("###.#");
        final String KILOBYTES = " KB";
        final String MEGABYTES = " MB";
        final String GIGABYTES = " GB";
        float fileSize = 0;
        String suffix = KILOBYTES;

        if (size > BYTES_IN_KILOBYTES) {
            fileSize = size / BYTES_IN_KILOBYTES;
            if (fileSize > BYTES_IN_KILOBYTES) {
                fileSize = fileSize / BYTES_IN_KILOBYTES;
                if (fileSize > BYTES_IN_KILOBYTES) {
                    fileSize = fileSize / BYTES_IN_KILOBYTES;
                    suffix = GIGABYTES;
                } else {
                    suffix = MEGABYTES;
                }
            }
        }
        return String.valueOf(dec.format(fileSize) + suffix);
    }

    /**
     * Attempt to retrieve the thumbnail of given File from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @author paulburke
     */
    public static
    @Nullable
    Bitmap getThumbnail(Context context, File file) {
        if (getUri(file) == null) {
            return null;
        }
        return getThumbnail(context, getUri(file), getMimeType(file));
    }

    /**
     * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @author paulburke
     */
    public static Bitmap getThumbnail(Context context, Uri uri) {
        return getThumbnail(context, uri, getMimeType(context, uri));
    }

    /**
     * Attempt to retrieve the thumbnail of given Uri from the MediaStore. This
     * should not be called on the UI thread.
     *
     * @author paulburke
     */
    public static Bitmap getThumbnail(Context context, Uri uri, String mimeType) {
        if (DEBUG) {
            Log.d(TAG, "Attempting to get thumbnail");
        }

        if (!isMediaUri(uri)) {
            Log.e(TAG, "You can only retrieve thumbnails for images and videos.");
            return null;
        }

        Bitmap bm = null;
        if (uri != null) {
            final ContentResolver resolver = context.getContentResolver();
            Cursor cursor = null;
            try {
                cursor = resolver.query(uri, null, null, null, null);
                if (cursor != null && cursor.moveToFirst()) {
                    final int id = cursor.getInt(0);
                    if (DEBUG) {
                        Log.d(TAG, "Got thumb ID: " + id);
                    }

                    if (mimeType.contains("video")) {
                        bm = MediaStore.Video.Thumbnails.getThumbnail(resolver, id,
                                MediaStore.Video.Thumbnails.MINI_KIND, null);
                    } else if (mimeType.contains(FileUtils.MIME_TYPE_IMAGE)) {
                        bm = MediaStore.Images.Thumbnails.getThumbnail(resolver, id,
                                MediaStore.Images.Thumbnails.MINI_KIND, null);
                    }
                }
            } catch (Exception e) {
                if (DEBUG) {
                    Log.e(TAG, "getThumbnail", e);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return bm;
    }

    /**
     * File and folder comparator. TODO Expose sorting option method
     *
     * @author paulburke
     */
    public static Comparator<File> sComparator = new Comparator<File>() {
        @Override
        public int compare(File f1, File f2) {
            // Sort alphabetically by lower case, which is much cleaner
            return f1.getName().toLowerCase().compareTo(f2.getName().toLowerCase());
        }
    };

    /**
     * File (not directories) filter.
     *
     * @author paulburke
     */
    public static FileFilter sFileFilter = new FileFilter() {
        @Override
        public boolean accept(File file) {
            final String fileName = file.getName();
            // Return files only (not directories) and skip hidden files
            return file.isFile() && !fileName.startsWith(HIDDEN_PREFIX);
        }
    };

    /**
     * Folder (directories) filter.
     *
     * @author paulburke
     */
    public static FileFilter sDirFilter = new FileFilter() {
        @Override
        public boolean accept(File file) {
            final String fileName = file.getName();
            // Return directories only and skip hidden directories
            return file.isDirectory() && !fileName.startsWith(HIDDEN_PREFIX);
        }
    };

    /**
     * Get the Intent for selecting content to be used in an Intent Chooser.
     *
     * @return The intent for opening a file with Intent.createChooser()
     * @author paulburke
     */
    public static Intent createGetContentIntent() {
        // Implicitly allow the user to select a particular kind of data
        final Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        // The MIME data type filter
        intent.setType("*/*");
        // Only return URIs that can be opened with ContentResolver
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        return intent;
    }

    /**
     * url to file name
     *
     * return null if no '/' in url
     */
    public static String getFileNameFromUrl(String url) {
        if (TextUtils.isEmpty(url) || !url.contains("/")) {
            return null;
        }
        return url.substring(url.lastIndexOf('/') + 1, url.length());
    }

    /**
     * check imgFile for long image
     * criterion: img.height / img.width >= screen.height / screen.width
     */
    public static boolean isLongImg(Context context, File imgFile) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgFile.getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        //ensure height > width
        int screenHeight = displayMetrics.heightPixels > displayMetrics.widthPixels
                ? displayMetrics.heightPixels : displayMetrics.widthPixels;
        int screenWidth = displayMetrics.widthPixels < displayMetrics.heightPixels
                ? displayMetrics.widthPixels : displayMetrics.heightPixels;

        if (((float) imageHeight / (float) imageWidth) >= ((float) screenHeight
                / (float) screenWidth)) {
            return true;
        }
        return false;
    }

    /**
     * check imgFile for long image
     * criterion: img.height / img.width >= (screen.height / screen.width) * 1.5
     */
    public static boolean isLongImg(Context context, int imageWidth, int imageHeight) {

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        //ensure height > width
        int screenHeight = displayMetrics.heightPixels > displayMetrics.widthPixels
                ? displayMetrics.heightPixels : displayMetrics.widthPixels;
        int screenWidth = displayMetrics.widthPixels < displayMetrics.heightPixels
                ? displayMetrics.widthPixels : displayMetrics.heightPixels;

        if (((float) imageHeight / (float) imageWidth) >= (((float) screenHeight
                / (float) screenWidth) * 1.5)) {
            return true;
        }
        return false;
    }

    public static void copyFile(InputStream in, File destFile) throws IOException {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(in);
        FileOutputStream outputStream = new FileOutputStream(destFile);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = bufferedInputStream.read(buffer)) != -1) {
            bufferedOutputStream.write(buffer, 0, len);
        }
        closeSilently(bufferedInputStream);
        closeSilently(outputStream);
        closeSilently(bufferedOutputStream);
    }

    public static void copyStream(InputStream is, OutputStream os) throws IOException {
        final int buffer_size = 1024 * 8; // 8k
        byte[] bytes = new byte[buffer_size];
        long total = 0;
        int count;
        while ((count = is.read(bytes)) != -1) {
            total += count;
            os.write(bytes, 0, count);
        }
        os.flush();
        os.close();
        is.close();
    }

    public static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException ignored) {

            }
        }
    }

    /**
     * get image height and width from url
     * e.g. http://debug.img.xueba.mobi/15435f36d2b87c8-23196471__440x4170.jpeg
     * H: 440  W: 4170
     */
    //public static Integer[] getWidthHeightFromUrl(String url) {
    //    if (TextUtils.isEmpty(url)) {
    //        return null;
    //    }
    //    Integer[] result = null; // 0--width, 1--height
    //
    //    String filename = FileUtils.getFileNameFromUrl(url);
    //    if (TextUtils.isEmpty(filename)) {
    //        return null;
    //    }
    //    Matcher matcher = Patterns.IMAGE_DENISITY.matcher(filename);
    //    while (matcher.find() && matcher.groupCount() >= 2) {
    //        try {
    //            Integer width = Integer.parseInt(matcher.group(1));
    //            Integer height = Integer.parseInt(matcher.group(2));
    //            Timber.d("width: " + width + " height: " + height);
    //            result = new Integer[2];
    //            result[0] = width;
    //            result[1] = height;
    //            return result;
    //        } catch (java.lang.NumberFormatException e) {
    //            e.printStackTrace();
    //            continue;
    //        }
    //    }
    //    return result;
    //}
}
//CHECKSTYLE:ON
