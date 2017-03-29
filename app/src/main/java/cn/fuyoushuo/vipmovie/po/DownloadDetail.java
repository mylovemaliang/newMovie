package cn.fuyoushuo.vipmovie.po;

import java.io.Serializable;

/**
 * Created by QA on 2017/3/27.
 */

public class DownloadDetail implements Serializable{

    public static final int DOWNLOAD_RUNNING = 1;

    public static final int DOWNLOAD_PAUSE = 2;

    public static final int DOWNLOAD_COMPLETE = 3;

    // 1:正在下载 2:暂停 3:下载完毕
    private int status;

    private int progress;

    private float currentMbs;

    private float totalMbs;

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public float getCurrentMbs() {
        return currentMbs;
    }

    public void setCurrentMbs(float currentMbs) {
        this.currentMbs = currentMbs;
    }

    public float getTotalMbs() {
        return totalMbs;
    }

    public void setTotalMbs(float totalMbs) {
        this.totalMbs = totalMbs;
    }
}
