package com.zhangteng.xim.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.zhangteng.xim.db.bean.NewFriend;
import com.zhangteng.xim.db.dao.DaoMaster;
import com.zhangteng.xim.db.dao.DaoSession;
import com.zhangteng.xim.db.dao.NewFriendDao;
import com.zhangteng.xim.db.dao.UserDao;

import org.greenrobot.greendao.query.DeleteQuery;

import java.util.List;

/**
 * Created by Lanxumit on 2017/9/5.
 * 使用前请先初始化context
 */

public class DBManager {
    private static DBManager dbManager = null;
    private static Context context = null;
    private DaoMaster.DevOpenHelper openHelper;

    private DBManager() {
    }

    public static synchronized DBManager instance() {
        if (dbManager == null) {
            synchronized (DBManager.class) {
                if (dbManager == null) {
                    dbManager = new DBManager();
                    if (context != null) {
                        dbManager.initDbHelp();
                    } else {
                        throw new NullPointerException("context is null");
                    }
                }
            }
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
            context = null;
        }
    }


    private void initDbHelp() {
        close();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "GreenDaoModule.db", null);
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

    public void insertUser(User user) {
        DBManager.instance().openWritableDb().getUserDao().insert(user);
    }

    public void deleteUser(User user) {
        DBManager.instance().openWritableDb().getUserDao().delete(user);
    }

    public void updateUser(User user) {
        UserDao userDao = DBManager.instance().openWritableDb().getUserDao();
        DeleteQuery<User> deleteQuery = userDao.queryBuilder().where(UserDao.Properties.Id.eq(user.getId())).where(UserDao.Properties.Name.eq(user.getName())).buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
        userDao.insertOrReplace(user);
    }

    public List<User> queryUser(User user) {
        UserDao userDao = DBManager.instance().openReadableDb().getUserDao();
        List<User> list = userDao.queryBuilder().where(UserDao.Properties.Name.eq(user.getName())).build().list();
        return list;
    }

    public void insertNewFriend(NewFriend newFriend) {
        DBManager.instance().openWritableDb().getNewFriendDao().insert(newFriend);
    }

    public void deleteNewFriend(NewFriend newFriend) {
        DBManager.instance().openWritableDb().getNewFriendDao().delete(newFriend);
    }

    public void updateNewFriend(NewFriend newFriend) {
        NewFriendDao newFriendDao = DBManager.instance().openWritableDb().getNewFriendDao();
        DeleteQuery<NewFriend> deleteQuery = newFriendDao.queryBuilder().where(NewFriendDao.Properties.Uid.eq(newFriend.getUid())).buildDelete();
        deleteQuery.executeDeleteWithoutDetachingEntities();
        newFriendDao.insertOrReplace(newFriend);
    }

    public List<NewFriend> queryNewFriend(NewFriend newFriend) {
        NewFriendDao newFriendDao = DBManager.instance().openReadableDb().getNewFriendDao();
        List<NewFriend> list = newFriendDao.queryBuilder().where(NewFriendDao.Properties.Uid.eq(newFriend.getUid())).build().list();
        return list;
    }
}
