package com.zhangteng.xim.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.zhangteng.xim.db.bean.NewFriend;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

/**
 * Created by swing on 2018/5/16.
 */
public class NewFriendDao extends AbstractDao<NewFriend, Long> {

    public static final String TABLENAME = "NEWFRIEND";

    public NewFriendDao(DaoConfig config) {
        super(config);
    }


    public NewFriendDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /**
     * Properties of entity NewFriend.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Uid = new Property(1, String.class, "uid", false, "UID");
        public final static Property Msg = new Property(2, String.class, "msg", false, "MSG");
        public final static Property Name = new Property(3, String.class, "name", false, "NAME");
        public final static Property Avatar = new Property(4, String.class, "avatar", false, "AVATAR");
        public final static Property Status = new Property(5, Integer.class, "status", false, "STATUS");
        public final static Property Time = new Property(6, Long.class, "time", false, "TIME");
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"NEWFRIEND\" (" +
                "\"_id\" INTEGER AUTO_INCREMENT PRIMARY KEY ," +
                "\"UID\" TEXT ," +
                "\"MSG\" TEXT ," +
                "\"NAME\" TEXT ," +
                "\"AVATAR\" TEXT ," +
                "\"STATUS\" INTEGER ," +
                "\"TIME\" INTEGER);");
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"NEWFRIEND\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, NewFriend entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        String uid = entity.getUid();
        if (uid != null) {
            stmt.bindString(2, uid);
        }
        String msg = entity.getMsg();
        if (msg != null) {
            stmt.bindString(3, msg);
        }
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(5, avatar);
        }
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(6, status);
        }
        Long time = entity.getTime();
        if (time != null) {
            stmt.bindLong(7, time);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, NewFriend entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        String uid = entity.getUid();
        if (uid != null) {
            stmt.bindString(2, uid);
        }
        String msg = entity.getMsg();
        if (msg != null) {
            stmt.bindString(3, msg);
        }
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(4, name);
        }
        String avatar = entity.getAvatar();
        if (avatar != null) {
            stmt.bindString(5, avatar);
        }
        Integer status = entity.getStatus();
        if (status != null) {
            stmt.bindLong(6, status);
        }
        Long time = entity.getTime();
        if (time != null) {
            stmt.bindLong(7, time);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    @Override
    public NewFriend readEntity(Cursor cursor, int offset) {
        NewFriend entity = new NewFriend(
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0),
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1),
                cursor.isNull(offset + 2) ? null : cursor.getString(offset + 1),
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 1),
                cursor.isNull(offset + 4) ? null : cursor.getString(offset + 1),
                cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 1),
                cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 1)
        );
        return entity;
    }

    @Override
    public void readEntity(Cursor cursor, NewFriend entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setUid(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setMsg(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setName(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setAvatar(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setStatus(cursor.isNull(offset + 5) ? null : cursor.getInt(offset + 5));
        entity.setTime(cursor.isNull(offset + 6) ? null : cursor.getLong(offset + 6));
    }

    @Override
    protected final Long updateKeyAfterInsert(NewFriend entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    @Override
    public Long getKey(NewFriend entity) {
        if (entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(NewFriend entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }

}
