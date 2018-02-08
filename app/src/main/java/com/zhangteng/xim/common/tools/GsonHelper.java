package com.zhangteng.xim.common.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GsonHelper {

    public static final String GSON_DATA = "data";

    public static <T> ArrayList<T> getObjectListFromStrOld(String jsonStr, Class<T> cls) {
        ArrayList<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            list = gson.fromJson(jsonStr, new TypeToken<List<T>>() {
            }.getType());
        } catch (Exception e) {
        }
        return list;
    }


    public static <T> T getObjectByKey(JSONObject obj, String key, Class<T> cls) {
        T t = null;
        try {
            if (!GsonHelper.isEmpty(key) && obj.isNull(key)) {
                Gson gson = new Gson();
                t = gson.fromJson(obj.getString(key), cls);
            }
        } catch (Exception e) {
            LogUtils.getInstance().logI("exception", e.getMessage());
        }
        return t;
    }

    public static <T> T getObjectFormStr(String jsonStr, Class<T> cls) {
        T t = null;
        try {
            Gson gson = new Gson();
            t = gson.fromJson(jsonStr, cls);
        } catch (Exception e) {
            LogUtils.getInstance().logI("exception", e.getMessage());
        }
        return t;
    }


    public static <T> ArrayList<T> getObjectListFromStr1(String jsonStr, Class<T> cls) {
        ArrayList<T> list = new ArrayList<T>();
        try {
            Gson gson = new Gson();
            Type type = new TypeToken<List<T>>() {
            }.getType();
            list = gson.fromJson(jsonStr, type);
        } catch (Exception e) {
        }
        return list;
    }


    public static <T> ArrayList<T> getObjectListFromStr(String jsonStr, Class<T> cls) {
        ArrayList<T> list = new ArrayList<T>();
        JsonParser parser = new JsonParser();
        JsonElement el = parser.parse(jsonStr);
        JsonObject jsonObj = null;
        JsonArray jsonArray = null;
        if (el.isJsonArray()) {
            jsonArray = el.getAsJsonArray();
        }
        Gson gson = new Gson();
        Iterator it = jsonArray.iterator();
        T item = null;
        while (it.hasNext()) {
            JsonElement e = (JsonElement) it.next();
            item = gson.fromJson(e, cls);
            list.add(item);
        }
        return list;
    }


    public static Map<String, String> parseData(String data) {
        GsonBuilder gb = new GsonBuilder();
        Gson g = gb.create();
        Map<String, String> map = g.fromJson(data, new TypeToken<Map<String, String>>() {
        }.getType());
        return map;
    }

    public static <T> String Object2Json(T t) {
        Gson gson = new Gson();
        String rs = gson.toJson(t);
        return rs;
    }

    public static <T> String ObjectArray2Json(ArrayList<T> list) {
        Gson gson = new Gson();
        String rs = gson.toJson(list);
        return rs;
    }


    public static String fromJsonResult(String json, String key) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            if (jsonObject.has(key)) {
                json = jsonObject.getString(key);
                return json;
            } else {
                return null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String format(String jsonStr) {
        int level = 0;
        StringBuffer jsonForMatStr = new StringBuffer();
        for (int i = 0; i < jsonStr.length(); i++) {
            char c = jsonStr.charAt(i);
            if (level > 0 && '\n' == jsonForMatStr.charAt(jsonForMatStr.length() - 1)) {
                jsonForMatStr.append(getLevelStr(level));
            }
            switch (c) {
                case '{':
                case '[':
                    jsonForMatStr.append(c + "\n");
                    level++;
                    break;
                case ',':
                    jsonForMatStr.append(c + "\n");
                    break;
                case '}':
                case ']':
                    jsonForMatStr.append("\n");
                    level--;
                    jsonForMatStr.append(getLevelStr(level));
                    jsonForMatStr.append(c);
                    break;
                default:
                    jsonForMatStr.append(c);
                    break;
            }
        }

        return jsonForMatStr.toString();

    }

    private static String getLevelStr(int level) {
        StringBuffer levelStr = new StringBuffer();
        for (int levelI = 0; levelI < level; levelI++) {
            levelStr.append("\t");
        }
        return levelStr.toString();
    }

    private static boolean isEmpty(String str) {
        if (str == null || str.trim().equals("") || str.equalsIgnoreCase("null")) {
            return true;
        }
        return false;
    }
}
