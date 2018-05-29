package com.zhangteng.xim.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import com.zhangteng.xim.db.bean.CityNo;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

/**
 * Created by swing on 2018/5/29.
 */
public class CityNoDao extends AbstractDao<CityNo, Long> {

    public static final String TABLENAME = "CITYNO";

    public CityNoDao(DaoConfig config) {
        super(config);
    }


    public CityNoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /**
     * Creates the underlying database table.
     */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists ? "IF NOT EXISTS " : "";
        db.execSQL("CREATE TABLE " + constraint + "\"CITYNO\" (" +
                "\"_id\" INTEGER AUTO_INCREMENT PRIMARY KEY ," +
                "\"Code\" ," +
                "\"Region\" TEXT ," +
                "\"Parent\" TEXT);");
    }

    /**
     * Drops the underlying database table.
     */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CITYNO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CityNo entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(2, code);
        }

        String region = entity.getRegion();
        if (region != null) {
            stmt.bindString(3, region);
        }

        String parent = entity.getParent();
        if (parent != null) {
            stmt.bindString(4, parent);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CityNo entity) {
        stmt.clearBindings();

        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        String code = entity.getCode();
        if (code != null) {
            stmt.bindString(2, code);
        }

        String region = entity.getRegion();
        if (region != null) {
            stmt.bindString(3, region);
        }

        String parent = entity.getParent();
        if (parent != null) {
            stmt.bindString(4, parent);
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }

    @Override
    public CityNo readEntity(Cursor cursor, int offset) {
        CityNo entity = new CityNo(
                cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0),
                cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1),
                cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2),
                cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3)
        );
        return entity;
    }

    @Override
    public void readEntity(Cursor cursor, CityNo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCode(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setRegion(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setParent(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
    }

    @Override
    protected final Long updateKeyAfterInsert(CityNo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }

    @Override
    public Long getKey(CityNo entity) {
        if (entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CityNo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }

    /**
     * Properties of entity CityNo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Code = new Property(1, String.class, "code", false, "code");
        public final static Property Region = new Property(2, String.class, "region", false, "region");
        public final static Property Parent = new Property(3, String.class, "parent", false, "parent");
    }

}
