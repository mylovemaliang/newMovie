package cn.fuyoushuo.vipmovie.presenter.impl;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.Display;
import android.view.View;

import java.lang.ref.WeakReference;

import cn.fuyoushuo.vipmovie.ext.BitmapManger;
import cn.fuyoushuo.vipmovie.ext.LocalFragmentManger;
import cn.fuyoushuo.vipmovie.ext.Pair;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by QA on 2017/2/27.
 */

public class TabPresenter extends BasePresenter{

    private WeakReference<Activity> context;

    //必须为activity实例
    public TabPresenter(Activity activity) {
        this.context = new WeakReference<Activity>(activity);
    }

    private Activity getContext(){
        return context.get();
    }

    //截图
    public  void captureScreen(final Integer fragmentId, final CaptureCallback captureCallback){
        Pair currentPair = LocalFragmentManger.getIntance().getFragment(fragmentId);
        if(getContext() == null || currentPair == null) return;
        mSubscriptions.add(createBitmapObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Bitmap>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                       if(captureCallback != null){
                           captureCallback.onError();
                       }
                    }

                    @Override
                    public void onNext(Bitmap bitmap) {
                        //保存bitmap
                        BitmapManger.getIntance().putBitmap(fragmentId,bitmap);
                        if(captureCallback != null){
                            captureCallback.onSuccess();
                        }
                    }
                })
        );

    }


    public interface CaptureCallback{

         void onSuccess();

         void onError();

    }



    private Observable<Bitmap> createBitmapObservable(){
         return Observable.create(new Observable.OnSubscribe<Bitmap>(){
             @Override
             public void call(Subscriber<? super Bitmap> subscriber) {
                 Activity activity = getContext();
                 View view = activity.getWindow().getDecorView();
                 view.buildDrawingCache();

                 // 获取状态栏高度
                 Rect rect = new Rect();
                 view.getWindowVisibleDisplayFrame(rect);
                 int statusBarHeights = rect.top;
                 Display display = activity.getWindowManager().getDefaultDisplay();

                 // 获取屏幕宽和高
                 int widths = display.getWidth();
                 int heights = display.getHeight();

                 // 允许当前窗口保存缓存信息
                 view.setDrawingCacheEnabled(true);

                 // 去掉状态栏
                 Bitmap bmp = Bitmap.createBitmap(view.getDrawingCache(), 0,
                         statusBarHeights, widths, heights - statusBarHeights);
                 // 销毁缓存信息
                 view.destroyDrawingCache();
                 subscriber.onNext(bmp);
             }
         });
    }
}
