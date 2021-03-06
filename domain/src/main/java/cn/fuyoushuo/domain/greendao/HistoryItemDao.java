package cn.fuyoushuo.domain.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import cn.fuyoushuo.domain.entity.HistoryItem;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "HISTORY_ITEM".
*/
public class HistoryItemDao extends AbstractDao<HistoryItem, Long> {

    public static final String TABLENAME = "HISTORY_ITEM";

    /**
     * Properties of entity HistoryItem.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property HistoryTitle = new Property(1, String.class, "historyTitle", false, "HISTORY_TITLE");
        public final static Property HistoryUrl = new Property(2, String.class, "historyUrl", false, "HISTORY_URL");
        public final static Property HistoryType = new Property(3, int.class, "historyType", false, "HISTORY_TYPE");
        public final static Property InputMd5 = new Property(4, String.class, "inputMd5", false, "INPUT_MD5");
        public final static Property CreateTime = new Property(5, java.util.Date.class, "createTime", false, "CREATE_TIME");
    }


    public HistoryItemDao(DaoConfig config) {
        super(config);
    }
    
    public HistoryItemDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"HISTORY_ITEM\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"HISTORY_TITLE\" TEXT," + // 1: historyTitle
                "\"HISTORY_URL\" TEXT," + // 2: historyUrl
                "\"HISTORY_TYPE\" INTEGER NOT NULL ," + // 3: historyType
                "\"INPUT_MD5\" TEXT," + // 4: inputMd5
                "\"CREATE_TIME\" INTEGER);"); // 5: createTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"HISTORY_ITEM\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, HistoryItem entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String historyTitle = entity.getHistoryTitle();
        if (historyTitle != null) {
            stmt.bindString(2, historyTitle);
        }
 
        String historyUrl = entity.getHistoryUrl();
        if (historyUrl != null) {
            stmt.bindString(3, historyUrl);
        }
        stmt.bindLong(4, entity.getHistoryType());
 
        String inputMd5 = entity.getInputMd5();
        if (inputMd5 != null) {
            stmt.bindString(5, inputMd5);
        }
 
        java.util.Date createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(6, createTime.getTime());
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, HistoryItem entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String historyTitle = entity.getHistoryTitle();
        if (historyTitle != null) {
            stmt.bindString(2, historyTitle);
        }
 
        String historyUrl = entity.getHistoryUrl();
        if (historyUrl != null) {
            stmt.bindString(3, historyUrl);
        }
        stmt.bindLong(4, entity.getHistoryType());
 
        String inputMd5 = entity.getInputMd5();
        if (inputMd5 != null) {
            stmt.bindString(5, inputMd5);
        }
 
        java.util.Date createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(6, createTime.getTime());
        }
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public HistoryItem readEntity(Cursor cursor, int offset) {
        HistoryItem entity = new HistoryItem( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // historyTitle
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // historyUrl
            cursor.getInt(offset + 3), // historyType
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // inputMd5
            cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)) // createTime
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, HistoryItem entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setHistoryTitle(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setHistoryUrl(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setHistoryType(cursor.getInt(offset + 3));
        entity.setInputMd5(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setCreateTime(cursor.isNull(offset + 5) ? null : new java.util.Date(cursor.getLong(offset + 5)));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(HistoryItem entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(HistoryItem entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(HistoryItem entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
