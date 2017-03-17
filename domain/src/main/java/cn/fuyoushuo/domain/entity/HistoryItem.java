package cn.fuyoushuo.domain.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

import java.util.Date;

/**
 * Created by QA on 2017/3/8.
 */
@Entity
public class HistoryItem {

    @Id(autoincrement = true)
    private Long id;

    private String historyTitle;

    private String historyUrl;

    /**
     *  历史记录类型,1:网址 2:搜索词
     */
    private int historyType;

    //用户输入搜索的MD5值
    private String inputMd5;

    private Date createTime;

    @Generated(hash = 971693790)
    public HistoryItem(Long id, String historyTitle, String historyUrl,
            int historyType, String inputMd5, Date createTime) {
        this.id = id;
        this.historyTitle = historyTitle;
        this.historyUrl = historyUrl;
        this.historyType = historyType;
        this.inputMd5 = inputMd5;
        this.createTime = createTime;
    }

    @Generated(hash = 1930117983)
    public HistoryItem() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHistoryTitle() {
        return this.historyTitle;
    }

    public void setHistoryTitle(String historyTitle) {
        this.historyTitle = historyTitle;
    }

    public String getHistoryUrl() {
        return this.historyUrl;
    }

    public void setHistoryUrl(String historyUrl) {
        this.historyUrl = historyUrl;
    }

    public int getHistoryType() {
        return this.historyType;
    }

    public void setHistoryType(int historyType) {
        this.historyType = historyType;
    }

    public String getInputMd5() {
        return this.inputMd5;
    }

    public void setInputMd5(String inputMd5) {
        this.inputMd5 = inputMd5;
    }

    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }


}
