package cn.fuyoushuo.vipmovie.ext;

import android.content.Context;

/**
 * Created by QA on 2017/3/20.
 */

public class AppInfoManger {

    private Context context;

    private static class AppInfoMangerHolder{
        private static AppInfoManger INTANCE = new AppInfoManger();
    }

    public static AppInfoManger getIntance(){
        return AppInfoMangerHolder.INTANCE;
    }

    public void initContext(Context context){
        this.context = context;
    }

    private boolean isNoPic = false;

    public void setNoPic(){
         this.isNoPic = !isNoPic;
    }

    public boolean isNoPic(){
        return this.isNoPic;
    }

}
