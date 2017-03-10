package cn.fuyoushuo.domain.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by QA on 2017/3/8.
 */

@Entity
public class StaticData {

    @Id(autoincrement = true)
    private Long id;

    private String data;

    private Date createTime;

    @Generated(hash = 1292797222)
    public StaticData(Long id, String data, Date createTime) {
        this.id = id;
        this.data = data;
        this.createTime = createTime;
    }

    @Generated(hash = 1924509979)
    public StaticData() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getData() {
        return this.data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
