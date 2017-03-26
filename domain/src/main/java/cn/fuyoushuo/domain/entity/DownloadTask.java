package cn.fuyoushuo.domain.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by maliang on 2017/3/26.
 */
@Entity
public class DownloadTask{

    @Id(autoincrement = true)
    private long id;

    private long downloadId;

    private String url;

    private  String title;

    private Date createTime;

    // 1：进行中 2：已完成
    private int taskState;

    @Generated(hash = 335457743)
    public DownloadTask(long id, long downloadId, String url, String title,
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

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDownloadId() {
        return this.downloadId;
    }

    public void setDownloadId(long downloadId) {
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
}
