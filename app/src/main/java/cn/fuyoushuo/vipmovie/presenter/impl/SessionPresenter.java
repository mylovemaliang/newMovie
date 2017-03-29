package cn.fuyoushuo.vipmovie.presenter.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.facebook.stetho.server.AddressNameHelper;

import java.io.IOException;
import java.util.Map;

import cn.fuyoushuo.commonlib.utils.ClientReturnEncoder;
import cn.fuyoushuo.commonlib.utils.Constants;
import cn.fuyoushuo.domain.entity.HttpResp;
import cn.fuyoushuo.domain.httpservice.VipwwwHttpService;
import cn.fuyoushuo.vipmovie.ServiceManager;
import cn.fuyoushuo.vipmovie.ext.AppInfoManger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by QA on 2017/3/28.
 */

public class SessionPresenter extends BasePresenter{

    private OkHttpClient okHttpClient;

    public SessionPresenter() {
        okHttpClient = new OkHttpClient();
    }

    public void getSessionForCookie(final SessionGetCallback sessionGetCallback){
        mSubscriptions.add(ServiceManager.createService(VipwwwHttpService.class)
             .getSession()
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe(new Subscriber<HttpResp>() {
                 @Override
                 public void onCompleted() {

                 }

                 @Override
                 public void onError(Throwable e) {
                    if(sessionGetCallback != null){
                        sessionGetCallback.onGetSession("",false);
                    }
                 }

                 @Override
                 public void onNext(HttpResp httpResp) {
                     String result = JSON.toJSONString(httpResp);
                     if(sessionGetCallback != null){
                         sessionGetCallback.onGetSession(result,true);
                     }
                 }
             }));
    }

    public String getSessionFromLocal(){
        return AppInfoManger.getIntance().getVipCookieSession();
    }

    public void getDataFromJs(String url, final DataGetCallback dataGetCallback){
        mSubscriptions.add(createDateBackObserver(url)
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Subscriber<String>() {
                     @Override
                     public void onCompleted() {

                     }

                     @Override
                     public void onError(Throwable e) {
                        if(dataGetCallback != null){
                            dataGetCallback.onGetData("",false);
                        }
                     }

                     @Override
                     public void onNext(String s) {
                        if(dataGetCallback != null){
                            dataGetCallback.onGetData(s,true);
                        }
                     }
                 }));
    }

    public void getDataFromFreeResolve(final String input,final FreeResolveCallback freeResolveCallback){
        mSubscriptions.add(createFreeResolveDataBackObserver(input)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<String>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                  if(freeResolveCallback != null){
                      freeResolveCallback.onGetFreeResolve("",false);
                  }
                }

                @Override
                public void onNext(String s) {
                  if(freeResolveCallback != null){
                      freeResolveCallback.onGetFreeResolve(s,true);
                  }
                }
            })

        );
    }


    //------------------------------------回调接口------------------------------------------------------

    public interface SessionGetCallback{

        void onGetSession(String sessionResult,boolean isOk);

    }

    public interface DataGetCallback{

        void onGetData(String result,boolean isOk);
    }

    public interface FreeResolveCallback{

        void onGetFreeResolve(String result,boolean isOk);
    }


    //-----------------------------------回调接口-------------------------------------------------------

    private Observable<String> createDateBackObserver(final String url){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    Response execute = okHttpClient.newCall(request).execute();
                    String result = execute.body().string();
                    subscriber.onNext(result);
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        }) ;
    }

    private Observable<String> createFreeResolveDataBackObserver(final String input){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String encodeString = ClientReturnEncoder.getEncodeString(input);
                String url = Constants.FreeResovleUrl+encodeString;
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    Response execute = okHttpClient.newCall(request).execute();
                    String result = execute.body().string();
                    result = result.trim().replace("\n","").replace("\t","");
                    subscriber.onNext(result);
                } catch (IOException e) {
                    subscriber.onError(e);
                }


            }
        });
    }
}
