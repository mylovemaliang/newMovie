package cn.fuyoushuo.domain.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by QA on 2017/3/23.
 */
@Entity
public class UserTrack {

    @Id(autoincrement = true)
    private Long id;

    private String trackName;

    private String trackUrl;

    private String md5Url;

    private Date createTime;

    @Generated(hash = 616700146)
    public UserTrack(Long id, String trackName, String trackUrl, String md5Url,
            Date createTime) {
        this.id = id;
        this.trackName = trackName;
        this.trackUrl = trackUrl;
        this.md5Url = md5Url;
        this.createTime = createTime;
    }

    @Generated(hash = 538797598)
    public UserTrack() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrackName() {
        return this.trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getTrackUrl() {
        return this.trackUrl;
    }

    public void setTrackUrl(String trackUrl) {
        this.trackUrl = trackUrl;
    }

    public String getMd5Url() {
        return this.md5Url;
    }

    public void setMd5Url(String md5Url) {
        this.md5Url = md5Url;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


}
