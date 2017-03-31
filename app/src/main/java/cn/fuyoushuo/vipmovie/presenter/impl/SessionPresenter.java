package cn.fuyoushuo.vipmovie.presenter.impl;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.github.lazylibrary.util.Base64;

import java.io.IOException;
import java.util.Date;

import cn.fuyoushuo.commonlib.utils.ClientReturnEncoder;
import cn.fuyoushuo.commonlib.utils.Constants;
import cn.fuyoushuo.commonlib.utils.MD5;
import cn.fuyoushuo.domain.httpservice.VipkdyHttpService;
import cn.fuyoushuo.vipmovie.ServiceManager;
import cn.fuyoushuo.vipmovie.ext.AppInfoManger;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
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
        mSubscriptions.add(ServiceManager.createService(VipkdyHttpService.class)
             .getSession()
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe(new Subscriber<JSONObject>() {
                 @Override
                 public void onCompleted() {

                 }

                 @Override
                 public void onError(Throwable e) {
                    if(sessionGetCallback != null){
                        sessionGetCallback.onGetSession("","",false);
                    }
                 }

                 @Override
                 public void onNext(JSONObject httpResp) {
                     if(sessionGetCallback == null) return;
                     if(httpResp != null && httpResp.getIntValue("s") == 1){
                         JSONObject result = httpResp.getJSONObject("r");
                         if(result == null){
                             sessionGetCallback.onGetSession("","",false);
                         }else{
                             String sessionId = result.getString("sessionId");
                             String token = result.getString("token");
                             sessionGetCallback.onGetSession(sessionId,token,true);
                         }
                     }else {
                         sessionGetCallback.onGetSession("","",false);
                     }
                 }
             }));
    }

    public void getDataFromJs(Integer action, final String input, final DataGetCallback dataGetCallback){
        if(action == null) return;
        mSubscriptions.add(Observable.just(action)
                   .flatMap(new Func1<Integer, Observable<String>>() {
                       @Override
                       public Observable<String> call(Integer action) {
                           if(action == 1){
                               return createCookieGetObserver(input);
                           }
                           else if(action == 2){
                               return createFreeResolveDataBackObserver(input);
                           }
                           else if(action == 3){
                               return createCloudResolveObserver(input);
                           }
                           else{
                               return Observable.just("_error_");
                           }
                       }
                   })
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
                     public void onNext(String result) {
                        if(dataGetCallback == null)
                            if("_error_".equals(result)){
                            dataGetCallback.onGetData("",false);
                        }
                        dataGetCallback.onGetData(result,true);
                     }
                 }));
    }




    //------------------------------------回调接口------------------------------------------------------

    public interface SessionGetCallback{

        void onGetSession(String sessionId,String token,boolean isOk);

    }

    public interface DataGetCallback{

        void onGetData(String result,boolean isOk);
    }


    //-----------------------------------回调接口-------------------------------------------------------

//    private Observable<String> createDateBackObserver(final String url){
//        return Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                Request request = new Request.Builder()
//                        .url(url)
//                        .build();
//                try {
//                    Response execute = okHttpClient.newCall(request).execute();
//                    String result = execute.body().string();
//                    result = result.trim().replace("\n","").replace("\t","");
//                    //String finalResult = CommonUtils.string2JsJson(result);
//                    //result = result.replace("\"", "&&&");
//                    //result = URLEncoder.encode(result, "utf8");
//                    result = Base64.encodeToString(result.getBytes("UTF-8"), true);
//                    subscriber.onNext(result);
//                } catch (IOException e) {
//                    subscriber.onError(e);
//                }
//            }
//        }) ;
//    }

    private Observable<String> createCookieGetObserver(final String input){
       return Observable.create(new Observable.OnSubscribe<String>() {
           @Override
           public void call(Subscriber<? super String> subscriber) {
              if(TextUtils.isEmpty(input)){
                  subscriber.onError(new RuntimeException("no input"));
              }
              String param = "";
              AppInfoManger.SessionPair sessionPair = AppInfoManger.getIntance().getVipCookieSession();
              String sessionId = sessionPair.getSessionId();
              String token = sessionPair.getToken();
              String t = String.valueOf(new Date().getTime());
              String mt = MD5.MD5Encode(t+token+"vsc_@!_m_d_5");
              param += "&sessionid="+sessionId+"&t="+t+"&mc="+mt;
              StringBuffer sb = new StringBuffer();
              sb.append(Constants.CookieGetUrl);
              sb.append("?");
              sb.append(input);
              sb.append(param);
              String requestUrl = sb.toString();
              Request request = new Request.Builder()
                       .url(requestUrl)
                       .build();
              try {
                   Response execute = okHttpClient.newCall(request).execute();
                   String result = execute.body().string();
                   result = result.trim().replace("\n","").replace("\t","");
                   //String finalResult = CommonUtils.string2JsJson(result);
                   //result = result.replace("\"", "&&&");
                   //result = URLEncoder.encode(result, "utf8");
                   result = Base64.encodeToString(result.getBytes("UTF-8"),false);
                   subscriber.onNext(result);
               } catch (IOException e) {
                   subscriber.onError(e);
               }
           }
       });
    }


    private Observable<String> createCloudResolveObserver(final String input){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String url = Constants.CloudResovleUrl+"?"+input;
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    Response execute = okHttpClient.newCall(request).execute();
                    String result = execute.body().string();
                    result = result.trim().replace("\n","").replace("\t","");
                    //String finalResult = CommonUtils.string2JsJson(result);
                    //result = result.replace("\"", "&&&");
                    //result = URLEncoder.encode(result, "utf8");
                    result = Base64.encodeToString(result.getBytes("UTF-8"),false);
                    subscriber.onNext(result);
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }

    private Observable<String> createFreeResolveDataBackObserver(final String input){
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                String encodeString = ClientReturnEncoder.getEncodeString(input);
                String url = Constants.FreeResovleUrl+encodeString;
                Log.d("get data url",url);
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                try {
                    Response execute = okHttpClient.newCall(request).execute();
                    String result = execute.body().string();
                    result = result.trim().replace("\n","").replace("\t","");
                    result = Base64.encodeToString(result.getBytes("UTF-8"),false);
                    subscriber.onNext(result);
                } catch (IOException e) {
                    subscriber.onError(e);
                }
            }
        });
    }
}
