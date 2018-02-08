package com.lanxum.bszh.teacher.common.tools;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lanxumit on 2017/12/15.
 */

public class SPFUtils {

    //存储app接口返回需保存的数据
    private static String PROJECT_CONTENT = "com.huike.interfacedata";
    //存储app配置数据
    private static String PROJECT_APP_CONTENT = "com.huike.appdata";

    public static SharedPreferences getSharePreferences(Context context) {
        if (context != null) {
            return context.getSharedPreferences(PROJECT_CONTENT, Context.MODE_PRIVATE);
        } else {
            return null;
        }
    }

    public static SharedPreferences getAppSharePreferences(Context context) {
        if (context != null) {
            return context.getSharedPreferences(PROJECT_APP_CONTENT, Context.MODE_PRIVATE);
        } else {
            return null;
        }
    }

    public static String getAppString(Context context, String key) {
        SharedPreferences sharedPreferences = getAppSharePreferences(context);
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        } else {
            return "";
        }
    }


    public static void setAppString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = getAppSharePreferences(context);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }


    public static void setString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = getSharePreferences(context);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(key, value);
            editor.apply();
        }
    }

    public static String getString(Context context, String key) {
        SharedPreferences sharedPreferences = getSharePreferences(context);
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, "");
        } else {
            return "";
        }
    }


    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = getSharePreferences(context);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(key, value);
            editor.apply();
        }
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = getSharePreferences(context);
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(key, false);
        } else {
            return false;
        }
    }


    public static void setInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = getSharePreferences(context);
        if (sharedPreferences != null) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key, value);
            editor.apply();
        }
    }

    public static int getInt(Context context, String key) {
        SharedPreferences sharedPreferences = getSharePreferences(context);
        if (sharedPreferences != null) {
            return sharedPreferences.getInt(key, 0);
        } else {
            return 0;
        }
    }


    public static void clearPreference(SharedPreferences p) {
        if (p != null) {
            SharedPreferences.Editor editor = p.edit();
            editor.clear();
            editor.apply();
        }
    }
}
