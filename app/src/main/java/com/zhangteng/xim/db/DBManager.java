package com.zhangteng.xim.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zhangteng.xim.bmob.config.Config;
import com.zhangteng.xim.bmob.http.UserApi;
import com.zhangteng.xim.db.bean.CityNo;
import com.zhangteng.xim.db.bean.LocalUser;
import com.zhangteng.xim.db.bean.NewFriend;
import com.zhangteng.xim.db.dao.CityNoDao;
import com.zhangteng.xim.db.dao.DaoMaster;
import com.zhangteng.xim.db.dao.DaoSession;
import com.zhangteng.xim.db.dao.NewFriendDao;
import com.zhangteng.xim.db.dao.UserDao;

import org.greenrobot.greendao.query.DeleteQuery;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lanxumit on 2017/9/5.
 * 使用前请先初始化context
 */

public class DBManager {
    private static DBManager dbManager = null;
    private static Context context = null;
    private DaoMaster.DevOpenHelper openHelper;
    public static String DBNAME = "GreenDaoModule.db";
    public static String CITYNODBNAME = "CityNo.db";
    public static String USERNAME = UserApi.getInstance().getUserInfo().getObjectId() == null ? DBNAME : UserApi.getInstance().getUserInfo().getObjectId();

    private DBManager() {
    }

    /**
     * 默认db管理实例
     */
    public static synchronized DBManager instance() {
        if (dbManager == null) {
            synchronized (DBManager.class) {
                if (dbManager == null) {
                    dbManager = new DBManager();
                }
            }
        }
        if (context != null) {
            dbManager.initDbHelp();
        } else {
            throw new NullPointerException("context is null");
        }
        return dbManager;
    }

    /**
     * 自定义数据库名（CityNO.db）
     */
    public static synchronized DBManager instance(String dbNmae) {
        if (dbManager == null) {
            synchronized (DBManager.class) {
                if (dbManager == null) {
                    dbManager = new DBManager();
                }
            }
        }
        if (context != null) {
            dbManager.initDbHelp(dbNmae);
        } else {
            throw new NullPointerException("context is null");
        }
        return dbManager;
    }


    public static void init(Context context) {
        DBManager.context = context;
    }

    private void close() {
        if (openHelper != null) {
            openHelper.close();
            openHelper = null;
        }
    }


    private void initDbHelp() {
        close();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, DBNAME, null);
        this.openHelper = helper;
    }

    private void initDbHelp(String dbNmae) {
        close();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, dbNmae, null);
        this.openHelper = helper;
    }

    public DaoSession openReadableDb() {
        isInitOk();
        SQLiteDatabase db = openHelper.getReadableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }


    public DaoSession openWritableDb() {
        isInitOk();
        SQLiteDatabase db = openHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();
        return daoSession;
    }


    private void isInitOk() {
        if (openHelper == null) {
            //   FileLog.e("DBManager", "DBManager#isInit not success or start,cause by openHelper is null");

            throw new RuntimeException("DBManager#isInit not success or start,cause by openHelper is null");
        }
    }

    public void insertUser(LocalUser user) {
        DBManager.instance(USERNAME).openWritableDb().getUserDao().insert(user);
    }

    public void deleteUser(LocalUser user) {
        DBManager.instance(USERNAME).openWritableDb().getUserDao().delete(user);
    }

    public void updateUser(LocalUser user) {
        UserDao userDao = DBManager.instance(USERNAME).openWritableDb().getUserDao();
        DeleteQuery<LocalUser> deleteQuery = userDao.queryBuilder().where(UserDao.Properties.ObjectId.eq(user.getObjectId())).buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
        userDao.insertOrReplace(user);
    }

    public List<LocalUser> queryUser(LocalUser user) {
        UserDao userDao = DBManager.instance(USERNAME).openReadableDb().getUserDao();
        List<LocalUser> list = userDao.queryBuilder().where(UserDao.Properties.ObjectId.eq(user.getObjectId())).build().list();
        return list;
    }

    public LocalUser queryUser(String objectId) {
        UserDao userDao = DBManager.instance(USERNAME).openReadableDb().getUserDao();
        List<LocalUser> list = userDao.queryBuilder().where(UserDao.Properties.ObjectId.eq(objectId)).build().list();
        return list.isEmpty() ? null : list.get(0);
    }

    public List<LocalUser> queryUsers(int startId, int endId) {
        UserDao userDao = DBManager.instance(USERNAME).openReadableDb().getUserDao();
        QueryBuilder qb = userDao.queryBuilder();
        qb.offset(startId).limit(endId - startId);
        qb.build();
        return qb.list();
    }

    public long countUser() {
        UserDao userDao = DBManager.instance(USERNAME).openReadableDb().getUserDao();
        QueryBuilder qb = userDao.queryBuilder();
        return qb.count();
    }

    public void insertNewFriend(NewFriend newFriend) {
        DBManager.instance(USERNAME).openWritableDb().getNewFriendDao().insert(newFriend);
    }

    public void updateNewFriend(NewFriend newFriend) {
        NewFriendDao newFriendDao = DBManager.instance(USERNAME).openWritableDb().getNewFriendDao();
        DeleteQuery<NewFriend> deleteQuery = newFriendDao.queryBuilder().where(NewFriendDao.Properties.Uid.eq(newFriend.getUid())).buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
        newFriendDao.insertOrReplace(newFriend);
    }

    public List<NewFriend> queryNewFriend(NewFriend newFriend) {
        NewFriendDao newFriendDao = DBManager.instance(USERNAME).openReadableDb().getNewFriendDao();
        List<NewFriend> list = newFriendDao.queryBuilder().where(NewFriendDao.Properties.Uid.eq(newFriend.getUid())).build().list();
        return list;
    }

    public CityNo queryCityNo(String code) {
        CityNoDao cityNoDao = DBManager.instance(DBManager.CITYNODBNAME).openReadableDb().getCityNoDao();
        List<CityNo> list = cityNoDao.queryBuilder().where(CityNoDao.Properties.Code.eq(code)).build().list();
        return list.isEmpty() ? null : list.get(0);
    }

    public void insertCityNo(CityNo cityNo) {
        DBManager.instance(DBManager.CITYNODBNAME).openWritableDb().getCityNoDao().insert(cityNo);
    }

    /**
     * 获取本地所有的邀请信息
     *
     * @return
     */
    public List<NewFriend> getAllNewFriend() {
        NewFriendDao dao = DBManager.instance(USERNAME).openReadableDb().getNewFriendDao();
        return dao.queryBuilder().orderDesc(NewFriendDao.Properties.Time).list();
    }

    /**
     * 创建或更新新朋友信息
     *
     * @param info
     * @return long:返回插入或修改的id
     */
    public long insertOrUpdateNewFriend(NewFriend info) {
        NewFriendDao dao = DBManager.instance(USERNAME).openWritableDb().getNewFriendDao();
        NewFriend local = getNewFriend(info.getUid(), info.getTime());
        if (local == null) {
            return dao.insertOrReplace(info);
        } else {
            return -1;
        }
    }

    /**
     * 获取本地的好友请求
     *
     * @param uid
     * @param time
     * @return
     */
    private NewFriend getNewFriend(String uid, Long time) {
        NewFriendDao dao = DBManager.instance(USERNAME).openReadableDb().getNewFriendDao();
        return dao.queryBuilder().where(NewFriendDao.Properties.Uid.eq(uid))
                .where(NewFriendDao.Properties.Time.eq(time)).build().unique();
    }

    /**
     * 是否有新的好友邀请
     *
     * @return
     */
    public boolean hasNewFriendInvitation() {
        List<NewFriend> infos = getNoVerifyNewFriend();
        if (infos != null && infos.size() > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取未读的好友邀请
     *
     * @return
     */
    public int getNewInvitationCount() {
        List<NewFriend> infos = getNoVerifyNewFriend();
        if (infos != null && infos.size() > 0) {
            return infos.size();
        } else {
            return 0;
        }
    }

    /**
     * 获取所有未读未验证的好友请求
     *
     * @return
     */
    private List<NewFriend> getNoVerifyNewFriend() {
        NewFriendDao dao = DBManager.instance(USERNAME).openReadableDb().getNewFriendDao();
        return dao.queryBuilder().where(NewFriendDao.Properties.Status.eq(Config.STATUS_VERIFY_NONE))
                .build().list();
    }

    /**
     * 批量更新未读未验证的状态为已读
     */
    public void updateBatchStatus() {
        List<NewFriend> infos = getNoVerifyNewFriend();
        if (infos != null && infos.size() > 0) {
            int size = infos.size();
            List<NewFriend> all = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                NewFriend msg = infos.get(i);
                msg.setStatus(Config.STATUS_VERIFY_READED);
                all.add(msg);
            }
            insertBatchMessages(infos);
        }
    }

    /**
     * 批量插入消息
     *
     * @param msgs
     */
    public void insertBatchMessages(List<NewFriend> msgs) {
        NewFriendDao dao = DBManager.instance(USERNAME).openWritableDb().getNewFriendDao();
        dao.insertOrReplaceInTx(msgs);
    }

    /**
     * 修改指定好友请求的状态
     *
     * @param friend
     * @param status
     * @return
     */
    public long updateNewFriend(NewFriend friend, int status) {
        NewFriendDao dao = DBManager.instance(USERNAME).openWritableDb().getNewFriendDao();
        friend.setStatus(status);
        return dao.insertOrReplace(friend);
    }

    /**
     * 删除指定的添加请求
     *
     * @param friend
     */
    public void deleteNewFriend(NewFriend friend) {
        NewFriendDao dao = DBManager.instance(USERNAME).openWritableDb().getNewFriendDao();
        dao.delete(friend);
    }
}
