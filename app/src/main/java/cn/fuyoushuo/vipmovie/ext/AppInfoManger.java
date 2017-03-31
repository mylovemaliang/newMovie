package cn.fuyoushuo.vipmovie.ext;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by QA on 2017/3/20.
 */

public class AppInfoManger {

    private static String SESSION_KEY = "vip_session_key";

    private static String TOKEY_KEY = "token_key";

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
    public void saveVipCookieSession(String sessionId,String token){
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(SESSION_KEY,sessionId);
        edit.putString(TOKEY_KEY,token);
        edit.commit();
    }

    //获得会话
    public SessionPair getVipCookieSession(){
        String sessionResult = "";
        String tokenResult = "";
        if(sharedPreferences.contains(SESSION_KEY)){
            sessionResult = sharedPreferences.getString(SESSION_KEY,"");
        }
        if(sharedPreferences.contains(TOKEY_KEY)){
            tokenResult = sharedPreferences.getString(TOKEY_KEY,"");
        }
        return new SessionPair(sessionResult,tokenResult);
    }

    public static class SessionPair{

        private String sessionId;

        private String token;

        public SessionPair(String sessionId, String token) {
            this.sessionId = sessionId;
            this.token = token;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

}
