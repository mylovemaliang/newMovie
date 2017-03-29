package cn.fuyoushuo.vipmovie.presenter.impl;

import android.animation.FloatArrayEvaluator;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPObject;
import com.github.lzyzsd.jsbridge.BridgeUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.fuyoushuo.domain.entity.FGoodItem;
import cn.fuyoushuo.domain.entity.HttpResp;
import cn.fuyoushuo.domain.entity.NewItem;
import cn.fuyoushuo.domain.entity.SiteItem;
import cn.fuyoushuo.domain.httpservice.FqbbHttpService;
import cn.fuyoushuo.domain.httpservice.NewsHttpService;
import cn.fuyoushuo.domain.httpservice.VipkdyHttpService;
import cn.fuyoushuo.vipmovie.MyApplication;
import cn.fuyoushuo.vipmovie.ServiceManager;
import cn.fuyoushuo.vipmovie.view.iview.IMainView;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by QA on 2017/2/24.
 */

public class MainPresenter extends BasePresenter {

    private IMainView mainView;

    public MainPresenter(IMainView mainView) {
        this.mainView = mainView;
    }


    /**
     * 获取商品信息
     * @param cateId
     * @param page
     */
    public void getFGoods(final Long cateId, final Integer page,final boolean isRefresh){
        mSubscriptions.add(ServiceManager.createService(FqbbHttpService.class).getGoodItems(cateId,20,page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HttpResp>() {
                    @Override
                    public void onCompleted() {
                        return;
                    }
                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(MyApplication.getContext(),"网速稍慢,请等待",Toast.LENGTH_SHORT).show();
                        mainView.setupFgoodsView(1,cateId,new ArrayList<FGoodItem>(),isRefresh);
                    }
                    @Override
                    public void onNext(HttpResp httpResp) {
                        if(httpResp.getS() == 1){
                            Object result = httpResp.getR();
                            JSONArray jsonArray = new JSONArray((List<Object>) result);
                            List<FGoodItem> goodItems = new ArrayList<FGoodItem>();
                            for(Object item : jsonArray){
                                JSONObject jobject = new JSONObject((Map<String, Object>) item);
                                goodItems.add(jobject.toJavaObject(FGoodItem.class));
                            }
                            mainView.setupFgoodsView(page,cateId,goodItems,isRefresh);
                        }else{
                            Toast.makeText(MyApplication.getContext(),httpResp.getM(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }));
    }

    /**
     *
     * @param type 新闻类型
     * @param startKey 初始新闻标识
     * @param isNext 是否请求下一页(当true时,startKey不能为空)
     */
    public void getNews(final String type, final String startKey, final boolean isNext){
         mSubscriptions.add(Observable.just(isNext)
             .flatMap(new Func1<Boolean, Observable<JSONObject>>() {
                 @Override
                 public Observable<JSONObject> call(Boolean isNext) {
                     if(!isNext){
                         return ServiceManager.createService(NewsHttpService.class).lastestNews(type);
                     }else{
                         return ServiceManager.createService(NewsHttpService.class).nextNews(type,startKey);
                     }
                 }
             })
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe(new Subscriber<JSONObject>() {
                 @Override
                 public void onCompleted() {

                 }

                 @Override
                 public void onError(Throwable e) {
                     Log.d("getNews callback",e.getMessage());
                     mainView.setupNewsView(new ArrayList<NewItem>(),type,isNext,false);
                 }

                 @Override
                 public void onNext(JSONObject result) {
                      if(result == null || result.isEmpty()){
                          // TODO: 2017/3/2 没数据
                            mainView.setupNewsView(new ArrayList<NewItem>(),type,isNext,false);
                      }else{
                          if(result.getIntValue("stat") != 1){
                              // TODO: 2017/3/2 状态异常
                            mainView.setupNewsView(new ArrayList<NewItem>(),type,isNext,false);
                          }else{
                              JSONArray data = result.getJSONArray("data");
                              List<NewItem> newItems = parseNewsArray(data);
                              mainView.setupNewsView(newItems,type,isNext,true);
                          }
                      }
                 }
             })
         );
    }

    /**
     * 获取头部网站信息
     */
    public void getHeadSites(){
        mSubscriptions.add(ServiceManager.createService(VipkdyHttpService.class)
             .getHomeSiteInfo()
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe(new Subscriber<List<SiteItem>>() {
                 @Override
                 public void onCompleted() {

                 }

                 @Override
                 public void onError(Throwable e) {
                    mainView.setupHeadSites(new ArrayList<SiteItem>(),false);
                 }

                 @Override
                 public void onNext(List<SiteItem> siteItems) {
                     mainView.setupHeadSites(siteItems,true);
                 }
             })
        );
    }

    //解析数组
    private List<NewItem> parseNewsArray(JSONArray array){
        List<NewItem> newItems = new ArrayList<NewItem>();
        if(array.isEmpty()){
            return newItems;
        }
        for(int i=0;i<array.size();i++){
            JSONObject currentObject = array.getJSONObject(i);
            NewItem item = new NewItem();
            int imgsSize = currentObject.getIntValue("miniimg_size");
            if(!(1 == imgsSize || 3 == imgsSize)){
                continue;
            }
            item.setImageSize(imgsSize);
            item.setTopic(currentObject.getString("topic"));
            item.setSource(currentObject.getString("source"));
            item.setNewUrl(currentObject.getString("url"));
            item.setRowKey(currentObject.getString("rowkey"));
            JSONArray miniimgs = currentObject.getJSONArray("miniimg");
            for(int j=0;j<miniimgs.size();j++){
                JSONObject imageObject = miniimgs.getJSONObject(j);
                item.getImageUrls().add(imageObject.getString("src"));
            }
            newItems.add(item);
        }
        return newItems;
    }


    //获取首页图标信息
    private Observable<List<SiteItem>> createSitesObserver(){
        return Observable.create(new OnSubscribe<List<SiteItem>>() {
            @Override
            public void call(Subscriber<? super List<SiteItem>> subscriber) {
                 List<SiteItem> siteItems = new ArrayList<SiteItem>();
                 String sites = BridgeUtil.assetFile2Str(MyApplication.getContext(), "sites");
                 siteItems = JSONObject.parseArray(sites, SiteItem.class);
                 subscriber.onNext(siteItems);
            }
        });


    }





}
