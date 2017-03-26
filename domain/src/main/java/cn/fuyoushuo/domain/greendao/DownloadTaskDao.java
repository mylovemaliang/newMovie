package cn.fuyoushuo.domain.greendao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import cn.fuyoushuo.domain.entity.DownloadTask;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "DOWNLOAD_TASK".
*/
public class DownloadTaskDao extends AbstractDao<DownloadTask, Long> {

    public static final String TABLENAME = "DOWNLOAD_TASK";

    /**
     * Properties of entity DownloadTask.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, long.class, "id", true, "_id");
        public final static Property DownloadId = new Property(1, long.class, "downloadId", false, "DOWNLOAD_ID");
        public final static Property Url = new Property(2, String.class, "url", false, "URL");
        public final static Property Title = new Property(3, String.class, "title", false, "TITLE");
        public final static Property CreateTime = new Property(4, java.util.Date.class, "createTime", false, "CREATE_TIME");
        public final static Property TaskState = new Property(5, int.class, "taskState", false, "TASK_STATE");
    }


    public DownloadTaskDao(DaoConfig config) {
        super(config);
    }
    
    public DownloadTaskDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"DOWNLOAD_TASK\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," + // 0: id
                "\"DOWNLOAD_ID\" INTEGER NOT NULL ," + // 1: downloadId
                "\"URL\" TEXT," + // 2: url
                "\"TITLE\" TEXT," + // 3: title
                "\"CREATE_TIME\" INTEGER," + // 4: createTime
                "\"TASK_STATE\" INTEGER NOT NULL );"); // 5: taskState
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"DOWNLOAD_TASK\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, DownloadTask entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getDownloadId());
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(3, url);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(4, title);
        }
 
        java.util.Date createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(5, createTime.getTime());
        }
        stmt.bindLong(6, entity.getTaskState());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, DownloadTask entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getId());
        stmt.bindLong(2, entity.getDownloadId());
 
        String url = entity.getUrl();
        if (url != null) {
            stmt.bindString(3, url);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(4, title);
        }
 
        java.util.Date createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindLong(5, createTime.getTime());
        }
        stmt.bindLong(6, entity.getTaskState());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.getLong(offset + 0);
    }    

    @Override
    public DownloadTask readEntity(Cursor cursor, int offset) {
        DownloadTask entity = new DownloadTask( //
            cursor.getLong(offset + 0), // id
            cursor.getLong(offset + 1), // downloadId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // url
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // title
            cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)), // createTime
            cursor.getInt(offset + 5) // taskState
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, DownloadTask entity, int offset) {
        entity.setId(cursor.getLong(offset + 0));
        entity.setDownloadId(cursor.getLong(offset + 1));
        entity.setUrl(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setTitle(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCreateTime(cursor.isNull(offset + 4) ? null : new java.util.Date(cursor.getLong(offset + 4)));
        entity.setTaskState(cursor.getInt(offset + 5));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(DownloadTask entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(DownloadTask entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(DownloadTask entity) {
        throw new UnsupportedOperationException("Unsupported for entities with a non-null key");
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}