package com.zhangteng.xim.bmob.entity;

import com.zhangteng.xim.db.bean.LocalUser;

import cn.bmob.v3.BmobUser;

/**
 * Created by swing on 2018/2/11.
 */

public class User extends BmobUser {
    private int sex;
    private int age;
    private int schoolId;
    private int roleId;
    private String realName;
    private String icoPath;
    private int gradeId;
    private int cityId;
    private int areaId;
    private int provinceId;
    private int classId;

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(int schoolId) {
        this.schoolId = schoolId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getIcoPath() {
        return icoPath;
    }

    public void setIcoPath(String icoPath) {
        this.icoPath = icoPath;
    }

    public int getGradeId() {
        return gradeId;
    }

    public void setGradeId(int gradeId) {
        this.gradeId = gradeId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public int getAreaId() {
        return areaId;
    }

    public void setAreaId(int areaId) {
        this.areaId = areaId;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceId) {
        this.provinceId = provinceId;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public static User getUser(LocalUser localUser) {
        User user = new User();
        user.setObjectId(localUser.getObjectId());
        user.setMobilePhoneNumber(localUser.getMobilePhoneNumber());
        user.setEmail(localUser.getEmail());
        user.setSex(localUser.getSex());
        user.setAge(localUser.getAge());
        user.setSchoolId(localUser.getSchoolId());
        user.setRoleId(localUser.getRoleId());
        user.setGradeId(localUser.getGradeId());
        user.setCityId(localUser.getCityId());
        user.setAreaId(localUser.getAreaId());
        user.setProvinceId(localUser.getProvinceId());
        user.setClassId(localUser.getClassId());
        user.setRealName(localUser.getRealName());
        user.setUsername(localUser.getUsername());
        user.setIcoPath(localUser.getIcoPath());
        return user;
    }
}
