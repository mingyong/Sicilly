package xyz.shaohui.sicilly.utils;

import android.content.Context;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import java.io.File;
import java.io.IOException;

/**
 * Created by PromeG on 2014/7/15.
 */
public final class CacheManager {

    protected static Context mContext = null;

    public static void setContext(Context context) {
        mContext = context;
    }

    private static final long MAX_SIZE = 10485760L; // 10MB

    public static final String IMG_CACHE_NAME = "images";

    private static final String TEMP_CACHE_NAME = "temp";

    private static SimpleDiskCache mSimpleDiskCache;

    @StringDef({IMG_CACHE_NAME, TEMP_CACHE_NAME})
    public @interface CacheDir {

    }

    private CacheManager() {
        //no instance
    }

    /**
     * create temp file in cache dir.
     *
     * @return null when fail
     */
    public static File createTempFileInCache(String prefix, String suffix)
            throws IOException {
        File cacheDir = getDiskCacheDir(TEMP_CACHE_NAME);

        if (!cacheDir.exists()) {
            cacheDir.mkdirs();
        }

        long size = getDirSize(cacheDir);
        if (size > MAX_SIZE) {
            cleanDir(cacheDir);
        }

        return File.createTempFile(prefix, suffix, cacheDir);
    }

    /**
     * get cache dir
     */
    @NonNull
    private static File getDiskCacheDir(@CacheDir String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            // there's an SDCard available, return external files dir
            cachePath = mContext.getExternalCacheDir().getPath();
        } else {
            // don't have an SDCard, return cache dir
            cachePath = mContext.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    @Nullable
    public static SimpleDiskCache open(@CacheDir String uniqueName) {
        if (mSimpleDiskCache == null) {
            synchronized (CacheManager.class) {
                if (mSimpleDiskCache == null) {
                    File cacheDir = getDiskCacheDir(uniqueName);
                    if (!cacheDir.exists()) {
                        cacheDir.mkdirs();
                    }
                    try {
                        mSimpleDiskCache = SimpleDiskCache.open(cacheDir, 1, MAX_SIZE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return mSimpleDiskCache;
    }

    private static void cleanDir(File dir) {

        long bytesDeleted = 0;
        File[] files = dir.listFiles();

        for (File file : files) {
            bytesDeleted += file.length();
            file.delete();
        }
    }

    private static long getDirSize(File dir) {

        long size = 0;
        if (dir != null && dir.exists()) {
            File[] files = dir.listFiles();

            for (File file : files) {
                if (file.isFile()) {
                    size += file.length();
                }
            }
        }

        return size;
    }
}
