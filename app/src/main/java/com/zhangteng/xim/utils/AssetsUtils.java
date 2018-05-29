package com.zhangteng.xim.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import com.zhangteng.xim.MyApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by swing on 2018/5/29.
 */
public class AssetsUtils {
    //数据库名称
    public static String dbName = "CityNo.db";
    //数据库存放的文件夹 /data/data/com.zhangteng.xim/databases 下面
    private static String pathStr = "data/data/" + MyApplication.getGlobalContext().getPackageName() + "/databases";
    //数据库存储路径
    private static String filePath = pathStr + "/" + dbName;

    /**
     * 获取assets中的json字符串
     *
     * @param fileName 文件名
     * @param context
     */
    public static String getJson(String fileName, Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }

    public static boolean isExistCityNoDb() {
        File jhPath = new File(filePath);
        if (jhPath.exists()) {
            Log.i("CityNo", "存在数据库");
            return true;
        }
        return false;
    }

    /**
     * 初始化assets中的db文件
     *
     * @param fileName assets中的文件名
     * @param context
     */
    public static boolean initDatabase(String fileName, Context context) {
        System.out.println("filePath:" + filePath);
        File jhPath = new File(filePath);
        if (jhPath.exists()) {
            Log.i("CityNo", "存在数据库");
            return true;
        } else {
            File path = new File(pathStr);
            Log.i("CityNo", "pathStr=" + path);
            if (path.mkdir()) {
                Log.i("CityNo", "创建成功");
            } else {
                Log.i("CityNo", "创建失败");
            }
            try {
                AssetManager am = context.getAssets();
                InputStream is = am.open(fileName);
                Log.i("CityNo", is + "");
                FileOutputStream fos = new FileOutputStream(jhPath);
                Log.i("CityNo", "fos=" + fos);
                Log.i("CityNo", "jhPath=" + jhPath);
                byte[] buffer = new byte[1024];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    Log.i("CityNo", "得到");
                    fos.write(buffer, 0, count);
                }
                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }
}
