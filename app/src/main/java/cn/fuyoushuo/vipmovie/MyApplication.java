package cn.fuyoushuo.vipmovie;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.DisplayMetrics;
import android.util.Log;

import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.producers.LocalFetchProducer;
import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

import java.util.Stack;

import cn.fuyoushuo.vipmovie.ext.AppInfoManger;
import cn.fuyoushuo.vipmovie.ext.DownloadManger;
import cn.fuyoushuo.vipmovie.ext.LocalFragmentManger;
import cn.fuyoushuo.vipmovie.service.InitX5Service;
import okhttp3.OkHttpClient;

/**
 * Created by QA on 2016/6/27.
 */
public class MyApplication extends Application{

    private static Context context;

    private static DisplayMetrics displayMetrics;

    //private RefWatcher mRefWatcher;

    private String channelValue = "";

    private Stack<Activity> allActivitys = new Stack<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        try {
            ApplicationInfo appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            //feedbackAppkey = String.valueOf(appInfo.metaData.getInt("feedback_appkey"));
            channelValue = appInfo.metaData.getString("UMENG_CHANNEL");
        }catch (Exception e){

        }
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(context).setMemoryTrimmableRegistry(new MemoryTrimmableRegistry() {
            @Override
            public void registerMemoryTrimmable(MemoryTrimmable trimmable) {
                trimmable.trim(MemoryTrimType.OnAppBackgrounded);
            }

            @Override
            public void unregisterMemoryTrimmable(MemoryTrimmable trimmable) {

            }
        }).build();

        //开启初始化x5服务
        Intent serviceIntent = new Intent(context,InitX5Service.class);
        startService(serviceIntent);

        Fresco.initialize(context,config);
        LocalFragmentManger.getIntance().initContext(context);
        GreenDaoManger.getIntance().initContext(context);
        GreenDaoManger.getIntance().initDatabase();
        Stetho.initializeWithDefaults(this);
        AppInfoManger.getIntance().initContext(this);
        //下载管理器
        DownloadManger.getIntance().initContext(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();


        //初始化异常拦截器
        //CrashHandler.getInstance().init(this);
        //用户登录信息管理
        //LoginInfoStore.getIntance().init(this);
        //LocalStatisticInfo.getIntance().init(this);
        //禁用默认的统计机制
        //MobclickAgent.setDebugMode(false);
        //MobclickAgent.openActivityDurationTrack(false);
        displayMetrics = context.getResources().getDisplayMetrics();
        //mRefWatcher = Constants.DEBUG ?  LeakCanary.install(this) : RefWatcher.DISABLED;
        //initFeedBack(feedbackAppkey);

    }

//    public static RefWatcher getRefWatcher(Context context) {
//        MyApplication application = (MyApplication) context
//                .getApplicationContext();
//        return application.mRefWatcher;
//    }

    //获取channel值
    public static String getChannelValue() {
        MyApplication application = (MyApplication) context
                .getApplicationContext();
        return application.channelValue;
    }

    public static MyApplication getMyapplication(){
        MyApplication application = (MyApplication) context
                .getApplicationContext();
        return application;
    }

    public static Context getContext(){
        return context;
    }

    public static DisplayMetrics getDisplayMetrics(){
        return displayMetrics;
    }

//    /**
//     * 初始化阿里百川 用户回馈
//     * @param appKey 阿里百川申请 的appkey
//     */
//    private void initFeedBack(String appKey){
//        //阿里百川用户反馈
//        FeedbackAPI.initAnnoy((Application) context,appKey);
//        Map<String,String> feedbackSetMap = new HashMap<String,String>();
//        feedbackSetMap.put("pageTitle","意见反馈");
//        feedbackSetMap.put("themeColor","#de323a");
//        FeedbackAPI.setUICustomInfo(feedbackSetMap);
//        FeedbackAPI.setCustomContact("",true);
//        FeedbackAPI.getFeedbackUnreadCount(context, null, new IWxCallback() {
//            @Override
//            public void onSuccess(Object... objects) {
//
//            }
//
//            @Override
//            public void onError(int i, String s) {
//
//            }
//
//            @Override
//            public void onProgress(int i) {
//
//            }
//        });
//    }

    private String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }

    //----------------------------------------activity 管理 --------------------------------------------

   public Activity getTopActivity(){
       return allActivitys.lastElement();
   }

    // activity管理：添加activity到列表
    public void addActivity(Activity activity) {
        allActivitys.add(activity);
    }

    // activity管理：从列表中移除activity
    public void removeActivity(Activity activity) {
        if(activity != null){
            allActivitys.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    // 移除栈顶Activity
    public void removeTopActivity(){
        Activity activity = allActivitys.lastElement();
        removeActivity(activity);
    }

    // activity管理：结束所有activity
    public void finishAllActivity() {
        for (Activity activity : allActivitys) {
            if (null != activity) {
                activity.finish();
            }
        }
        allActivitys.clear();
    }

    // 结束线程,一般与finishAllActivity()一起使用
    // 例如: finishAllActivity;finishProgram();
    public void finishProgram() {
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
