package com.zhangteng.xim;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhangteng.xim.bmob.callback.BmobCallBack;
import com.zhangteng.xim.bmob.entity.Story;
import com.zhangteng.xim.bmob.http.DataApi;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.db.DBManager;
import com.zhangteng.xim.db.bean.CityNo;
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
}
