package com.zhangteng.xim;

import android.app.Application;
import android.content.Context;
import android.os.Environment;

import com.zhangteng.xim.bmob.DemoMessageHandler;
import com.zhangteng.xim.db.DBManager;
import com.zhangteng.xim.utils.AppManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;

import static com.zhangteng.xim.bmob.config.Config.APPLICATIONID;

/**
 * Created by swing on 2018/2/9.
 */

public class MyApplication extends Application {
    private static MyApplication instance;
    private static Context globalContext;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        globalContext = this;
        DBManager.init(this);

        //提供以下两种方式进行初始化操作：

        //第一：默认初始化
//        Bmob.initialize(this, APPLICATIONID);
        // 注:自v3.5.2开始，数据sdk内部缝合了统计sdk，开发者无需额外集成，传渠道参数即可，不传默认没开启数据统计功能
        Bmob.initialize(this, APPLICATIONID, "bmob");

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        //BmobConfig config =new BmobConfig.Builder(this)
        ////设置appkey
        //.setApplicationId("Your Application ID")
        ////请求超时时间（单位为秒）：默认15s
        //.setConnectTimeout(30)
        ////文件分片上传时每片的大小（单位字节），默认512*1024
        //.setUploadBlockSize(1024*1024)
        ////文件的过期时间(单位为秒)：默认1800s
        //.setFileExpiration(2500)
        //.build();
        //Bmob.initialize(config);
        //TODO 集成：1.8、初始化IM SDK，并注册消息接收器
        if (getApplicationInfo().packageName.equals(getMyProcessName())) {
            BmobIM.init(this);
            BmobIM.registerDefaultMessageHandler(new DemoMessageHandler(this));
        }
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getGlobalContext() {
        return globalContext;
    }

    public void exit() {
        AppManager.AppExit(instance);
    }

    public File getDiskCacheDir(String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = instance.getExternalCacheDir().getPath();
        } else {
            cachePath = instance.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);

    }

    /**
     * 获取当前运行的进程名
     *
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
