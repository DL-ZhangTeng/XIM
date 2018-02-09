package com.zhangteng.xim.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.jakewharton.disklrucache.DiskLruCache;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 本地缓存管理器
 *
 * @author Alex
 */
public final class DiskLruCacheUtils {
    private static DiskLruCache mCache;

    /**
     * 打开DiskLruCache。
     */
    public static void openCache(Context context) {
        try {
            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                    || !Environment.isExternalStorageRemovable()) {
                mCache = DiskLruCache.open(context.getExternalCacheDir(), 0, 1, 10 * 1024 * 1024);
            } else {
                mCache = DiskLruCache.open(context.getCacheDir(), 0, 1, 10 * 1024 * 1024);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加value到cache
     *
     * @param key
     * @param value
     */
    public static void put(String key, String value) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
            InputStream in = new ByteArrayInputStream(value.getBytes());
            try {
                //添加缓存
                dump(in, key);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取缓存
     *
     * @param key
     * @return
     */
    public static String get(String key) {
        if (!TextUtils.isEmpty(key)) {
            StringBuilder out = new StringBuilder();
            try {
                InputStream in = load(key);
                if (in != null) {
                    byte[] buffer = new byte[4096];
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        out.append(new String(buffer, 0, len));
                    }
                    in.close();
                    return out.toString();
                }
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

    /**
     * 写出缓存。
     */
    private static void dump(InputStream inputStream, String keyCache)
            throws IOException {
        if (mCache == null)
            throw new IllegalStateException("Must call openCache() first!");

        DiskLruCache.Editor mEditor = mCache.edit(keyCache);

        OutputStream outputStream = mEditor.newOutputStream(0);

        BufferedInputStream bin = new BufferedInputStream(inputStream);
        BufferedOutputStream bout = new BufferedOutputStream(outputStream);

        byte[] buf = new byte[1024];
        int len;
        while ((len = bin.read(buf)) != -1) {
            bout.write(buf, 0, len);
        }

        bout.close();
        outputStream.close();

        mEditor.commit();
    }

    /**
     * 读取缓存。
     */
    private static InputStream load(String keyCache) throws IOException {
        if (mCache == null)
            throw new IllegalStateException("Must call openCache() first!");

        DiskLruCache.Snapshot snapshot = mCache.get(keyCache);

        if (snapshot == null)
            return null;
        else
            return snapshot.getInputStream(0);
    }

    /**
     * 同步日志。
     */
    public static void syncLog() {
        try {
            mCache.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
