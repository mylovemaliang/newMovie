package cn.fuyoushuo.vipmovie.presenter.impl;

import android.text.TextUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.fuyoushuo.domain.entity.HistoryItem;
import cn.fuyoushuo.domain.greendao.HistoryItemDao;
import cn.fuyoushuo.domain.httpservice.BaiduHttpService;
import cn.fuyoushuo.vipmovie.GreenDaoManger;
import cn.fuyoushuo.vipmovie.ServiceManager;
import cn.fuyoushuo.vipmovie.view.iview.ISearchView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by QA on 2017/3/9.
 */

public class SearchPresenter extends BasePresenter {

    private WeakReference<ISearchView> searchView;

    public SearchPresenter(ISearchView searchView) {
        this.searchView = new WeakReference<ISearchView>(searchView);
    }

    private ISearchView getMyView(){
        return searchView.get();
    }

    /**
     * 获取所有的历史记录
     */
    public void getAllHistory(){
      mSubscriptions.add(createFindHisObserver()
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Subscriber<List<HistoryItem>>() {

                     @Override
                     public void onCompleted() {

                     }

                     @Override
                     public void onError(Throwable e) {
                         if(getMyView() != null){
                             getMyView().setHistoryItems(new ArrayList<HistoryItem>(),false);
                         }
                     }

                     @Override
                     public void onNext(List<HistoryItem> historyItems) {
                         if(getMyView() != null){
                              getMyView().setHistoryItems(historyItems,true);
                         }
                     }
                 })
      );
    }

    /**
     * 新增历史记录
     */
    public void addHistory(HistoryItem historyItem, final addHistoryCallback addHistoryCallback){
        mSubscriptions.add(createAddHisObserver(historyItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HistoryItem>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(addHistoryCallback != null){
                            addHistoryCallback.onAddCallBack(false);
                        }
                    }

                    @Override
                    public void onNext(HistoryItem historyItem) {
                        if(addHistoryCallback != null){
                            addHistoryCallback.onAddCallBack(true);
                        }
                    }
                })
        );
    }


    /**
     * 删除历史记录
     */
    public void delHistory(HistoryItem historyItem){
        if (historyItem == null || historyItem.getId() == null) return;
        mSubscriptions.add(createDelHisObserver()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(getMyView() != null){
                            getMyView().onDelHistoryItem(false);
                        }
                    }

                    @Override
                    public void onNext(Boolean isOk) {
                        if(getMyView() != null){
                            getMyView().onDelHistoryItem(true);
                        }
                    }
                })
        );
    }

    public static void updateHistoryTitle(Long id,String title){
        createUpdateHistoryObservere(id,title)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .subscribe();
    }

    /**
     * 查询关键字
     * @param keyword
     */
    public void searchKeyWord(String keyword){
        if(TextUtils.isEmpty(keyword)) return;
        Observable<List<HistoryItem>> searchHisObserver = createSearchHisObserver(keyword);
        Observable<JSONObject> searchBaiduObserver = ServiceManager.createService(BaiduHttpService.class).getBaiduKeyWords(keyword);
        mSubscriptions.add(Observable.merge(searchHisObserver,searchBaiduObserver)
                 .subscribeOn(Schedulers.io())
                 .observeOn(AndroidSchedulers.mainThread())
                 .subscribe(new Subscriber<Object>() {
                     @Override
                     public void onCompleted() {

                     }

                     @Override
                     public void onError(Throwable e) {
                       if(getMyView() != null){
                           getMyView().setHistorySearchItems(new ArrayList<HistoryItem>(),false);
                       }
                     }

                     @Override
                     public void onNext(Object object) {
                         List<HistoryItem> resultList = new ArrayList<HistoryItem>();
                         if(object instanceof List){
                             List<HistoryItem> list = (List<HistoryItem>) object;
                             resultList.addAll(list);
                         }
                         else if(object instanceof JSONObject){
                             if(object == null) return;
                             JSONObject resultObject = (JSONObject) object;
                             if(resultObject.containsKey("s"));
                             JSONArray array = resultObject.getJSONArray("s");
                             for(Object item : array){
                                 String item1 = (String) item;
                                 HistoryItem historyItem = new HistoryItem();
                                 historyItem.setHistoryTitle(item1);
                                 historyItem.setHistoryType(2);
                                 resultList.add(historyItem);
                             }
                         }
                         if(getMyView() != null){
                             getMyView().setHistorySearchItems(resultList,true);
                         }
                     }
                 })

        );
    }


    //-----------------------------------自定义接口------------------------------------------------

    public interface addHistoryCallback{

        void onAddCallBack(Boolean isOk);

    }



    //------------------------------------创建观察者------------------------------------------------

    /**
     *  获取所有的历史记录,按插入事件倒序显示,最大显示20条
      * @return 数据库里所有的历史记录
     */
   private Observable<List<HistoryItem>> createFindHisObserver(){
       return Observable.create(new Observable.OnSubscribe<List<HistoryItem>>() {
           @Override
           public void call(Subscriber<? super List<HistoryItem>> subscriber) {
               HistoryItemDao historyItemDao = GreenDaoManger.getIntance().getmDaoSession().getHistoryItemDao();
               List<HistoryItem> list = historyItemDao.queryBuilder().orderDesc(HistoryItemDao.Properties.CreateTime).limit(20).list();
               subscriber.onNext(list);
           }
       });
   }

    /**
     * 新增历史记录
     * @param historyItem
     * @return
     */
    private Observable<HistoryItem> createAddHisObserver(final HistoryItem historyItem){
        return Observable.create(new Observable.OnSubscribe<HistoryItem>() {
            @Override
            public void call(Subscriber<? super HistoryItem> subscriber) {
                HistoryItemDao historyItemDao = GreenDaoManger.getIntance().getmDaoSession().getHistoryItemDao();
                long insert = historyItemDao.insert(historyItem);
                historyItem.setId(insert);
                subscriber.onNext(historyItem);
        }
     });
    }

    /**
     * 删除所有历史记录
     * @param
     * @return
     */
    private Observable<Boolean> createDelHisObserver(){
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
               GreenDaoManger.getIntance()
                             .getmDaoSession()
                             .getHistoryItemDao()
                             .deleteAll();
               subscriber.onNext(true);
            }
        });
    }

    private Observable<List<HistoryItem>> createSearchHisObserver(final String key){
        return Observable.create(new Observable.OnSubscribe<List<HistoryItem>>() {
            @Override
            public void call(Subscriber<? super List<HistoryItem>> subscriber) {
                HistoryItemDao historyItemDao = GreenDaoManger.getIntance().getmDaoSession().getHistoryItemDao();
                List<HistoryItem> list = historyItemDao.queryBuilder().where(HistoryItemDao.Properties.HistoryTitle.like(key)).limit(20).list();
                subscriber.onNext(list);
            }
        });
    }

    private static Observable<Boolean> createUpdateHistoryObservere(final Long id,final String title){
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                HistoryItemDao historyItemDao = GreenDaoManger.getIntance().getmDaoSession().getHistoryItemDao();
                HistoryItem load = historyItemDao.load(id);
                if(load != null){
                    load.setHistoryTitle(title);
                    historyItemDao.update(load);
                }
            }
        });
    }
}
