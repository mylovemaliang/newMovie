package cn.fuyoushuo.vipmovie.presenter.impl;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.fuyoushuo.commonlib.utils.CommonUtils;
import cn.fuyoushuo.domain.entity.DownloadTask;
import cn.fuyoushuo.domain.greendao.DownloadTaskDao;
import cn.fuyoushuo.vipmovie.GreenDaoManger;
import cn.fuyoushuo.vipmovie.ext.DownloadManger;
import cn.fuyoushuo.vipmovie.po.DownloadDetail;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by QA on 2017/3/27.
 */

public class DownloadPresenter extends BasePresenter{


   //更新下载的VIEW
   public void updateDownloadView(DownloadTask downloadTask, final ProgressBar progressBar, final TextView downloadInfoText){
       mSubscriptions.add(createDownloadProgressObserver(downloadTask)
             .subscribeOn(Schedulers.io())
             .observeOn(AndroidSchedulers.mainThread())
             .subscribe(new Subscriber<DownloadTask>() {
                 @Override
                 public void onCompleted() {

                 }

                 @Override
                 public void onError(Throwable e) {

                 }

                 @Override
                 public void onNext(DownloadTask downloadTask) {
                    int progress = downloadTask.getProgress();
                    float totalMbs = downloadTask.getTotalMbs();
                    float currentMbs = downloadTask.getCurrentMbs();
                    int taskState = downloadTask.getTaskState();
                    String state = "";
                    String totalMbsString = String.valueOf(totalMbs)+"MB";
                    String currentMbsString = String.valueOf(currentMbs)+"MB";
                    if(taskState == DownloadDetail.DOWNLOAD_RUNNING){
                        state = "正在下载";
                    }
                    else if(taskState == DownloadDetail.DOWNLOAD_PAUSE){
                        state = "暂停";
                    }
                    else if(taskState == DownloadDetail.DOWNLOAD_COMPLETE){
                        state = "下载完成";
                    }
                    if(progressBar != null){
                        if(taskState == DownloadDetail.DOWNLOAD_COMPLETE){
                            progressBar.setVisibility(View.GONE);
                        }else{
                            progressBar.setProgress(progress);
                        }
                    }
                    if(downloadInfoText != null){
                        downloadInfoText.setText(currentMbsString+"/"+totalMbsString+" | "+state);
                    }
                 }
             })
       );
   }

   public void getAllDownloads(final FindAllDownloadCallback findAllDownloadCallback){
       mSubscriptions.add(createFindDownloadObserver()
               .subscribeOn(Schedulers.io())
               .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new Subscriber<List<DownloadTask>>() {
                   @Override
                   public void onCompleted() {

                   }

                   @Override
                   public void onError(Throwable e) {
                      if(findAllDownloadCallback != null){
                          findAllDownloadCallback.onAllDownloadsReturn(new ArrayList<DownloadTask>(),false);
                      }
                   }

                   @Override
                   public void onNext(List<DownloadTask> downloadTasks) {
                     if(findAllDownloadCallback != null){
                         findAllDownloadCallback.onAllDownloadsReturn(downloadTasks,true);
                     }
                   }
               })
       );
   }

    //----------------------------------------接口回调---------------------------------------------------------------------------

    public interface FindAllDownloadCallback{

        void onAllDownloadsReturn(List<DownloadTask> downloadTasks,boolean isOk);
    }




    //-------------------------------------------生产观察者-----------------------------------------------------------------------

    private Observable<List<DownloadTask>> createFindDownloadObserver(){
        return Observable.create(new Observable.OnSubscribe<List<DownloadTask>>() {
            @Override
            public void call(Subscriber<? super List<DownloadTask>> subscriber) {
                DownloadTaskDao downloadTaskDao = GreenDaoManger.getIntance().getmDaoSession().getDownloadTaskDao();
                List<DownloadTask> list = downloadTaskDao.queryBuilder().orderDesc(DownloadTaskDao.Properties.CreateTime).list();
                subscriber.onNext(list);
            }
        });
    }


    private Observable<DownloadTask> createDownloadProgressObserver(final DownloadTask downloadTask){
        return Observable.create(new Observable.OnSubscribe<DownloadTask>() {
            @Override
            public void call(Subscriber<? super DownloadTask> subscriber) {
                Long downloadId = downloadTask.getDownloadId();
                DownloadDetail downloadDetail = DownloadManger.getIntance().getDownloadProgress(downloadId);
                if(downloadDetail.getStatus() == DownloadDetail.DOWNLOAD_RUNNING){
                    downloadTask.setTaskState(DownloadDetail.DOWNLOAD_RUNNING);
                }
                else if(downloadDetail.getStatus() == DownloadDetail.DOWNLOAD_PAUSE){
                    downloadTask.setTaskState(DownloadDetail.DOWNLOAD_PAUSE);
                }
                else{
                    downloadTask.setTaskState(DownloadDetail.DOWNLOAD_COMPLETE);
                }
                downloadTask.setProgress(downloadDetail.getProgress());
                downloadTask.setCurrentMbs(downloadDetail.getCurrentMbs());
                downloadTask.setTotalMbs(downloadDetail.getTotalMbs());
                subscriber.onNext(downloadTask);
            }
        });
    }
}
