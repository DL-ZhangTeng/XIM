package com.zhangteng.xim.common.tools;

import android.util.Log;

/**
 * Created by Lanxumit on 2017/1/17.
 */
public final class LogUtils {
    private static LogUtils instance;
    private boolean isShow = false; //是否打印日志

    private LogUtils() {
    }

    public static LogUtils getInstance() {
        if (instance == null) {
            instance = new LogUtils();
        }
        return instance;
    }

    private static boolean isNotEmpty(String str) {
        if (str == null || str.trim().equals("") || str.equalsIgnoreCase("null")) {
            return false;
        }
        return true;
    }

    public boolean isShow() {
        return isShow;
    }

//

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
    }

    public void logI(String key, String content) {
        if (isShow && isNotEmpty(key) && isNotEmpty(content)) {
            Log.i(key, content);
        }
    }

    public void logE(String key, String content) {
        if (isNotEmpty(content) && isShow && isNotEmpty(key)) {
            Log.e(key, content);
        }
    }
}
