package cn.fuyoushuo.vipmovie.presenter.impl;

import android.text.TextUtils;
import android.text.style.UnderlineSpan;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.apache.log4j.or.ThreadGroupRenderer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.fuyoushuo.commonlib.utils.MD5;
import cn.fuyoushuo.domain.entity.BookMark;
import cn.fuyoushuo.domain.entity.HistoryItem;
import cn.fuyoushuo.domain.entity.UserTrack;
import cn.fuyoushuo.domain.greendao.BookMarkDao;
import cn.fuyoushuo.domain.greendao.HistoryItemDao;
import cn.fuyoushuo.domain.greendao.UserTrackDao;
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

    public SearchPresenter() {
    }

    private ISearchView getMyView(){
        return searchView.get();
    }




    //-----------------------------------------历史记录相关---------------------------------------------

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
    public void delHistory(HistoryItem historyItem,final DeleteHistoryCallback deleteHistoryCallback){
        if (historyItem == null || historyItem.getId() == null) return;
        mSubscriptions.add(createDelHisObserver(historyItem)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(deleteHistoryCallback != null){
                            deleteHistoryCallback.onDeleteHistory(false);
                        }
                    }

                    @Override
                    public void onNext(Boolean isOk) {
                        if(deleteHistoryCallback != null){
                            deleteHistoryCallback.onDeleteHistory(true);
                        }
                    }
                })
        );
    }

    /**
     * 更新历史记录
     * @param id
     * @param title
     */
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
                                 historyItem.setInputMd5(MD5.MD5Encode(item1));
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


    //----------------------------------------------------------------------------------------------

    /**
     * 获取所有的历史记录
     * @param findAllHisCallback
     */
    public void findAllHistorys(final FindAllHisCallback findAllHisCallback){
        mSubscriptions.add(createFindHisObserver()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<HistoryItem>>() {

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(findAllHisCallback != null){
                            findAllHisCallback.onFindAllHistorys(new ArrayList<HistoryItem>(),false);
                        }
                    }

                    @Override
                    public void onNext(List<HistoryItem> historyItems) {
                        if(findAllHisCallback != null){
                            findAllHisCallback.onFindAllHistorys(historyItems,true);
                        }
                    }
                })
        );
    }

    //-----------------------------------------书签记录-----------------------------------------------

    /**
     * 获取所有的书签记录
     * @param findAllBookMarkCallback
     */
    public void findAllBookmarks(final FindAllBookMarkCallback findAllBookMarkCallback){
         mSubscriptions.add(createFindBookmarkObserver()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<BookMark>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        if(findAllBookMarkCallback != null){
                            findAllBookMarkCallback.onFindAllBookmarks(new ArrayList<BookMark>(),false);
                        }
                    }

                    @Override
                    public void onNext(List<BookMark> bookMarks) {
                        if(findAllBookMarkCallback != null){
                            findAllBookMarkCallback.onFindAllBookmarks(bookMarks,true);
                        }
                    }
                })

         );
    }

    /**
     * 删除书签
     * @param bookMark
     * @param deleteBookmarkCallback
     */
    public void deleteBookMark(BookMark bookMark,final DeleteBookmarkCallback deleteBookmarkCallback){
        mSubscriptions.add(createDelMarkObserver(bookMark)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable e) {
                   if(deleteBookmarkCallback != null){
                       deleteBookmarkCallback.onDeleteBookMark(false);
                   }
                }

                @Override
                public void onNext(Boolean aBoolean) {
                    if(deleteBookmarkCallback != null){
                        deleteBookmarkCallback.onDeleteBookMark(true);
                    }
                }
            })

        );
    }

    /**
     * 添加书签
     * @param bookMark
     * @param addBookmarkCallback
     */
    public void addBookmark(final BookMark bookMark, final AddBookmarkCallback addBookmarkCallback){
        mSubscriptions.add(createAddBookmarkObserver(bookMark)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new Subscriber<BookMark>() {
                  @Override
                  public void onCompleted() {

                  }

                  @Override
                  public void onError(Throwable e) {
                       if(addBookmarkCallback != null){
                           addBookmarkCallback.onAddBookMark(bookMark,false);
                       }
                  }

                  @Override
                  public void onNext(BookMark bookMark) {
                       if(addBookmarkCallback != null){
                           addBookmarkCallback.onAddBookMark(bookMark,true);
                       }
                  }
              })
        );
    }

    //---------------------------------------------用户踪迹相关---------------------------------------------

    /**
     * 添加用户踪迹
     * @param userTrack
     * @param addUserTrackCallback
     */
    public void addUserTrack(final UserTrack userTrack, final AddUserTrackCallback addUserTrackCallback){
        mSubscriptions.add(createAddUserTrackObserver(userTrack)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(new Subscriber<UserTrack>() {
                  @Override
                  public void onCompleted() {

                  }

                  @Override
                  public void onError(Throwable e) {
                    if(addUserTrackCallback != null){
                        addUserTrackCallback.onAddUserTrack(userTrack,false);
                    }
                  }

                  @Override
                  public void onNext(UserTrack userTrack) {
                     if(addUserTrackCallback != null){
                         addUserTrackCallback.onAddUserTrack(userTrack,true);
                     }
                  }
              })

        );
  }

    /**
     * 查询所有的用户踪迹
     * @param findAllUtCallback
     */
    public void findAllUserTrack(final FindAllUtCallback findAllUtCallback){
        mSubscriptions.add(createFindUserTrackObserver()
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
           .subscribe(new Subscriber<List<UserTrack>>() {
               @Override
               public void onCompleted() {

               }

               @Override
               public void onError(Throwable e) {
                   if(findAllUtCallback != null){
                       findAllUtCallback.onFindAllUserTrack(new ArrayList<UserTrack>(),false);
                   }
               }

               @Override
               public void onNext(List<UserTrack> userTracks) {
                   if(findAllUtCallback != null){
                       findAllUtCallback.onFindAllUserTrack(userTracks,true);
                   }
               }
           })
        );
    }

    /**
     * 删除所有的用户踪迹
      * @param deleteAllUtCallback
     */
    public void deleteAllUserTrack(final DeleteAllUtCallback deleteAllUtCallback){
        mSubscriptions.add(createDeleteAllUserTrackObserver()
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe(new Subscriber<Boolean>() {
                 @Override
                 public void onCompleted() {

                 }

                 @Override
                 public void onError(Throwable e) {
                    if(deleteAllUtCallback != null){
                        deleteAllUtCallback.onDeleteAllUserTrack(false);
                    }
                 }

                 @Override
                 public void onNext(Boolean aBoolean) {
                     if(deleteAllUtCallback != null){
                         deleteAllUtCallback.onDeleteAllUserTrack(true);
                     }
                 }
             })
        );
    }

    //-------------------------------------------------------------------------------------------


    //-----------------------------------自定义接口------------------------------------------------

    public interface addHistoryCallback{

        void onAddCallBack(Boolean isOk);

    }

    public interface FindAllHisCallback{

        void onFindAllHistorys(List<HistoryItem> result,boolean isOk);

    }

    public interface FindAllBookMarkCallback{

        void onFindAllBookmarks(List<BookMark> result,boolean isOk);
    }

    public interface DeleteHistoryCallback{

         void onDeleteHistory(boolean isOk);
    }

    public interface DeleteBookmarkCallback{

         void onDeleteBookMark(boolean isOk);

    }

    public interface AddBookmarkCallback{

        void onAddBookMark(BookMark bookMark,boolean isOk);

    }

    public interface AddUserTrackCallback{

        void onAddUserTrack(UserTrack userTrack,boolean isOk);
    }

    public interface FindAllUtCallback{

        void onFindAllUserTrack(List<UserTrack> result,boolean isOk);
    }

    public interface DeleteAllUtCallback{

        void onDeleteAllUserTrack(boolean isOk);
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
                //删除旧的相同的历史记录
                historyItemDao.queryBuilder().where(HistoryItemDao.Properties.InputMd5.eq(historyItem.getInputMd5())).buildDelete().executeDeleteWithoutDetachingEntities();
                long count = historyItemDao.count();
                if(count >= 20){
                    //删除最旧的历史记录
                    historyItemDao.queryBuilder().orderAsc(HistoryItemDao.Properties.Id).limit(1).buildDelete().executeDeleteWithoutDetachingEntities();
                }
                long insert = historyItemDao.insert(historyItem);
                historyItem.setId(insert);
                subscriber.onNext(historyItem);
            }

     });
    }

    /**
     * 添加书签
     * @param bookMark
     * @return
     */
    private Observable<BookMark> createAddBookmarkObserver(final BookMark bookMark){
       return Observable.create(new Observable.OnSubscribe<BookMark>() {
           @Override
           public void call(Subscriber<? super BookMark> subscriber) {
               BookMarkDao bookMarkDao = GreenDaoManger.getIntance().getmDaoSession().getBookMarkDao();
               //删除旧的相同的书签记录
               bookMarkDao.queryBuilder().where(BookMarkDao.Properties.Urlmd5.eq(bookMark.getUrlmd5())).buildDelete().executeDeleteWithoutDetachingEntities();
               //插入新的书签记录
               long insert = bookMarkDao.insert(bookMark);
               bookMark.setId(insert);
               subscriber.onNext(bookMark);
           }
       });
    }

    /**
     * 删除所有历史记录
     * @param
     * @return
     */
    private Observable<Boolean> createDelAllHisObserver(){
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

    private Observable<Boolean> createDelHisObserver(final HistoryItem historyItem){
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                GreenDaoManger.getIntance().getmDaoSession().getHistoryItemDao().delete(historyItem);
                subscriber.onNext(true);
            }
        });
    }

    private Observable<Boolean> createDelMarkObserver(final BookMark bookMark){
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                GreenDaoManger.getIntance().getmDaoSession().getBookMarkDao().delete(bookMark);
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

    //获取所有的书签
    private Observable<List<BookMark>> createFindBookmarkObserver(){
        return Observable.create(new Observable.OnSubscribe<List<BookMark>>() {
            @Override
            public void call(Subscriber<? super List<BookMark>> subscriber) {
                BookMarkDao bookMarkDao = GreenDaoManger.getIntance().getmDaoSession().getBookMarkDao();
                List<BookMark> list = bookMarkDao.queryBuilder().list();
                subscriber.onNext(list);
            }
        });
    }

    //添加用户踪迹
    public Observable<UserTrack> createAddUserTrackObserver(final UserTrack userTrack){
        return Observable.create(new Observable.OnSubscribe<UserTrack>() {
            @Override
            public void call(Subscriber<? super UserTrack> subscriber) {
                UserTrackDao userTrackDao = GreenDaoManger.getIntance().getmDaoSession().getUserTrackDao();
                userTrackDao.queryBuilder().where(UserTrackDao.Properties.Md5Url.eq(userTrack.getMd5Url())).buildDelete().executeDeleteWithoutDetachingEntities();
                long insert = userTrackDao.insert(userTrack);
                userTrack.setId(insert);
                subscriber.onNext(userTrack);
            }
        });
    }

    //获取所有的用户踪迹记录
    private Observable<List<UserTrack>> createFindUserTrackObserver(){
        return Observable.create(new Observable.OnSubscribe<List<UserTrack>>() {
            @Override
            public void call(Subscriber<? super List<UserTrack>> subscriber) {
                UserTrackDao userTrackDao = GreenDaoManger.getIntance().getmDaoSession().getUserTrackDao();
                List<UserTrack> result = userTrackDao.queryBuilder().orderDesc(UserTrackDao.Properties.CreateTime).list();
                subscriber.onNext(result);
            }
        });
    }

    //删除所有的用户踪迹记录
    private Observable<Boolean> createDeleteAllUserTrackObserver(){
        return Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                UserTrackDao userTrackDao = GreenDaoManger.getIntance().getmDaoSession().getUserTrackDao();
                userTrackDao.deleteAll();
                subscriber.onNext(true);
            }
        });
    }
}
