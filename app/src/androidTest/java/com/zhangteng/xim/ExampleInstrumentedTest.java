package com.zhangteng.xim;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Toast;

import com.githang.androidcrash.reporter.mailreporter.CrashEmailReporter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.Story;
import com.zhangteng.xim.bmob.http.DataApi;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.db.DBManager;
import com.zhangteng.xim.db.bean.CityNo;
import com.zhangteng.xim.utils.AppManager;
import com.zhangteng.xim.utils.AssetsUtils;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.reflect.Type;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.zhangteng.xim", appContext.getPackageName());
    }

    @Test
    public void queryCityNo() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
//        DBManager.init(appContext);
        CityNo cityNo = DBManager.instance(AssetsUtils.dbName).queryCityNo("330100");
        assertEquals("com.zhangteng.xim", appContext.getPackageName());
    }

    String parent = "000000";

    /**
     * 生成CityNo.db
     */
    @Test
    public void createCityNoDb() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        DBManager.init(appContext);
        String json = AssetsUtils.getJson("cityno.json", appContext);
        Type listType = new TypeToken<List<CityNo>>() {
        }.getType();
        //这里的json是字符串类型 = jsonArray.toString();
        List<CityNo> list = new Gson().fromJson(json, listType);
        forCityNo(list);
    }

    public void forCityNo(List<CityNo> list) {
        for (CityNo cityNo : list) {
            cityNo.setParent(parent);
            DBManager.instance(AssetsUtils.dbName).insertCityNo(cityNo);
            if (cityNo.getRegionEntitys() != null && !cityNo.getRegionEntitys().isEmpty()) {
                parent = cityNo.getCode();
                forCityNo(cityNo.getRegionEntitys());
                parent = "000000";
            }
        }
    }

    @Test
    public void queryStory() throws Exception {
        final Story[] story = new Story[1];
        Context appContext = InstrumentationRegistry.getTargetContext();
        DataApi.getInstance().queryStorys(UserApi.getInstance().getUserInfo(), 0, 1, new BmobCallBack<List<Story>>(appContext, false) {
            @Override
            public void onSuccess(@Nullable List<Story> bmobObject) {
                story[0] = bmobObject.get(0);
            }
        });
        assertEquals("11", story[0].getContent());
    }

    @Test
    public void sendMail() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        CrashEmailReporter reporter = new CrashEmailReporter(appContext) {
            /**
             * 重写此方法，可以弹出自定义的崩溃提示对话框，而不使用系统的崩溃处理。
             * @param thread
             * @param ex
             */
            @Override
            public void closeApp(Thread thread, Throwable ex) {
                final Activity activity = AppManager.currentActivity();
                if (activity == null) {
                    return;
                }
                Toast.makeText(activity, "发生异常，正在退出", Toast.LENGTH_SHORT).show();
                // 自定义弹出对话框
                new AlertDialog.Builder(activity).
                        setMessage("程序发生异常，现在退出").
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AppManager.AppExit(activity);
                            }
                        }).create().show();
            }
        };
        reporter.setReceiver("763263311@qq.com");
        reporter.setSender("zhangteng0633@163.com");
        reporter.setSendPassword("zhangteng0633");
        reporter.setSMTPHost("smtp.163.com");
        reporter.setPort("465");//994/465/25
        reporter.sendFile(null);
    }
}
