package cn.fuyoushuo.vipmovie.ext;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;

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
    public void submitTask(String taskurl,String title){
        if(TextUtils.isEmpty(taskurl)) return;
        DownloadTask downloadTask = new DownloadTask(taskurl, title);
        DownloadManager.Request request = new DownloadManager.Request(downloadTask.getDownloadUri());
        

    }


    /**
     * 封装下载任务
     */
    static class DownloadTask{

        private  String title;

        private  String url;

        public DownloadTask(String url, String title) {
            this.url = url;
            this.title = title;
        }

        public Uri getDownloadUri(){
            return Uri.parse(this.url);
        }
    }

}
