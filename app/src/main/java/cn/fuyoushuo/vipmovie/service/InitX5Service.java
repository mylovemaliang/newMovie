package cn.fuyoushuo.vipmovie.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.TbsListener;

import cn.fuyoushuo.vipmovie.MyApplication;

public class InitX5Service extends Service {

    public static final String TAG = "Initx5Service";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                Log.e("###app qbk init###", " onViewInitFinished is " + arg0);
                stopSelf();
            }

            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };

        QbSdk.setTbsListener(new TbsListener() {
            @Override
            public void onDownloadFinish(int i) {
                Log.d("###app qbk init###","onDownloadFinish");
            }

            @Override
            public void onInstallFinish(int i) {
                Log.d("###app qbk init###","onInstallFinish");
            }

            @Override
            public void onDownloadProgress(int i) {
                Log.d("###app qbk init###","onDownloadProgress:"+i);
            }
        });

        QbSdk.initX5Environment(MyApplication.getContext(),cb);
        return super.onStartCommand(intent, flags, startId);
    }

}
