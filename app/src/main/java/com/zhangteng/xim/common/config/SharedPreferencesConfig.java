package com.zhangteng.xim.common.config;

/**
 * Created by Lanxumit on 2017/12/15.
 */

public class SharedPreferencesConfig {

    public static class AppConfig {
        public static String NAVIGATION_TAG = "navigationTag";

    }

    public static class UserInfoConfig {
        //用户id
        public static String USER_ID = "userId";
        //用户身份 1学生、2老师、3管理者、4家长，
        public static String USER_TYPE = "userType";
        //用户姓名
        public static String USER_NAME = "realName";
        //用户头像
        public static String USER_ICOPATH = "icoPath";
        //学校id
        public static String SCHOOLID = "schoolId";
        //学校名称
        public static String SCHOOL_NAME = "schoolName";
        //班级id
        public static String CLASSID = "classId";
        //年级id
        public static String GRADEID = "gradeId";
        //vip剩余天数
        public static String VIP_DAYS = "vipDays";

        public static String CLASS_INFO = "classInfo";
        //联系方式
        public static String PHONE = "phone";
        //家长联系方式
        public static String PARENT_PHONE = "parentPhone";
        //邮箱
        public static String EMAIL = "email";
        //性别
        public static String SEX = "sex";
        //班主任 0 不是 1 是
        public static String ISHEADER = "isHeader";
    }

}
