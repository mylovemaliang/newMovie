package cn.fuyoushuo.vipmovie.ext;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

import java.util.Date;

import cn.fuyoushuo.domain.entity.DownloadTask;
import cn.fuyoushuo.domain.greendao.DownloadTaskDao;
import cn.fuyoushuo.vipmovie.GreenDaoManger;

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

}
