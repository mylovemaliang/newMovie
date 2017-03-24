package cn.fuyoushuo.domain.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by QA on 2017/3/8.
 */

@Entity
public class BookMark  {

    @Id(autoincrement = true)
    private Long id;

    private String markName;

    private String markUrl;

    private String urlmd5;

    private Date createTime;

    @Generated(hash = 2058362888)
    public BookMark(Long id, String markName, String markUrl, String urlmd5,
            Date createTime) {
        this.id = id;
        this.markName = markName;
        this.markUrl = markUrl;
        this.urlmd5 = urlmd5;
        this.createTime = createTime;
    }

    @Generated(hash = 1704575762)
    public BookMark() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarkName() {
        return this.markName;
    }

    public void setMarkName(String markName) {
        this.markName = markName;
    }

    public String getMarkUrl() {
        return this.markUrl;
    }

    public void setMarkUrl(String markUrl) {
        this.markUrl = markUrl;
    }

    public String getUrlmd5() {
        return this.urlmd5;
    }

    public void setUrlmd5(String urlmd5) {
        this.urlmd5 = urlmd5;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
