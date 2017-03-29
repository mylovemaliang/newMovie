package cn.fuyoushuo.vipmovie.ext;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import org.jsoup.helper.DataUtil;

import java.util.Date;

import cn.fuyoushuo.commonlib.utils.DateUtils;
import cn.fuyoushuo.domain.entity.DownloadTask;
import cn.fuyoushuo.domain.greendao.DownloadTaskDao;
import cn.fuyoushuo.vipmovie.GreenDaoManger;
import cn.fuyoushuo.vipmovie.po.DownloadDetail;

/**
 * Created by QA on 2017/3/20.
 */

public class DownloadManger {

    private Context context;

    private DownloadManager downloadManager;

    private static class DownloadMangerHolder{
        private static DownloadManger INTANCE = new DownloadManger();
    }

    public static DownloadManger getIntance(){
        return DownloadMangerHolder.INTANCE;
    }

    public void initContext(Context context){
        this.context = context;
        this.downloadManager = (DownloadManager) this.context.getSystemService(Context.DOWNLOAD_SERVICE);
    }

    /**
     * 提交任务
     * @param taskurl
     * @param title
     */
    public void submitTask(String taskurl,String title,String mineType){
        if(TextUtils.isEmpty(taskurl)) return;
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(taskurl));
        request.setTitle(title);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setMimeType(mineType);
        DownloadTask task = new DownloadTask();
        task.setCreateTime(new Date());
        task.setTaskState(1);
        task.setTitle(title);
        task.setUrl(taskurl);
        long downloadId = this.downloadManager.enqueue(request);
        task.setDownloadId(downloadId);
        saveDownloadItem(task);
        return;
    }

    //数据库保持下载的基本信息
    private void saveDownloadItem(DownloadTask downloadTask){
        DownloadTaskDao downloadTaskDao = GreenDaoManger.getIntance().getmDaoSession().getDownloadTaskDao();
        downloadTaskDao.insert(downloadTask);
    }

    //获取当前的下载进度
    public DownloadDetail getDownloadProgress(Long downloadId){
        DownloadManager.Query query = new DownloadManager.Query().setFilterById(downloadId);
        Cursor cursor = null;
        try {
            cursor = this.downloadManager.query(query);
            if(cursor != null && cursor.moveToFirst()){
                long status = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
                long downloadedBytes = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                long totalBytes  = cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                DownloadDetail downloadDetail = new DownloadDetail();
                downloadDetail.setCurrentMbs(DateUtils.getFormatFloat((float)(downloadedBytes/1048576l)));
                downloadDetail.setTotalMbs(DateUtils.getFormatFloat((float)(totalBytes/1048576l)));
                if(status == DownloadManager.STATUS_RUNNING){
                    downloadDetail.setStatus(DownloadDetail.DOWNLOAD_RUNNING);
                    downloadDetail.setProgress((int)(downloadedBytes*100/totalBytes));
                }
                else if(status == DownloadManager.STATUS_PAUSED){
                     downloadDetail.setStatus(DownloadDetail.DOWNLOAD_PAUSE);
                     downloadDetail.setProgress((int)(downloadedBytes*100/totalBytes));
                }
                else if(status == DownloadManager.STATUS_SUCCESSFUL){
                    downloadDetail.setStatus(DownloadDetail.DOWNLOAD_COMPLETE);
                    downloadDetail.setProgress(100);
                }
                return downloadDetail;
            }else{
                return null;
            }
        }finally {
            if(cursor != null){
                cursor.close();
            }
        }
    }

}
