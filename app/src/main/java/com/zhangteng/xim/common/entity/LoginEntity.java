package com.zhangteng.xim.common.entity;

/**
 * Created by Lanxumit on 2017/11/29.
 */

public class LoginEntity {

    private String userId;
    private String userType;
    private String realName;
    private String icoPath;
    private String schoolId;
    private String schoolName;
    private int[] classId;
    private String gradeId;
    private String otherId;
    private String vipDays;

    public LoginEntity(String userId, String userType, String realName, String icoPath, String schoolId, String schoolName, int[] classId, String gradeId, String otherId, String vipDays) {
        this.userId = userId;
        this.userType = userType;
        this.realName = realName;
        this.icoPath = icoPath;
        this.schoolId = schoolId;
        this.schoolName = schoolName;
        this.classId = classId;
        this.gradeId = gradeId;
        this.otherId = otherId;
        this.vipDays = vipDays;
    }

    public LoginEntity() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
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

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public int[] getClassId() {
        return classId;
    }

    public void setClassId(int[] classId) {
        this.classId = classId;
    }

    public String getGradeId() {
        return gradeId;
    }

    public void setGradeId(String gradeId) {
        this.gradeId = gradeId;
    }

    public String getOtherId() {
        return otherId;
    }

    public void setOtherId(String otherId) {
        this.otherId = otherId;
    }

    public String getVipDays() {
        return vipDays;
    }

    public void setVipDays(String vipDays) {
        this.vipDays = vipDays;
    }
}
