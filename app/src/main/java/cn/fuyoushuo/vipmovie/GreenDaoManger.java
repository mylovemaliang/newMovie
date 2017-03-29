package cn.fuyoushuo.vipmovie;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import cn.fuyoushuo.domain.greendao.DaoMaster;
import cn.fuyoushuo.domain.greendao.DaoSession;

/**
 * Created by QA on 2017/3/8.
 */

public class GreenDaoManger {

    private Context context;

    private static class GreenDaoMangerHolder{
        private static GreenDaoManger INTANCE = new GreenDaoManger();
    }

    public static GreenDaoManger getIntance(){
        return GreenDaoManger.GreenDaoMangerHolder.INTANCE;
    }

    //初始化上下文
    public void initContext(Context context){
        this.context = context;
    }

    private DaoMaster.DevOpenHelper mHelper;

    private SQLiteDatabase db;

    private DaoMaster mDaoMaster;

    private DaoSession mDaoSession;

    //初始化
    public void initDatabase(){
         mHelper = new DaoMaster.DevOpenHelper(this.context,"vip-movie-db",null);
         db = mHelper.getWritableDatabase();
         mDaoMaster = new DaoMaster(db);
         //这里新建多个session,对应一个数据库连接
         mDaoSession = mDaoMaster.newDevSession(context,"vip_movie_dev");
    }

    public DaoSession getmDaoSession(){
        return this.mDaoSession;
    }

    public SQLiteDatabase getDb(){
        return this.db;
    }
}
