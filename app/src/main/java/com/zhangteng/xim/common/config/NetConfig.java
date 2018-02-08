package com.zhangteng.xim.common.config;

/**
 * Created by Lanxumit on 2017/11/29.
 */

public class NetConfig {

    public static final String BASE_URL = "http://000.000.000.000:000/";
    public static final String PATH = "**.**";

    public static class NetMethodConfig {
        //登录
        public static final String LOGIN = "logInByUidAndPwd";
        //获取用户信息
        public static final String GET_USERINFO = "getUserInfo";
        //获取班级概况
        public static final String GET_CLASSINFO = "getClassInfo";
        //获取学习动态
        public static final String GET_USERSTUDYMSG = "getUserStuDyMsg";
        //获取所教班级
        public static final String GET_TEACHING_CLASS = "getClassTeacherNowC";
        //获取班级活动
        public static final String GET_CLASSMESSAGELIST = "getClassMessageList";
        //添加班级活动
        public static final String ADD_CLASSACTIVITIES = "addClassActivities";
        //删除班级活动
        public static final String DELETE_CLASSACTIVITIES = "delClassActivities";

        //获取本周课表
        public static final String GET_COURSELIST_BYTID = "getCourseListByTid";
        //获取班级本周课表
        public static final String GET_COURSELIST_BYCID = "getCourseListByCid";
        //获取今日作业列表
        public static final String GET_HBTEATODAYWORK = "getHBTeaTodayWork";
        //发送今日作业提醒
        public static final String ADD_WORKREMIND = "addWorkRemind";
        //家长确认列表
        public static final String GET_HBPARCONSUBSTATUS = "getHBParConSubStatus";
        //获取学科家长确认列表
        public static final String GET_HBPARCONSUBINFO = "getHBParConSubInfo";
        //获取留言列表
        public static final String GET_USERLEAVELIST = "getUserLeaveList";
        //回复留言
        public static final String ADD_LEAVEMESSAGE = "addLeaveMessage";
        //删除留言
        public static final String DELETE_LEAVECOMMENT = "delComment";
        //删除留言回复
        public static final String DELETE_COMMENTREPLY = "delCommentReply";

        //获取留言列表
        public static final String GET_LEAVE_LIST = "getUserLeaveList";
        //回复留言
        public static final String ADD_LEAVE_MESSAGE = "addLeaveMessage";
        //删除留言
        public static final String DEL_COMMENT = "delComment";
        //获取本周课表
        public static final String GET_COURSESCHEDULE = "getCourseListByTid";
        //关注 查找用户
        public static final String GET_HB_TEACHERLIST = "getHBTeacherList";
        //查找用户详情
        public static final String GET_HB_USERDETAIL = "getHBUserDetail";
        //获取已关注列表
        public static final String GET_MYFOLLOWS = "getMyFollows";
        //删除关注
        public static final String DELETE_USER_ATTENTION = "deleteUserAttention";
        //添加关注
        public static final String Add_USER_ATTENTION = "addUserAttention";
        //获取学习资源列表
        public static final String GET_HB_COURSELIST = "getHBCourseList";
        //获取学习资源详情
        public static final String GET_COURSE_DETAIL = "getCourseDetail";
        //添加收藏
        public static final String ADD_USER_COLLECT = "addUserCollect";
        //取消收藏
        public static final String DELETE_USER_COLLECT = "deleteUserCollect";
        //推荐课程
        public static final String ADD_VIPRECOMMAND = "addVipRecommand";
        //获取收藏列表
        public static final String GET_USER_COLLECTS = "getUserCollectVipCourseList";
        //获取教师学习资源列表
        public static final String GET_TEACHER_COURSELIST = "getTeacherCourseList";
        //备课
        //获取教师备课列表
        public static final String GET_HBPREPARELIST = "getHBPrepareList";
        //获取教师备课发布的班级列表
        public static final String GET_HBPRE_RELEASECLASS = "getHBPreReleaseClass";
        //获取班级学生答题情况列表
        public static final String GET_STUDENTTYPEWORK = "getHBStudentTypeWork";
        //获取学生答题情况详情
        public static final String GET_HBSTUDENTWORKDETAIL = "getHBStudentWorkDetail";
        //获取班级学生知识点掌握情况列表
        public static final String GET_HB_TEAPREKNOWRATE = "getHBTeaPreKnowRate";
        //获取班级学生各题测试情况列表
        public static final String GET_HB_TEAPREEXAMRATE = "getHBTeaPreExamRate";
        //批改学生答题
        public static final String MARK_HB_WORK = "markHBWork";


        //获取教师备课列表分类依据
        public static final String GET_CLASSIFICATIONOFPREPARATIONS = "getClassificationOfPreparations";
        //获取教师备课列表
        public static final String GET_CLASS_PREPARATIONS = "getPreparations";

        //获取教师备课列表详情
        public static final String GET_PREPARATIONDETAIL = "getPreparationDetail";

        //获取班级学生答题情况列表
        public static final String GET_ANSWERSHEET = "getAnswerSheets";
        //获取学生答题情况分析
        public static final String GET_ANSWER_SHEETS_ANALYSIS = "getAnswerSheetsAnalysis";
        //确认学生答题情况分析
        public static final String CONFIRM_ANSWERSHEETS_ANALYSIS = "confirmAnswerSheetsAnalysis";

        //获取待完成作业列表
        public static final String GET_TODOHOMEWORKS = "getToDoHomeWorks";
        //获取已答题列表
        public static final String GET_COMPLETED_HOMEWORK = "getCompletedHomeWorks";

        //获取学生答题情况详情
        public static final String GET_ANSWERSHEET_DETAIL = "getAnswerSheetDetail";

        //获取班级学生知识点掌握情况列表
        public static final String GET_GRASPINGOFKNOWLEDGEPOINTS = "getGraspingOfKnowledgePoints";

        //获取班级学生各题测试情况列表
        public static final String GET_GRASPINGOFQUESTIONS = "getGraspingOfQuestions";


        //获取年级各班报告
        public static final String GET_REPORTSOFCLASSES = "getReportsOfClasses";

        //获取年级各班课前课中课后正确率报告
        public static final String GET_ACCURACYOFCLASSES = "getAccuracyOfClasses";

        //获取班级各学生实施掌握情
        public static final String GET_GRASPINGOFSTUDENTS = "getGraspingOfStudents";

        //获取班级通知列表
        public static final String GET_CLASS_NOTICE_LIST = "getClassNoticeList";
        //添加班级通知
        public static final String ADD_CLASS_NOTICE = "addClassNotices";
        //获取用户今日课表
        public static final String GET_TODAY_COURSE_LIST = "getTodayCourseListByTid";
        //提交意见反馈
        public static final String COMMIT_HB_FEEDBACK = "commitHbFeedBack";
        //上传文件
        public static final String UPLOAD_FILE = "uploadFile";

        //获取科目
        public static final String GET_FILTER_SUBJECT = "getFilterSubjectL";
        //获取版本
        public static final String GET_FILTER_VERSION = "getFilterVersionL";
        //取分册
        public static final String GET_FILTER_FAS = "getFilterFasL";
        //取学段
        public static final String GET_FILTER_STAGE = "getFilterStageL";
        //筛选章节目录树
        public static final String GET_FILTER_CHAPTER = "getFilterChapterL";
        //筛选知识点目录树
        public static final String GET_FILTER_KNOW = "getFilterKnowL";

        //获取应用版本信息
        public static final String GET_SOFTVERSION_LASTEST = "getSoftVersionLastest";

    }


    public static class ResponseKeyConfig {

        public static final String MESSAGE = "msg";
        public static final String CODE = "code";
        public static final String DATA = "data";

    }

    public static class ResponseMessageConfig {
        public static final String SUCCESS = "success";
    }

    public static class ResponseCodeConfig {
        /**
         * OK
         */
        public static final int OK = 1000;
        /**
         * 参数错误或令牌验证错误
         */
        public static final int ERRO_RPARAMORTOKEN = 1001;
        /**
         * 数据库操作或服务执行失败
         */
        public static final int FAILED_DATABASEORSERVICE = 1002;
        /**
         * 缓存服务访问错误
         */
        public static final int ERROR_CACHESERVICEACCESS = 1003;
        /**
         * 暂无相关数据！
         */
        public static final int NO_DATA = 1004;
        /**
         * 用户还未设置科目！
         */
        public static final int NOT_SETSUBJECT = 1005;
        /**
         * 用户还未加入班级！
         */
        public static final int NOT_JOINCLASS = 1006;
        /**
         * 没有数据更新
         */
        public static final int NOT_DATAUPDATE = 1007;
        /**
         * 数据更新失败
         */
        public static final int FAILED_DATAUPDATE = 1008;
        /**
         * failure
         */
        public static final int FAILURE = 1009;
        /**
         * 用户自己上传的资源
         */
        public static final int USER_SELFRESOURCE = 1010;
        /**
         * 资源已收藏！
         */
        public static final int USER_RESOURCECOLLECTION = 1011;
        /**
         * 时间、NONCE或签名验证错误
         */
        public static final int ERROR_TIMEORNONCEORTOKEN = 1012;
        /**
         * 获取用户ID错误
         */
        public static final int ERROR_GAINUSERID = 1013;
        /**
         * 连接缓存失败
         */
        public static final int FAILED_CONNCACHE = 1014;
        /**
         * 读取缓存失败
         */
        public static final int FAILED_GETCACHE = 1015;
        /**
         * 写入缓存失败
         */
        public static final int FAILED_WRITECACHE = 1016;
        /**
         * 参数为空或有误
         */
        public static final int ERROR_OR_NULL_PARAM = 1017;
        /**
         * 初始数据缓存失败
         */
        public static final int FAILED_INITCACHE = 1018;
        /**
         * 账户不存在!
         */
        public static final int NOT_USER = 1019;
        /**
         * 密码错误
         */
        public static final int ERROR_PASSWORD = 1020;
        /**
         * 读取数据失败
         */
        public static final int FAILED_READDATA = 1021;
        /**
         * 账号格式不对
         */
        public static final int ERROR_USERIDFORMAT = 1022;
        /**
         * 该账号未激活，请联系管理员
         */
        public static final int NOT_USERIDACTIVITIED = 1023;
        /**
         * 账号或密码错误
         */
        public static final int ERROR_USERORPASSWORD = 1024;
        /**
         * 角色错误
         */
        public static final int ERROR_ROLE = 1025;
        /**
         * 手机号已被注册
         */
        public static final int USEED_PHONE = 1100;
        /**
         * 手机号或密码错误
         */
        public static final int ERROR_PHONEORPASSWORD = 1101;
        /**
         * 验证码错误
         */
        public static final int ERROR_VERIFICATIONCODE = 1102;
        /**
         * 输入名称已经存在
         */
        public static final int USEED_INPUTNAME = 1103;
        /**
         * 操作权限错误
         */
        public static final int ERROR_PERMISSION = 1104;
        /**
         * 请求内容不存在
         */
        public static final int NOT_REQUESTCONTENT = 1105;
        /**
         * 发送验证码错误
         */
        public static final int ERROR_SENDVERIFICATION = 1106;
        /**
         * 卡号错误
         */
        public static final int ERROR_CARDNODE = 1200;
        /**
         * 登录成功!
         */
        public static final int SUCCESS_LOGIN = 2001;
        /**
         * 成功
         */
        public static final int SUCCESS = 2002;
        /**
         * 用户未激活
         */
        public static final int NOT_USERACTIVITIED = 2005;
    }
}
