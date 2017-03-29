package cn.fuyoushuo.vipmovie.ext;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by QA on 2017/3/20.
 */

public class AppInfoManger {

    private static String SESSION_KEY = "vip_session_key";

    private Context context;

    private SharedPreferences sharedPreferences;

    private static class AppInfoMangerHolder{
        private static AppInfoManger INTANCE = new AppInfoManger();
    }

    public static AppInfoManger getIntance(){
        return AppInfoMangerHolder.INTANCE;
    }

    public void initContext(Context context){
        this.context = context;
        sharedPreferences = context.getSharedPreferences("AppInfo",Context.MODE_PRIVATE);
    }

    private boolean isNoPic = false;

    public void setNoPic(){
         this.isNoPic = !isNoPic;
    }

    public boolean isNoPic(){
        return this.isNoPic;
    }

    //会话是否存在
    public boolean isSessionExist(){
        return sharedPreferences.contains(SESSION_KEY);
    }

    //保存会话
    public void saveVipCookieSession(String sessionResult){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(SESSION_KEY,sessionResult);
        edit.commit();
    }

    //获得会话
    public String getVipCookieSession(){
        String sessionResult = "";
        if(sharedPreferences.contains(SESSION_KEY)){
            sessionResult = sharedPreferences.getString(SESSION_KEY,"");
        }
        return sessionResult;
    }

}
