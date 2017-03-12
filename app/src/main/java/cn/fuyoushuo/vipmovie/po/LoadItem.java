package cn.fuyoushuo.vipmovie.po;

import java.io.Serializable;

/**
 * Created by maliang on 2017/3/12.
 */

public class LoadItem implements Serializable {

    private int loadType;

    private String loadURL;

    private String keyWord;

    private Long historyId;

    public LoadItem(int loadType, String loadURL, String keyWord) {
        this.loadType = loadType;
        this.loadURL = loadURL;
        this.keyWord = keyWord;
    }

    public LoadItem bindId(Long id){
        this.historyId = id;
        return this;
    }

    public String getLoadURL() {
        return loadURL;
    }

    public void setLoadURL(String loadURL) {
        this.loadURL = loadURL;
    }

    public int getLoadType() {
        return loadType;
    }

    public void setLoadType(int loadType) {
        this.loadType = loadType;
    }

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public Long getHistoryId() {
        return historyId;
    }

    public void setHistoryId(Long historyId) {
        this.historyId = historyId;
    }



}
