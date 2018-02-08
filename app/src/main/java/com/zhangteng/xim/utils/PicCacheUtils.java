package com.zhangteng.xim.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.SystemClock;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.codec.binary.Base64;

public final class PicCacheUtils {
    private static final String TAG = "PicCacheUtils";
    private static final int PIC_SIZE = 1920 * 1080;
    private static final int ZOOM_PIC_SIZE = 80 * 80;
    private static PicCacheUtils instance = null;

    private PicCacheUtils() {
    }

    ;

    public static PicCacheUtils getInstance() {
        if (instance == null) {
            instance = new PicCacheUtils();
        }
        return instance;
    }

    /**
     * 获取图片保存路径
     *
     * @return
     */
    public static String getPicSavePath(Context context) {
        File cacheLocation = null;
        StringBuilder sb = new StringBuilder();
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            sb.append(Environment.getExternalStorageDirectory()).append("/Android-BitmapCache/jpg/");
            cacheLocation = new File(sb.substring(0));
        } else {
            sb.append(context.getFilesDir()).append("/Android-BitmapCache/jpg/");
            cacheLocation = new File(sb.substring(0));
        }
        if (!cacheLocation.exists()) {
            cacheLocation.mkdirs();
        }
        sb.delete(0, sb.length());
        String substring = sb.append(cacheLocation.getAbsolutePath()).append(SystemClock.currentThreadTimeMillis()).append(".jpg").substring(0);
        return substring;
    }

    /**
     * 图片编码base64
     *
     * @param path 图片路径
     * @return 编码后的base64字符串
     */
    public static String getBase64(String path) {
        String imgString = "";
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        try {
            InputStream in = new FileInputStream(path.replaceAll("file:///", ""));
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            Bitmap bitmap = BitmapUtils.makeBitmap(bos.toByteArray(), ZOOM_PIC_SIZE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imgString = Base64.encodeBase64(stream.toByteArray()).toString();
            in.close();
            stream.close();
            bitmap.recycle();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return imgString;
    }


    public static Bitmap getBitmapByPath(String path, int size) {
        Bitmap bitmap = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            InputStream in = new FileInputStream(path.replaceAll("file:///", ""));
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            bitmap = BitmapUtils.makeBitmap(bos.toByteArray(), size);
            in.close();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 通过文件名创建file
     *
     * @param path 图片路径
     * @return
     */
    public static File createFileByFileName(Context context, String path) {
        FileOutputStream stream;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            InputStream in = new FileInputStream(path.replaceAll("file:///", ""));
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = in.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }
            Bitmap bitmap = BitmapUtils.makeBitmap(bos.toByteArray(), PIC_SIZE);
            //TODO 添加图片路径
            String savePath = PicCacheUtils.getPicSavePath(context);
            stream = new FileOutputStream(savePath);
            //ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            in.close();
            stream.close();
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            bos.close();
            return new File(savePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String compressPicture(String srcPath, String desPath) {
        FileOutputStream fos = null;
        BitmapFactory.Options op = new BitmapFactory.Options();

        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        op.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, op);
        op.inJustDecodeBounds = false;

        // 缩放图片的尺寸
        float w = op.outWidth;
        float h = op.outHeight;
        float hh = 800f;//
        float ww = 480f;//
        // 最长宽度或高度1024
        float be = 1.0f;
        if (w > h && w > ww) {
            be = (float) (w / ww);
        } else if (w < h && h > hh) {
            be = (float) (h / hh);
        }
        if (be <= 0) {
            be = 1.0f;
        }
        op.inSampleSize = (int) be;// 设置缩放比例,这个数字越大,图片大小越小.
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, op);
        int desWidth = (int) (w / be);
        int desHeight = (int) (h / be);
        bitmap = Bitmap.createScaledBitmap(bitmap, desWidth, desHeight, true);
        try {
            fos = new FileOutputStream(desPath);
            if (bitmap != null) {
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return desPath;
    }
}
