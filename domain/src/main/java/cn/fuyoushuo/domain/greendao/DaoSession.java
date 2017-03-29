package cn.fuyoushuo.domain.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import cn.fuyoushuo.domain.entity.BookMark;
import cn.fuyoushuo.domain.entity.HistoryItem;
import cn.fuyoushuo.domain.entity.StaticData;
import cn.fuyoushuo.domain.entity.UserTrack;
import cn.fuyoushuo.domain.entity.DownloadTask;

import cn.fuyoushuo.domain.greendao.BookMarkDao;
import cn.fuyoushuo.domain.greendao.HistoryItemDao;
import cn.fuyoushuo.domain.greendao.StaticDataDao;
import cn.fuyoushuo.domain.greendao.UserTrackDao;
import cn.fuyoushuo.domain.greendao.DownloadTaskDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig bookMarkDaoConfig;
    private final DaoConfig historyItemDaoConfig;
    private final DaoConfig staticDataDaoConfig;
    private final DaoConfig userTrackDaoConfig;
    private final DaoConfig downloadTaskDaoConfig;

    private final BookMarkDao bookMarkDao;
    private final HistoryItemDao historyItemDao;
    private final StaticDataDao staticDataDao;
    private final UserTrackDao userTrackDao;
    private final DownloadTaskDao downloadTaskDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        bookMarkDaoConfig = daoConfigMap.get(BookMarkDao.class).clone();
        bookMarkDaoConfig.initIdentityScope(type);

        historyItemDaoConfig = daoConfigMap.get(HistoryItemDao.class).clone();
        historyItemDaoConfig.initIdentityScope(type);

        staticDataDaoConfig = daoConfigMap.get(StaticDataDao.class).clone();
        staticDataDaoConfig.initIdentityScope(type);

        userTrackDaoConfig = daoConfigMap.get(UserTrackDao.class).clone();
        userTrackDaoConfig.initIdentityScope(type);

        downloadTaskDaoConfig = daoConfigMap.get(DownloadTaskDao.class).clone();
        downloadTaskDaoConfig.initIdentityScope(type);

        bookMarkDao = new BookMarkDao(bookMarkDaoConfig, this);
        historyItemDao = new HistoryItemDao(historyItemDaoConfig, this);
        staticDataDao = new StaticDataDao(staticDataDaoConfig, this);
        userTrackDao = new UserTrackDao(userTrackDaoConfig, this);
        downloadTaskDao = new DownloadTaskDao(downloadTaskDaoConfig, this);

        registerDao(BookMark.class, bookMarkDao);
        registerDao(HistoryItem.class, historyItemDao);
        registerDao(StaticData.class, staticDataDao);
        registerDao(UserTrack.class, userTrackDao);
        registerDao(DownloadTask.class, downloadTaskDao);
    }
    
    public void clear() {
        bookMarkDaoConfig.clearIdentityScope();
        historyItemDaoConfig.clearIdentityScope();
        staticDataDaoConfig.clearIdentityScope();
        userTrackDaoConfig.clearIdentityScope();
        downloadTaskDaoConfig.clearIdentityScope();
    }

    public BookMarkDao getBookMarkDao() {
        return bookMarkDao;
    }

    public HistoryItemDao getHistoryItemDao() {
        return historyItemDao;
    }

    public StaticDataDao getStaticDataDao() {
        return staticDataDao;
    }

    public UserTrackDao getUserTrackDao() {
        return userTrackDao;
    }

    public DownloadTaskDao getDownloadTaskDao() {
        return downloadTaskDao;
    }

}
