package cn.fuyoushuo.domain.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by maliang on 2017/3/26.
 */
@Entity
public class DownloadTask{

    @Id(autoincrement = true)
    private Long id;

    private Long downloadId;

    private String url;

    private  String title;

    private Date createTime;

    // 1：进行中 2:暂停  3:下载完毕
    private int taskState;

    @Transient
    private float totalMbs;

    @Transient
    private float currentMbs;

    //下载进度
    @Transient
    private int progress = 0;

    @Generated(hash = 1080514263)
    public DownloadTask(Long id, Long downloadId, String url, String title,
            Date createTime, int taskState) {
        this.id = id;
        this.downloadId = downloadId;
        this.url = url;
        this.title = title;
        this.createTime = createTime;
        this.taskState = taskState;
    }

    @Generated(hash = 1999398913)
    public DownloadTask() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDownloadId() {
        return this.downloadId;
    }

    public void setDownloadId(Long downloadId) {
        this.downloadId = downloadId;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getTaskState() {
        return this.taskState;
    }

    public void setTaskState(int taskState) {
        this.taskState = taskState;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public float getTotalMbs() {
        return totalMbs;
    }

    public void setTotalMbs(float totalMbs) {
        this.totalMbs = totalMbs;
    }

    public float getCurrentMbs() {
        return currentMbs;
    }

    public void setCurrentMbs(float currentMbs) {
        this.currentMbs = currentMbs;
    }
}
