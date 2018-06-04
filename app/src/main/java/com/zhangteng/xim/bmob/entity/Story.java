package com.zhangteng.xim.bmob.entity;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobGeoPoint;

/**
 * 一条动态
 * Created by swing on 2018/5/22.
 */
public class Story extends BmobObject {
    private User user;
    private List<String> iconPaths;
    private BmobGeoPoint geoPoint;
    private String content;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<String> getIconPaths() {
        return iconPaths;
    }

    public void setIconPaths(List<String> iconPaths) {
        this.iconPaths = iconPaths;
    }

    public BmobGeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(BmobGeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
