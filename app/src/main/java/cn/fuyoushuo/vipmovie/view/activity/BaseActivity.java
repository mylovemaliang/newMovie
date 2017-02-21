package cn.fuyoushuo.vipmovie.view.activity;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;

import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import butterknife.ButterKnife;
import cn.fuyoushuo.vipmovie.MyApplication;


/**
 * @Package com.micky.commonproj.ui.activity
 * @Project CommonProj
 * @Description
 * @Author Micky Liu
 * @Email mickyliu@126.com
 * @Team KTEAM
 * @Date 2016-01-04 21:27
 */
public class BaseActivity extends RxAppCompatActivity {

    private boolean mAutoBindView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.getMyapplication().addActivity(this);
//        if(isTablet(this)){
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//        }else{
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        }
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void setContentView(int layoutResID) {
        setContentView(layoutResID, false);
    }

    public void setContentView(int layoutResID, boolean hideBackButton) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        mAutoBindView = true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getMyapplication().removeActivity(this);
        if(mAutoBindView){
            ButterKnife.unbind(this);
        }
        //MyApplication.getRefWatcher(MyApplication.getContext()).watch(this);
    }

    /**
     * 判断当前设备是手机还是平板，代码来自 Google I/O App for Android
     * @param context
     * @return 平板返回 True，手机返回 False
     */
    public static boolean isTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_LARGE;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
