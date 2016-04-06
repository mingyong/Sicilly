package xyz.shaohui.sicilly.data.cache;

import android.content.Context;
import android.os.Environment;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.File;
import java.io.IOException;

import xyz.shaohui.sicilly.SicillyApplication;

/**
 * Created by kpt on 16/4/6.
 */
public class DiskCache {

    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    public static DiskLruCache getJsonLruCache() {
        try {
            File cacheDir = getDiskCacheDir(SicillyApplication.getContext(), "json");
            if (!cacheDir.exists()) {
                cacheDir.mkdirs();
            }
            DiskLruCache mDiskLruCache = DiskLruCache.open(cacheDir, 1, 1, 10 * 1024 * 1024);
            return mDiskLruCache;

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void saveCache(String json, String mark) {
        DiskLruCache lruCache = getJsonLruCache();
        if (lruCache != null) {
            try {
                DiskLruCache.Editor editor = lruCache.edit(mark);
                editor.set(0, json);
                editor.commit();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String readCache(String mark) {
        DiskLruCache lruCache = getJsonLruCache();
        if (lruCache != null) {
            try {
                DiskLruCache.Snapshot snapshot = lruCache.get(mark);
                return snapshot.getString(0);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }



}
