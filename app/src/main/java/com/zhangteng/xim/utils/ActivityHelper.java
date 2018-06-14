package com.zhangteng.xim.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.zhangteng.xim.R;


/**
 * Created by swing on 2017/11/4.
 */

public class ActivityHelper {


    public static void jumpToActivity(Activity activity, Class cls, int code) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        activity.startActivity(intent);
        anim(activity, code);
    }

    public static void jumpToActivityWithBundle(Activity activity, Class cls, Bundle bundle, int code) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        anim(activity, code);
    }

    public static void jumpActivityWithBundle(Activity activity, Class cls, Bundle bundle, int code) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        intent.putExtras(bundle);
        activity.startActivity(intent);
        anim(activity, code);
        AppManager.finishActivity(activity);
    }

    public static void jumpActivity(Activity activity, Class cls, int code) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        activity.startActivity(intent);
        anim(activity, code);
        AppManager.finishActivity(activity);
    }

    public static void jumpToActivityForParams(Activity activity, Class cls, String key, String value, int code) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        intent.putExtra(key, value);
        activity.startActivity(intent);
        anim(activity, code);
    }

    public static void jumpActivityForParams(Activity activity, Class cls, String key, String value, int code) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        intent.putExtra(key, value);
        activity.startActivity(intent);
        anim(activity, code);
        AppManager.finishActivity(activity);
    }

    public static void jumpActivityResult(Activity activity, Class cls, int requestCode, int code) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        activity.startActivityForResult(intent, requestCode);
        anim(activity, code);
        AppManager.finishActivity(activity);
    }

    public static void jumpToActivityResult(Activity activity, Class cls, int requestCode, int code) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        activity.startActivityForResult(intent, requestCode);
        anim(activity, code);
    }


    private static void anim(Activity activity, int code) {
        if (code == 1) {
            ActivityHelper.setActivityAnimShow(activity);
        } else if (code == 2) {
            ActivityHelper.setActivityAnimClose(activity);
        } else if (code == 0) {
            activity.overridePendingTransition(0, 0);
        } else if (code == 3) {
            activity.overridePendingTransition(R.anim.activity_down, R.anim.activity_down_1);
        }
    }


    //跳转进activity时的动画
    public static void setActivityAnimShow(Activity activity) {
        activity.overridePendingTransition(R.anim.activity_show, R.anim.activity_show_1);
    }

    //退出activity时的动画
    public static void setActivityAnimClose(Activity activity) {
        activity.overridePendingTransition(R.anim.activity_close_1, R.anim.activity_close);
    }
}

