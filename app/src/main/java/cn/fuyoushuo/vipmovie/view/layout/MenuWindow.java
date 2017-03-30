package cn.fuyoushuo.vipmovie.view.layout;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.zhy.android.percent.support.PercentLinearLayout;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.fuyoushuo.vipmovie.R;
import cn.fuyoushuo.vipmovie.ext.AppInfoManger;
import rx.functions.Action1;

/**
 * Created by QA on 2016/7/19.
 */
public class MenuWindow extends PopupWindow {

    //用来加载页面
    LayoutInflater layoutInflater;

    //popupwindow 所依附的组件
    View belowView;

    //内容所依附的view
    View contentView;

    //获取上下文资料
    WeakReference<Context> context;

    //半透明背景色
    View belowGroudView;

    OnItemClick onItemClick;

    //绑定元素
    @Bind(R.id.menu_no_pic_area)
    PercentLinearLayout noPicArea;

    @Bind(R.id.menu_hisormark_area)
    PercentLinearLayout historyArea;

    @Bind(R.id.menu_add_mark)
    PercentLinearLayout addMarkArea;

    @Bind(R.id.menu_refresh)
    PercentLinearLayout refreshArea;

    @Bind(R.id.menu_exit)
    PercentLinearLayout exitArea;

    @Bind(R.id.menu_download)
    PercentLinearLayout downloadArea;

    @Bind(R.id.no_pic_image)
    ImageView noPicImage;

    @Bind(R.id.down_area)
    RelativeLayout downArea;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public MenuWindow(Context context, View belowView) {
        super(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.context = new WeakReference<Context>(context);
        this.belowView = belowView;
        layoutInflater = LayoutInflater.from(context);
    }

    public MenuWindow init() {
        if(context.get() == null || ((Activity)context.get()).isFinishing()){
            return this;
        }
        contentView = layoutInflater.inflate(R.layout.layout_menu_view, null);
        //初始化各种组件
        ButterKnife.bind(this,contentView);

        if(AppInfoManger.getIntance().isNoPic()){
            noPicImage.setImageResource(R.mipmap.no_pic);
        }else{
            noPicImage.setImageResource(R.mipmap.has_pic);
        }

        RxView.clicks(noPicArea).throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        //进行取反操作
                        AppInfoManger.getIntance().setNoPic();
                        if(AppInfoManger.getIntance().isNoPic()){
                            noPicImage.setImageResource(R.mipmap.no_pic);
                        }else{
                            noPicImage.setImageResource(R.mipmap.has_pic);
                        }
                        if(onItemClick != null){
                            onItemClick.onNoPicClick();
                        }
                    }
                });

        RxView.clicks(historyArea).throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if(onItemClick != null){
                            onItemClick.onHisClick();
                        }
                        dismissWindow();
                    }
                });

        RxView.clicks(addMarkArea).throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if(onItemClick != null){
                            onItemClick.onAddMarkClick();
                        }
                        dismissWindow();
                    }
                });

        RxView.clicks(refreshArea).throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                         if(onItemClick != null){
                             onItemClick.onRefreshClick();
                         }
                         dismissWindow();
                    }
                });

        RxView.clicks(exitArea).throttleFirst(1000, TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                         if(onItemClick != null){
                             onItemClick.onExit();
                         }
                    }
                });

        RxView.clicks(downloadArea).throttleFirst(1000,TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if(onItemClick != null){
                            onItemClick.onDownload();
                        }
                        dismissWindow();
                    }
                });

        RxView.clicks(downArea).throttleFirst(1000,TimeUnit.MILLISECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        dismissWindow();
                    }
                });

        this.setContentView(contentView);
        this.setOutsideTouchable(false);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                dismissWindow();
                backgroundAlpha(1.0f);
            }
        });
        return this;
    }

    //展示window
    public void showWindow() {
        if(belowView == null || context.get() == null || ((Activity)context.get()).isFinishing()){
            return;
        }
        if (this.isShowing()) {
            this.dismiss();
        }
        this.setFocusable(true);
        ColorDrawable backgroundColor = new ColorDrawable(context.get().getResources().getColor(R.color.transparent));
        this.setBackgroundDrawable(backgroundColor);
        //防止虚拟软键盘被弹出菜单遮住
        this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        showAtLocation(belowView,Gravity.BOTTOM|Gravity.LEFT,0,0);
        backgroundAlpha(0.5f);
    }

    //关掉window
    public void dismissWindow() {
        if (context.get() == null) {
            return;
        }
        this.dismiss();
    }

    /**
     * 设置添加屏幕的背景透明度
     * @param alpha
     */
    private void backgroundAlpha(float alpha) {
        if(context.get() != null){
          Window window = ((FragmentActivity)(context.get())).getWindow();
          WindowManager.LayoutParams lp = window.getAttributes();
          lp.alpha = alpha; //0.0-1.0
          window.setAttributes(lp);
    }
    }




    //由外部实现
    public interface OnItemClick{

        //点击无图模式
        void onNoPicClick();

        //点击历史记录模式
        void onHisClick();

        //点击增加书签
        void onAddMarkClick();

        //点击共享
        void onRefreshClick();

        //点击退出
        void onExit();

        //点击下载管理
        void onDownload();
    }

    @Override
    public String toString() {
        return "MenuWindow{}";
    }
}
